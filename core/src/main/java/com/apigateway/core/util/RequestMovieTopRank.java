package com.apigateway.core.util;

//import com.apigateway.token.entities.RequestTokenAuth;
import com.apigateway.token.entities.RequestTokenAuth;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestMovieTopRank {


    private RequestTokenAuth requestTokenAuth;
    private String requestName;

    public RequestMovieTopRank(RequestTokenAuth requestTokenAuth){
        this.requestTokenAuth = requestTokenAuth;
    }
}
