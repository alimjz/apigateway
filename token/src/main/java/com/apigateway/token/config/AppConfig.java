package com.apigateway.token.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


import java.security.MessageDigest;

@Configuration
public class AppConfig {
    @Bean
    @SneakyThrows
    public MessageDigest getMessageDigest(){
        return MessageDigest.getInstance("MD5");
    }




}
