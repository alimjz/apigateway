package com.apigateway.core.controller;

import com.apigateway.core.api.ImdbServices;
import com.apigateway.core.util.MovieItems;
import com.apigateway.core.util.ResponseMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/movies")
public class ApiController {

    ImdbServices imdbServices;

    @Autowired
    public void setImdbServices(ImdbServices imdbServices) {
        this.imdbServices = imdbServices;
    }

    @GetMapping("/toprank")
    public MovieItems topRankMovies(){
        return imdbServices.topRank250Movies();
    }


}
