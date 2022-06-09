package com.apigateway.token.dto;

import lombok.Data;

@Data
public class TokenImpDto {

    private String key;
    private long keyCreationTime;
    private String extendedInformation;

    private String digestive;

}
