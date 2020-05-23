package com.ykgroup.dayco.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.ykgroup.dayco.uaa.auth.config.AppProperties;

@EnableEurekaClient
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class DaycoUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaycoUaaApplication.class, args);
	}

}
