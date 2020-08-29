package io.github.dayco.external.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class BearerAuthRequestInterceptor implements RequestInterceptor {
    private String token;

    public BearerAuthRequestInterceptor(String token) {
        if(token == null) {
            new IllegalArgumentException("Token should not be null");
        }
        this.token = token;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + token);
    }
}
