package com.ykgroup.dayco.gateway.config;

import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

public class RibbonConfiguration {

    /**
     * Returns the {@link IPing} changes the default state checking mechanism
     *
     * @param config the {@link IClientConfig}
     */
    @Bean
    public IPing ribbonPing(final IClientConfig config) {
        return new PingUrl(false, "/actuator/health");
    }

    /**
     * Returns the {@link IRule} to modify default load balancing strategy
     *
     * @param config the {@link IClientConfig}
     */
    @Bean
    public IRule ribbonRule(final IClientConfig config) {
        return new AvailabilityFilteringRule();
    }
}
