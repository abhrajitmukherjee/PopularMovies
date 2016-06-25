package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public ArrayList<String> utube = new ArrayList<>();
    public ArrayList<MovieDetailsWebService.VideoResults> videoList = new ArrayList<>();
    public ArrayList<MovieDetailsWebService.RepoResults> reviewList = new ArrayList<>();
    VideoRecyclerViewAdapter adVideos;
    CommentsRecyclerViewAdapter adReviews;
    RecyclerView rvVideos;
    RecyclerView rvReviews;
    String posterPath;


    public MovieDetailsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("START Videolist size", Integer.toString(videoList.size()));

        rvVideos = (RecyclerView) getActivity().findViewById(R.id.recyclerviewVideo);
        setupRecyclerViewVideos(rvVideos);

        rvReviews = (RecyclerView) getActivity().findViewById(R.id.recyclerviewReviews);
        setupRecyclerViewReviews(rvReviews);
        rvReviews.setNestedScrollingEnabled(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Bundle arguments = getArguments();

            if (arguments!=null){

                posterPath = arguments.getString(getString(R.string.intent_poster_path));
            String title = arguments.getString(getString(R.string.intent_title));
            String overview = arguments.getString(getString(R.string.intent_overview));
            String vote = arguments.getString(getString(R.string.intent_vote_avg));
            String release = arguments.getString(getString(R.string.intent_release_date));
            String id = arguments.getString(getString(R.string.intent_movie_id));

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
            webObj.getReviews(id);
            Log.v("Main Videolist size", Integer.toString(videoList.size()));

            RelativeLayout rl = (RelativeLayout) rootView.findViewById(R.id.headerLayout);
            rl.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // it was the 1st button
                    if (utube.size() > 0) {
                        String s = "http://www.youtube.com/watch?v=" + utube.get(0);
                        Log.v("test", s + " " + Integer.toString(videoList.size()));
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));

                    }

                }
            });


        }


        return rootView;
    }


    public void setupRecyclerViewVideos(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Log.v("Adapter Videolist size", Integer.toString(videoList.size()) + " " + utube);

        adVideos = new VideoRecyclerViewAdapter(getActivity(),
                videoList);
        recyclerView.setAdapter(adVideos);
    }

    public void setupRecyclerViewReviews(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Log.v("Adapter Videolist size", Integer.toString(videoList.size()) + " " + utube);

        adReviews = new CommentsRecyclerViewAdapter(getActivity(),
                reviewList);
        recyclerView.setAdapter(adReviews);
    }


    public static class VideoRecyclerViewAdapter
            extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

        private ArrayList<MovieDetailsWebService.VideoResults> mValues;

        public VideoRecyclerViewAdapter(Context context, ArrayList<MovieDetailsWebService.VideoResults> items) {
            mValues = (ArrayList<MovieDetailsWebService.VideoResults>) items.clone();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.video_scroll_layout, parent, false);
            //       view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final int pos = position;
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = "http://www.youtube.com/watch?v=" + mValues.get(pos).key + "0.jpg";
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
                }
            });
            Log.v("Image View text", "http://img.youtube.com/vi/" + mValues.get(position).key + "/1.jpg");
            Picasso.with(holder.mImageView.getContext()).
                    load("http://img.youtube.com/vi/" + mValues.get(position).key + "/0.jpg")
                    .into(holder.mImageView);
            holder.mTextView.setText(mValues.get(position).name);

        }

        @Override
        public int getItemCount() {

            Log.v("REVIEW COUNT___", Integer.toString(mValues.size()));
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
                mTextView = (TextView) view.findViewById(R.id.videoTitle);

            }

        }
    }


    public static class CommentsRecyclerViewAdapter
            extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> {

        private ArrayList<MovieDetailsWebService.RepoResults> mValuesComments;

        public CommentsRecyclerViewAdapter(Context context, ArrayList<MovieDetailsWebService.RepoResults> items) {
            mValuesComments = (ArrayList<MovieDetailsWebService.RepoResults>) items.clone();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comments_scroll_layout, parent, false);
            //       view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
//            final int pos=position;
//            holder.mImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String s = "http://www.youtube.com/watch?v=" + mValuesComments.get(pos).key+"0.jpg";
//                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(s)));
//                }
//            });
            Picasso.with(holder.mImageView.getContext()).
                    load(R.drawable.avatar)
                    .into(holder.mImageView);
            holder.mTextView.setText(mValuesComments.get(position).content.trim());
            holder.mAuthorView.setText(mValuesComments.get(position).author.trim());


        }

        @Override
        public int getItemCount() {

            Log.v("COMMENT COUNT___", Integer.toString(mValuesComments.size()));
            return mValuesComments.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            public final TextView mAuthorView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_avatar);
                mTextView = (TextView) view.findViewById(R.id.comment_text);
                mAuthorView = (TextView) view.findViewById(R.id.author_name);

            }

        }
    }

}
