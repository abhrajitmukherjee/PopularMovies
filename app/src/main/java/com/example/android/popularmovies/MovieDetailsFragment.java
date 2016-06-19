package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
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

            Picasso.with(getActivity()).load(posterPath).into(((ImageView) rootView.findViewById(R.id.imagePoster)));


            ((TextView) rootView.findViewById(R.id.textTitle))
                    .setText(title);
            ((TextView) rootView.findViewById(R.id.textOverview))
                    .setText(overview);
            ((TextView) rootView.findViewById(R.id.textVote))
                    .setText(vote);
            ((TextView) rootView.findViewById(R.id.textRelease))
                    .setText(release);


        }
        return rootView;
    }


}
