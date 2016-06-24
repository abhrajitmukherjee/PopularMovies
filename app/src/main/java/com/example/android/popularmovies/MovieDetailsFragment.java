package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    public ArrayList<String> utube=new ArrayList<>();

    public MovieDetailsFragment() {
    }
    public void playYoutube(){

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_poster_path))) {
            String posterPath = intent.getStringExtra(getString(R.string.intent_poster_path));
            String title = intent.getStringExtra(getString(R.string.intent_title));
            String overview = intent.getStringExtra(getString(R.string.intent_overview));
            String vote = intent.getStringExtra(getString(R.string.intent_vote_avg));
            String release = intent.getStringExtra(getString(R.string.intent_release_date));
            String id=intent.getStringExtra(getString(R.string.intent_movie_id));

            Picasso.with(getActivity()).load(posterPath).into(((ImageView) rootView.findViewById(R.id.imagePoster)));


            ((TextView) rootView.findViewById(R.id.textTitle))
                    .setText(title);
            ((TextView) rootView.findViewById(R.id.textOverview))
                    .setText(overview);
            ((TextView) rootView.findViewById(R.id.textVote))
                    .setText(vote);
            ((TextView) rootView.findViewById(R.id.textRelease))
                    .setText(release);

            MovieDetailsWebService webObj=new MovieDetailsWebService(getActivity(),this);
            webObj.getVideos(id);
            webObj.getReviews(id);


            RelativeLayout rl=(RelativeLayout) rootView.findViewById(R.id.headerLayout);
            rl.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {
                    // it was the 1st button
                    String s="http://www.youtube.com/watch?v="+utube.get(0);
                    Log.v("test",s);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
                }
            });


        }



        return rootView;
    }


}
