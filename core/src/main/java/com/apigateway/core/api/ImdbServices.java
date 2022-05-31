package com.apigateway.core.api;

import com.apigateway.core.util.MovieItems;
import com.apigateway.core.util.ResponseMovie;
import com.apigateway.core.util.Utilities;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ImdbServices {

    private String API_KEY = "k_yn6mhy7i";


    public String searchMovieByName(String name) {
        try {
            String url = "https://imdb-api.com/en/API/SearchMovie/" + API_KEY + "/" + name;
            return new RestTemplate().getForObject(url, String.class);
        } catch (ResourceAccessException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public MovieItems topRank250Movies() {
        String url = "https://imdb-api.com/en/API/Top250Movies/" + API_KEY;
        String response = new RestTemplate().getForObject(url, String.class);
//        List<ResponseMovie> responseMovieList = new Utilities().movieJsonParser(response);
//        return new RestTemplate().getForObject(url, String.class);
        return new Utilities().movieJsonParser(response);
    }
}
