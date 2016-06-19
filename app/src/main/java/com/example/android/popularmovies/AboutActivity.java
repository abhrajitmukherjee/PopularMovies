package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView iv=(ImageView)findViewById(R.id.image_attribution);
        Picasso.
                with(this).
                load("https://assets.tmdb.org/images/logos/var_7_0_tmdb-logo-2_Antitled.png").
                into(iv);
    }
}
