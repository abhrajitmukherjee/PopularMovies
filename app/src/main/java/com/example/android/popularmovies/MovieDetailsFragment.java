package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
    public ArrayList<String> utube = new ArrayList<>();
    public ArrayList<MovieDetailsWebService.VideoResults> videoList = new ArrayList<>();
    SimpleStringRecyclerViewAdapter ad;
    RecyclerView rv;


    public MovieDetailsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("START Videolist size",Integer.toString(videoList.size()));

        rv = (RecyclerView) getActivity().findViewById(R.id.recyclerviewVideo);
        setupRecyclerView(rv);
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
            String id = intent.getStringExtra(getString(R.string.intent_movie_id));

            Picasso.with(getActivity()).load(posterPath).into(((ImageView) rootView.findViewById(R.id.imagePoster)));


            ((TextView) rootView.findViewById(R.id.textTitle))
                    .setText(title);
            ((TextView) rootView.findViewById(R.id.textOverview))
                    .setText(overview);
            ((TextView) rootView.findViewById(R.id.textVote))
                    .setText(vote);
            ((TextView) rootView.findViewById(R.id.textRelease))
                    .setText(release);

            MovieDetailsWebService webObj = new MovieDetailsWebService(getActivity(), this);
            webObj.getVideos(id);
          //  webObj.getReviews(id);
            Log.v("Main Videolist size",Integer.toString(videoList.size()));

            RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.headerLayout);
            rl.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // it was the 1st button
                    String s = "http://www.youtube.com/watch?v=" + utube.get(0);
                    Log.v("test", s+" "+Integer.toString(videoList.size()));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
                }
            });


        }


        return rootView;
    }


    public void setupRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Log.v("Adapter Videolist size",Integer.toString(videoList.size())+" "+utube);

        ad= new SimpleStringRecyclerViewAdapter(getActivity(),
                videoList);
        recyclerView.setAdapter(ad);
            }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<MovieDetailsWebService.VideoResults> mValues;

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<MovieDetailsWebService.VideoResults> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = (ArrayList<MovieDetailsWebService.VideoResults>) items.clone();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_scroll_layout,parent,false);
     //       view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, CheeseDetailActivity.class);
//                    intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                    context.startActivity(intent);
//                    return true;
//                }
//            });
            Log.v("Image View text","http://img.youtube.com/vi/" + mValues.get(position).key + "/1.jpg");
            Picasso.with(holder.mImageView.getContext()).
                    load("http://img.youtube.com/vi/" + mValues.get(position).key + "/0.jpg")
                    .into(holder.mImageView);
            holder.mTextView.setText(mValues.get(position).name);

        }

        @Override
        public int getItemCount() {

            Log.v("COUNT___", Integer.toString(mValues.size()));
            return mValues.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.videoThumbnails);
                mTextView=(TextView) view.findViewById(R.id.videoTitle);

            }

        }
    }

}
