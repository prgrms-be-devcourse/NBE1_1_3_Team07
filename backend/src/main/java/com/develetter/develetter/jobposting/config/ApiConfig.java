package com.develetter.develetter.jobposting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfig {

    //    @Bean
//    public DefaultUriBuilderFactory builderFactory() { // encoding 모드 지정해줘서 accessKey 값 달라지는 것 방지
//        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
//        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
//        return factory;
//    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
//                .uriBuilderFactory(builderFactory())
                .build();
    }
}
