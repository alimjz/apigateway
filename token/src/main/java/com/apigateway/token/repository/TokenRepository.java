package com.apigateway.token.repository;

import com.apigateway.token.util.TokenImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.token.Token;

import java.util.List;

public interface TokenRepository extends MongoRepository<TokenImpl,Byte[]> {

    List<TokenImpl> findByKey(String key);

    List<TokenImpl> findByDigest(String digest);
}
