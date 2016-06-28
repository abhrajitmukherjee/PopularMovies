package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwoPane;


    @Override
    public void onItemSelected(ArrayList<String[]> mThumbIds,int position){

        if (mTwoPane){

            Bundle args = new Bundle();
            args.putString(getString(R.string.intent_poster_path), mThumbIds.get(position)[0]);
            args.putString(getString(R.string.intent_title), mThumbIds.get(position)[1]);
            args.putString(getString(R.string.intent_overview), mThumbIds.get(position)[2]);
            args.putString(getString(R.string.intent_vote_avg), mThumbIds.get(position)[3]);
            args.putString(getString(R.string.intent_release_date), mThumbIds.get(position)[4]);
            args.putString(getString(R.string.intent_movie_id), mThumbIds.get(position)[5]);



            MovieDetailsFragment movFragment = new MovieDetailsFragment();
            movFragment.setArguments(args);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, movFragment)
                    .commit();

        }
        else
        {

            Intent intent = new Intent(this, MovieDetailsActivity.class)
                    .putExtra(getString(R.string.intent_poster_path), mThumbIds.get(position)[0])
                    .putExtra(getString(R.string.intent_title), mThumbIds.get(position)[1])
                    .putExtra(getString(R.string.intent_overview), mThumbIds.get(position)[2])
                    .putExtra(getString(R.string.intent_vote_avg), mThumbIds.get(position)[3])
                    .putExtra(getString(R.string.intent_release_date), mThumbIds.get(position)[4])
                    .putExtra(getString(R.string.intent_movie_id), mThumbIds.get(position)[5]);
            startActivity(intent);

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_detail_container)!=null){
            mTwoPane=true;
        }
        else{
            mTwoPane=false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {


            Intent intent = new Intent(this, AboutActivity.class);

            startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }



}
