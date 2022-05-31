package com.apigateway.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Utilities {



    public MovieItems movieJsonParser(String response){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MovieItems movieItems = objectMapper.readValue(response,MovieItems.class);
            return movieItems;
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
}
