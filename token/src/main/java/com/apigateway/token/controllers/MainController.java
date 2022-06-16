package com.apigateway.token.controllers;

import com.apigateway.token.entities.RequestTokenAuth;
import com.apigateway.token.entities.ResponseTokenAuth;
import com.apigateway.token.util.TokenImpl;
import com.apigateway.token.util.TokenServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.Token;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/")
@RestController
public class MainController {


    private TokenServiceImpl tokenService;


    @Autowired
    public void setTokenService(TokenServiceImpl tokenService) {
        this.tokenService = tokenService;
    }

    @Operation(summary = "allocate a token to entered key",description = "receive key and generate token",operationId = "generateToken()")
    @PostMapping(value = "/token")
    public ResponseEntity<Token> generateToken(@RequestParam("preSharedKey") String preSharedKey) {
        log.info("New Token : " + preSharedKey);
        Token token = tokenService.allocateToken(preSharedKey);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Pre-Shared-Key",preSharedKey);

        return new ResponseEntity<>(token, httpHeaders, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Validate a token",description = "Check whether token is valid or not.",operationId = "authorize()")
    @PostMapping("/auth/{digest}")
    public ResponseEntity<ResponseTokenAuth> authorize(@PathVariable("digest") String digest,
                                                       @RequestBody RequestTokenAuth requestTokenAuth){

        HttpHeaders httpHeaders = new HttpHeaders();
        if (tokenService.isValid(digest,requestTokenAuth.getKey()) &&
                digest.equals(requestTokenAuth.getDigest())){
            httpHeaders.add(HttpHeaders.ACCEPT,"Accept");
            httpHeaders.add(HttpHeaders.AUTHORIZATION,"Authorized");
            httpHeaders.add(HttpHeaders.ALLOW,"Allowed");
            return new ResponseEntity<>(new ResponseTokenAuth(200,"Success"),httpHeaders, HttpStatus.OK);
        }
//        return new Response(1,"No Valid token");
        httpHeaders.add(HttpHeaders.ACCEPT,"Not Accept");
        httpHeaders.add(HttpHeaders.AUTHORIZATION,"Not Authorized");
        httpHeaders.add(HttpHeaders.ALLOW,"Not Allowed");
        httpHeaders.add(HttpHeaders.AUTHORIZATION,"Failed.");
        return new ResponseEntity<>(new ResponseTokenAuth(404,"Invalid Authorization Token"),httpHeaders,HttpStatus.OK);
    }

    @Operation(summary = "return a bunch of tokens",description = "return existed tokens based on pagination",operationId = "getAllTokens()")
    @GetMapping("/{offSet}/{page}")
    public List<TokenImpl> getAllTokens(@PathVariable("offSet") int offSet, @PathVariable("page") int page){
        return tokenService.findAllTokens(offSet,page);
    }



}
