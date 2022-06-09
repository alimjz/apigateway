package com.apigateway.core.exception;

import com.apigateway.core.util.RequestMovieTopRank;
import com.apigateway.token.entities.RequestTokenAuth;
import com.apigateway.token.entities.ResponseTokenAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

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


//    public static boolean authorize(String digest, RequestMovieTopRank requestMovieTopRank){
//        if (requestMovieTopRank == null || digest.isEmpty())
//            throw new AuthenticationException(RequestMovieTopRank.class,digest,requestMovieTopRank);
//
//        String tokenUrl = "http://localhost:9091/api/v1/auth/";
//        HttpHeaders httpHeaders = new HttpHeaders();
//        HttpEntity<RequestTokenAuth> requestTokenAuthHttpEntity = new HttpEntity<RequestTokenAuth>(requestMovieTopRank.
//                getRequestTokenAuth(),httpHeaders);
//        ResponseTokenAuth authResponse = new RestTemplate().exchange(tokenUrl+digest, HttpMethod.POST,
//                        requestTokenAuthHttpEntity,ResponseTokenAuth.class)
//                .getBody();
//        assert authResponse != null;
//        return authResponse.getResponseCode() == 0;
//    }


}
