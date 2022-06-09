package com.apigateway.core.exception;

import org.springframework.util.StringUtils;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(Class clazz){
        super(AuthenticationException.generateMessage(clazz.getSimpleName()));
    }
    private static String generateMessage(String entity) {
        return StringUtils.capitalize(entity) +
                " Authentication Failed" ;
    }
    private String generateMessageAuth(String entity, boolean authResult) {
        return StringUtils.capitalize(entity) + "Invalid Authentication happened.";
    }
}
