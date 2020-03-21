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

import com.headstartech.sermo.MOInput;
import com.headstartech.sermo.USSDApplication;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Per Johansson
 */

@Configuration
public class USSDMetricsConfiguration {

    @Bean
    public <S, E extends MOInput> USSDMetrics<S, E>  ussdMetricsBinder(MeterRegistry meterRegistry, USSDApplication<S, E> ussdApplication) {
        return new USSDMetrics<>(ussdApplication);
    }

}
