package com.apigateway.core.exception;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class LimitExceedException extends RuntimeException{
    public LimitExceedException(Class clazz){
        super(LimitExceedException.generateMessage(clazz.getFields()));
    }
    private static String generateMessage(Field[] field) {
        return Arrays.toString(field) + "Maximum Limit Exceeded." ;
    }

}
