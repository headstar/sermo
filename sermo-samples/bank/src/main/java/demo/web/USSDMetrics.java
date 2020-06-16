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

package demo.web;

import com.headstartech.sermo.DialogEvent;
import com.headstartech.sermo.SermoDialogException;
import com.headstartech.sermo.SermoDialogExecutor;
import com.headstartech.sermo.SermoDialogListener;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Per Johansson
 */
public class USSDMetrics<S,E extends DialogEvent> implements MeterBinder {

    private final SermoDialogExecutor<S, E> sermoDialogExecutor;

    public USSDMetrics(SermoDialogExecutor<S, E> sermoDialogExecutor) {
        this.sermoDialogExecutor = sermoDialogExecutor;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        MetricsListener<E> metricsListener = new MetricsListener<>(registry);
        sermoDialogExecutor.register(metricsListener);

        FunctionCounter.builder("sermo.dialog.error", metricsListener,
                s -> s.getDialogErrorCount())
              .register(registry);
    }

    static class MetricsListener<E extends DialogEvent> implements SermoDialogListener<E> {

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
        public void postEventHandled(String sessionId, E event, SermoDialogException e) {
            Timer.Sample sample = sessionTimerSamples.remove(sessionId);
            if(e == null) {
                sample.stop(meterRegistry.timer("sermo.dialog.event"));
            } else {
                dialogErrorCounter.incrementAndGet();
            }
        }
    }
}
