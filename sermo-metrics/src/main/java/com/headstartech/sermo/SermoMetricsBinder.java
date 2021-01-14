/*
 *  Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.headstartech.sermo;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Per Johansson
 */
public class SermoMetricsBinder<S,E extends DialogEvent> implements MeterBinder {

    private final DialogExecutor<S, E> dialogExecutor;

    public SermoMetricsBinder(DialogExecutor<S, E> dialogExecutor) {
        this.dialogExecutor = dialogExecutor;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        MetricsListener<E> metricsListener = new MetricsListener<>(registry);
        dialogExecutor.addListener(metricsListener);

        FunctionCounter.builder("sermo.dialog.error", metricsListener,
                s -> s.getDialogErrorCount())
              .register(registry);
    }

    static class MetricsListener<E extends DialogEvent> implements DialogListener<E> {

        private AtomicLong dialogErrorCounter = new AtomicLong();
        private final MeterRegistry meterRegistry;
        private final ConcurrentHashMap<String, Timer.Sample> sessionTimerSamples = new ConcurrentHashMap<>();


        public MetricsListener(MeterRegistry meterRegistry) {
            this.meterRegistry = meterRegistry;
        }

        public long getDialogErrorCount() {
            return dialogErrorCounter.get();
        }

        @Override
        public void preEventHandled(String sessionId, E event) {
            sessionTimerSamples.put(sessionId, Timer.start(meterRegistry));
        }

        @Override
        public void postEventHandled(String sessionId, E event, RuntimeException e) {
            Timer.Sample sample = sessionTimerSamples.remove(sessionId);
            if(e == null) {
                sample.stop(meterRegistry.timer("sermo.dialog.event"));
            } else {
                dialogErrorCounter.incrementAndGet();
            }
        }
    }
}
