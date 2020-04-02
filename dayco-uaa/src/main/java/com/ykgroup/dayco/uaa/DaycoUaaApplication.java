package com.ykgroup.dayco.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DaycoUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaycoUaaApplication.class, args);
	}

}
