package com.apigateway.token.util;

import com.apigateway.token.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {



    TokenRepository tokenRepository;
    MessageDigest messageDigest;

    @Autowired
    private void setTokenRepository(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }
    @Autowired
    private void setMessageDigest(MessageDigest messageDigest){this.messageDigest= messageDigest;}
    @Override
    public Token allocateToken(String extendedInformation) {
        Assert.notNull(extendedInformation,
                "Must provided non-null extendedInformation (but it can be empty)");
        long creationTime = new Date().getTime();
        String serverSecret = computeServerSecretApplicableAt(creationTime);
        String pseudoRandomNumber = generatePseudoRandomNumber();
        String content = Long.toString(creationTime) + ":" + pseudoRandomNumber + ":"
                + extendedInformation;
        // Compute key
        String sha512Hex = Sha512DigestUtils.shaHex(content + ":" + serverSecret);
        String keyPayload = content + ":" + sha512Hex;
        String key = Utf8.decode(Base64.getEncoder().encode(Utf8.encode(keyPayload)));
        String digest = Base64.getEncoder().encodeToString(messageDigest.digest(key.getBytes()));
        TokenImpl token = new TokenImpl(key, creationTime, extendedInformation,digest);
        log.info("Token Info : " + token.toString());
        tokenRepository.save(token);
        log.info("token saved in Mongo!");
//        log.info(String.valueOf(tokenRepository.findAll()));
        return token;
    }

    private String generatePseudoRandomNumber() {
        return String.valueOf(new SecureRandom().nextInt());
    }

    private String computeServerSecretApplicableAt(long creationTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyymmddHHmmss");
        return dateTimeFormatter.format(currentDateTime);
    }

    @Override
    public Token verifyToken(String key) {
        List<TokenImpl> token = tokenRepository.findByKey(key);
        if (token.size() > 0){
            return token.get(0);
        }
        else
            return null;
    }

    public boolean isValid(String digest){
        List<TokenImpl> tokens = tokenRepository.findByDigest(digest);
        return tokens.size() > 0;
    }
}
