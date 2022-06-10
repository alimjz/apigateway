package com.apigateway.core.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ResponseTokenAuth {

    private int responseCode;
    private String errorMsg;
    private String localDateTime ;

    public ResponseTokenAuth(int responseCode, String errorMsg){
        this.responseCode = responseCode;
        this.errorMsg=errorMsg;
        this.localDateTime = DateTimeFormatter.ofPattern("yyyy-mm-dd-HH:mm:ss").format(LocalDateTime.now());
    }

}
