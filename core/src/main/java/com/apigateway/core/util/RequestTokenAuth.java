package com.apigateway.core.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestTokenAuth {
    private String digest;
    private String key;

    public RequestTokenAuth(String digest,String id){
        this.digest = digest;
        this.key = id;
    }
}
