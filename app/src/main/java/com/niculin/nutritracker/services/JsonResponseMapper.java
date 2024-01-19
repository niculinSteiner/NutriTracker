package com.niculin.nutritracker.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niculin.nutritracker.domain.Recipe;

import java.util.List;

public class JsonResponseMapper {
    public static List<Recipe> mapRecipeResponse(String response) {
        List<Recipe> allRecipes;
        ObjectMapper mapper = new ObjectMapper();
        try {
            allRecipes = mapper.readValue(response, new TypeReference<List<Recipe>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return allRecipes;
    }
}
