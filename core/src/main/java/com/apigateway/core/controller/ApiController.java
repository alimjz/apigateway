package com.apigateway.core.controller;

import com.apigateway.core.api.ImdbServices;
import com.apigateway.core.exception.AuthenticationException;
import com.apigateway.core.exception.RestExceptionHandler;
import com.apigateway.core.util.MovieItems;
import com.apigateway.core.util.RequestMovieTopRank;
import com.apigateway.token.entities.RequestTokenAuth;
import com.apigateway.token.entities.ResponseTokenAuth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController("/api/v1/movies")
@Slf4j
public class ApiController {

    ImdbServices imdbServices;

    @Value("k_yn6mhy7i")
    private String API_KEY = "k_yn6mhy7i";
    @Value("http://localhost:9091/api/v1/auth/")
    private String tokenUrl = "http://localhost:9091/api/v1/auth/";


    RestTemplate restTemplate;

    @Autowired
    public void setImdbServices(ImdbServices imdbServices) {
        this.imdbServices = imdbServices;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping("/toprank")
    public MovieItems topRankMovies(){
        return imdbServices.topRank250Movies();
    }
    @Operation(summary = "Query top 250 list of IMDB",description = "the token should be valid. otherwise, it return error.",operationId = "topRankByRestTemplate()")
    @PostMapping("/top250")
    public MovieItems topRankByRestTemplate(@RequestHeader("digest") String digest,
                                        @RequestBody RequestMovieTopRank requestMovieTopRank) throws AuthenticationException{
            authorize(digest,requestMovieTopRank);
            String url = "https://imdb-api.com/en/API/Top250Movies/" + API_KEY;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> stringHttpEntity = new HttpEntity<String>(httpHeaders);
            return restTemplate.exchange(url, HttpMethod.GET,stringHttpEntity,MovieItems.class).getBody();

    }

    public void authorize(String digest, RequestMovieTopRank requestMovieTopRank) {
        if (requestMovieTopRank == null || digest.isEmpty())
            throw new AuthenticationException(RequestMovieTopRank.class);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<RequestTokenAuth> requestTokenAuthHttpEntity = new HttpEntity<RequestTokenAuth>(requestMovieTopRank.getRequestTokenAuth(), httpHeaders);

            ResponseTokenAuth authResponse = restTemplate.exchange(tokenUrl + digest, HttpMethod.POST,
                            requestTokenAuthHttpEntity, ResponseTokenAuth.class)
                    .getBody();
            assert authResponse != null;
            if (authResponse.getResponseCode() != 200) {
                throw new AuthenticationException(RequestMovieTopRank.class);
            }
    }
}
