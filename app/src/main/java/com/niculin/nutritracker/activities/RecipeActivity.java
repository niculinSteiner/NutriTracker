package com.niculin.nutritracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niculin.nutritracker.R;
import com.niculin.nutritracker.domain.Recipe;
import com.niculin.nutritracker.services.ImageLoader;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RecipeActivity extends AppCompatActivity {

    public static final String ACTUAL_CALORIES_KEY = "actual_calorie";
    public static final String TAG = "NotificationCompat";
    private final String url = "https://api.sampleapis.com/recipes/recipes";


    int goal;
    int consumedCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        goBackActionListener();
        doHttpRequest();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        goal = Integer.parseInt(intent.getExtras().getString(TrackActivity.GOAL_KEY));
        consumedCalories = Integer.parseInt(intent.getExtras().getString(ACTUAL_CALORIES_KEY));
    }

    private void goBackActionListener() {
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void doHttpRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this::upDateView, error -> Log.i(TAG, "Error :" + error.toString()));
        requestQueue.add(stringRequest);
    }

    private void upDateView(String response) {
        List<Recipe> allRecipes;
        allRecipes = mapResponse(response);
        List<Recipe> filteredRecipes = filterRecipes(allRecipes);
        Recipe recipe1 = null;
        if (!filteredRecipes.isEmpty()) {
            recipe1 = filteredRecipes.get(new Random().nextInt(filteredRecipes.size()));
            CardView cardView = (CardView) findViewById(R.id.recipeCard1);
            TextView cardTitle = (TextView) cardView.findViewById(R.id.recipeTitle1);
            TextView cardText = (TextView) cardView.findViewById(R.id.cardBody1);
            ImageView imageView = (ImageView) findViewById(R.id.recipePictureImageView1);
            fillRecipeCard(recipe1, cardTitle, cardText, imageView);
        }
        if (filteredRecipes.size() >= 2) {
            Recipe recipe2;
            do {
                recipe2 = filteredRecipes.get(new Random().nextInt(filteredRecipes.size()));
            } while (recipe1.getId() == recipe2.getId());
            CardView cardView = (CardView) findViewById(R.id.recipeCard2);
            TextView cardTitle = (TextView) cardView.findViewById(R.id.recipeTitle2);
            TextView cardText = (TextView) cardView.findViewById(R.id.cardBody2);
            ImageView imageView = (ImageView) findViewById(R.id.recipePictureImageView2);
            fillRecipeCard(recipe2, cardTitle, cardText, imageView);
        }
    }

    private void fillRecipeCard(Recipe recipe, TextView cardTitle, TextView cardText, ImageView imageView) {
        cardTitle.setText(recipe.getTitle());
        String recipeContent = recipe.getCookTime() + "\n" + recipe.getIngredients() + "\n" + recipe.getCalories();
        cardText.setText(recipeContent);
        String photoUrlString = recipe.getPhotoUrl();
        ImageLoader.loadImage(this, photoUrlString, imageView);

    }

    private static List<Recipe> mapResponse(String response) {
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

    private List<Recipe> filterRecipes(List<Recipe> recipes) {
        int difference = goal - consumedCalories;
        return recipes.stream().filter(recipe -> recipe.getCalories() <= difference).collect(Collectors.toList());
    }

}