package com.ykgroup.dayco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DaycoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaycoApplication.class, args);
	}

}
