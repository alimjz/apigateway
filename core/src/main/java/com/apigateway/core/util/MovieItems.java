package com.apigateway.core.util;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieItems {
    private List<ResponseMovie> items;
    public String errorMessage;
}
