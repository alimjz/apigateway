package com.apigateway.token.entities;

import lombok.Data;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Component;

@Data
@Component
public class User{

    private String userName;
    private String password;
    private Token token;

}
