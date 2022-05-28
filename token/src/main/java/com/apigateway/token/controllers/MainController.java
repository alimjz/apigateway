package com.apigateway.token.controllers;

import com.apigateway.token.entities.Response;
import com.apigateway.token.util.TokenImpl;
import com.apigateway.token.util.TokenServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Token;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequestMapping("/api/v1/")
@RestController
public class MainController {


    private TokenServiceImpl tokenService;

    @Autowired
    public void setTokenService(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }


    @PostMapping(value = "/token")
    public Token generateToken(@RequestParam("preSharedKey") String preSharedKey) {
        log.info("New Token : " + preSharedKey);
        return tokenService.allocateToken(preSharedKey);
    }

    @GetMapping("/auth")
    public Response authorize(@RequestParam("digest") String token){

        if (tokenService.isValid(token)){
            return new Response(0,"Success");
        }
        return new Response(1,"No Valid token");
    }

}
