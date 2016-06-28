package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MovieDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");


        setContentView(R.layout.activity_movie_details);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_poster_path))) {

            String posterPath = intent.getStringExtra(getString(R.string.intent_poster_path));
            String title = intent.getStringExtra(getString(R.string.intent_title));
            String overview = intent.getStringExtra(getString(R.string.intent_overview));
            String vote = intent.getStringExtra(getString(R.string.intent_vote_avg));
            String release = intent.getStringExtra(getString(R.string.intent_release_date));
            String id = intent.getStringExtra(getString(R.string.intent_movie_id));

            Bundle args = new Bundle();
            args.putString(getString(R.string.intent_poster_path), posterPath);
            args.putString(getString(R.string.intent_title), title);
            args.putString(getString(R.string.intent_overview), overview);
            args.putString(getString(R.string.intent_vote_avg), vote);
            args.putString(getString(R.string.intent_release_date), release);
            args.putString(getString(R.string.intent_movie_id), id);


            MovieDetailsFragment movFragment = new MovieDetailsFragment();
            movFragment.setArguments(args);


            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movie_details_container, movFragment)
                        .commit();
            }
        }


    }


}
