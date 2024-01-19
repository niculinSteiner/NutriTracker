package com.niculin.nutritracker.activities;

import static com.niculin.nutritracker.services.JsonResponseMapper.mapRecipeResponse;
import static com.niculin.nutritracker.services.RecipeFilterService.filterRecipesByCalorieDifference;

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
import com.niculin.nutritracker.R;
import com.niculin.nutritracker.domain.Recipe;
import com.niculin.nutritracker.services.ImageLoader;

import java.util.List;
import java.util.Random;

public class RecipeActivity extends AppCompatActivity {

    public static final String ACTUAL_CALORIES_KEY = "actual_calorie";
    private static final String TAG = "NotificationCompat";


    private int goal;
    private int consumedCalories;

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
        String url = "https://api.sampleapis.com/recipes/recipes";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this::upDateView, error -> Log.i(TAG, "Error :" + error.toString()));
        requestQueue.add(stringRequest);
    }

    private void upDateView(String response) {
        List<Recipe> allRecipes = mapRecipeResponse(response);
        List<Recipe> filteredRecipes = filterRecipesByCalorieDifference(allRecipes, goal, consumedCalories);
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
}