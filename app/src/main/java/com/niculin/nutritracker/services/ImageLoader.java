package com.niculin.nutritracker.services;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

public class ImageLoader {
    public static void loadImage(Context context, String imageUrl, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
    }
}

