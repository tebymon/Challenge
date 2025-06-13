package com.ecampos.challenge.infrastructure.config.retry;


import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RetryConfig {

    @Bean
    public RetryRegistry retryRegistry() {
        RetryRegistry registry = RetryRegistry.ofDefaults();

        registry.retry("percentageRetry").getEventPublisher()
                .onRetry(event -> log.info("Reintento número {} para operación '{}'", event.getNumberOfRetryAttempts(), event.getName()));

        return registry;
    }
}