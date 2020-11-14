package com.headstartech.sermo;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SermoMetricsConfiguration {

    @Bean
    public <S, E extends DialogEvent> SermoMetricsBinder<S, E>  sermoMetricsBinder(MeterRegistry meterRegistry, DialogExecutor<S, E> dialogExecutor) {
        return new SermoMetricsBinder<>(dialogExecutor);
    }

}
