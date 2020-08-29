package io.github.dayco.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import io.github.dayco.gateway.config.RibbonConfiguration;

@EnableZuulProxy
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
@EnableEurekaClient
@SpringBootApplication
public class DaycoGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaycoGatewayApplication.class, args);
	}
}
