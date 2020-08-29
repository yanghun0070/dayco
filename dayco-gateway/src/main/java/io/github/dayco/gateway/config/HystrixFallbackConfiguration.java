package io.github.dayco.gateway.config;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HystrixFallbackConfiguration {

    @Bean
    public FallbackProvider fallbackProvider() {
        return new ZuulFallbackProvider();
    }
}
