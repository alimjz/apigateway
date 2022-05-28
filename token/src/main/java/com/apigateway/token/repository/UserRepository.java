package com.apigateway.token.repository;

import com.apigateway.token.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,String> {

}
