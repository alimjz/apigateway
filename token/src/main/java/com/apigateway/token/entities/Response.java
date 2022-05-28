package com.apigateway.token.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class Response {

    private int responseCode;
    private String errorMsg;
}
