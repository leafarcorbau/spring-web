package com.dh.sp.web.mvc.config;

import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockServerConfig {

    @Value("${dh.mock.server.host}")
    private String host;
    @Value("${dh.mock.server.port}")
    private int port;

    @Bean
    public MockServerClient mockServerClient(){
        return new MockServerClient(host, port);
    }
}
