package com.ykgroup.dayco.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DaycoRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaycoRegistryApplication.class, args);
    }

}
