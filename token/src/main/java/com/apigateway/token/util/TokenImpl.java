package com.apigateway.token.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.token.Token;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Data
@Document(collection = "tokens")
@NoArgsConstructor
public class TokenImpl implements Token {

    @Id
    private String key;
    private long keyCreationTime;
    private String extendedInformation;

    private String digest;


    public TokenImpl(String key, long creationTime, String extendedInformation, String digest) {
        this.key = key;
        this.keyCreationTime = creationTime;
        this.extendedInformation = extendedInformation;
        this.digest = digest;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public long getKeyCreationTime() {
        return this.keyCreationTime;
    }

    @Override
    public String getExtendedInformation() {
        return extendedInformation;
    }



    @Override
    public String toString() {
        return "TokenImpl{" +
                "keyTime=" + keyCreationTime +
                ", entryKey='" + extendedInformation + '\'' +
                ", Token=" + digest +
                '}';
    }
}
