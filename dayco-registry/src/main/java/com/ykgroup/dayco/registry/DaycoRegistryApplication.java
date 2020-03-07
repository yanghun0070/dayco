package com.ykgroup.dayco.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import com.ykgroup.dayco.registry.config.RibbonConfiguration;

@EnableEurekaServer
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
@EnableZuulProxy
@SpringBootApplication
public class DaycoRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaycoRegistryApplication.class, args);
    }

}
