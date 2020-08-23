package com.ykgroup.dayco.gateway.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import com.netflix.hystrix.exception.HystrixTimeoutException;

/**
 * When a circuit for a given route in Zuul is tripped,
 * you can provide a fallback response by {@link ZuulFallbackProvider}.
 */
public class ZuulFallbackProvider implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return fallbackResponse(HttpStatus.GATEWAY_TIMEOUT, cause);
        } else {
            return fallbackResponse(HttpStatus.INTERNAL_SERVER_ERROR, cause);
        }
    }

    private final ClientHttpResponse fallbackResponse(HttpStatus status, Throwable cause) {

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.toString();
            }

            @Override
            public void close() {}

            @Override
            public InputStream getBody() throws IOException {
                HttpStatus status = getStatusCode();
                if(status == HttpStatus.GATEWAY_TIMEOUT) {
                    return new ByteArrayInputStream("Failed to handle the request in given thread time".getBytes());
                } else {
                    return new ByteArrayInputStream("Failed server error ".getBytes());
                }
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
