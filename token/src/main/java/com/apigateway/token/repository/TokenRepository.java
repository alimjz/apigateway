package com.apigateway.token.repository;

import com.apigateway.token.util.TokenImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.token.Token;

import java.util.List;

public interface TokenRepository extends MongoRepository<TokenImpl,String> {

    List<TokenImpl> findByKey(String key);

    List<TokenImpl> findByDigest(String digest);

    @Query("FROM TOKENS")
    List<TokenImpl> findAllPages(Pageable pagination);
}
