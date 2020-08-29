package io.github.dayco.external.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class UaaApiRequestConfiguration {

//    @Bean
//    public RequestInterceptor uaaApiRequestHeader(
//            @Value("${feign.user-api.bearerToken}") String token
//    ) {
//        return new BearerAuthRequestInterceptor(token);
//    }
}
