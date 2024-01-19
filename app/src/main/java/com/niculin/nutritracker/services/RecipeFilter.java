package com.niculin.nutritracker.services;

import com.niculin.nutritracker.domain.Recipe;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeFilter {
    public static List<Recipe> filterRecipesByCalorieDifference(List<Recipe> recipes, int goal, int consumedCalories) {
        int difference = goal - consumedCalories;
        return recipes.stream().filter(recipe -> recipe.getCalories() <= difference && recipe.getCalories() != 0).collect(Collectors.toList());
    }
}
