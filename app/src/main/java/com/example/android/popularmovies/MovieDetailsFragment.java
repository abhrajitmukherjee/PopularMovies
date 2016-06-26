package com.example.android.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
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
    String title;
    String overview;
    String vote;
    String release;
    String id;
    String fileName;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] segments = posterPath.split("/");
                    fileName = segments[segments.length - 1];
                    File file = new File(
                            Environment.getExternalStorageDirectory().getPath()
                                    + fileName);
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


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

        if (arguments != null) {

            posterPath = arguments.getString(getString(R.string.intent_poster_path));
            title = arguments.getString(getString(R.string.intent_title));
            overview = arguments.getString(getString(R.string.intent_overview));
            vote = arguments.getString(getString(R.string.intent_vote_avg));
            release = arguments.getString(getString(R.string.intent_release_date));
            id = arguments.getString(getString(R.string.intent_movie_id));

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ImageView favIcon = (ImageView) getActivity().findViewById(R.id.cardFavIconDetail);
        final int test = 0;
        ContentResolver cr = getActivity().getContentResolver();
        ContentValues cValues = new ContentValues();
        String selection= MoviesContract.MovieEntry.MOVIE_ID+"='"+id+"'";
        Cursor c = cr.query(MoviesContract.MovieEntry.CONTENT_URI, null,
                selection, null, null);
        String dbMovieId="";
        if(c.moveToFirst()){
            do{
                //assing values
                dbMovieId = c.getString(1);

            }while(c.moveToNext());
        }
        if (dbMovieId.equals(id)) {
            Picasso.with(getActivity()).load(R.drawable.hearts).into(favIcon);

        } else {
            Picasso.with(getActivity()).load(R.drawable.heart_blank).into(favIcon);
        }



        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something

                ContentResolver cr = getActivity().getContentResolver();
                ContentValues cValues = new ContentValues();
//                cValues.put(MoviesContract.MovieEntry.MOVIE_ID,id);
//                cValues.put(MoviesContract.MovieEntry.POSTER,posterPath);
//                cValues.put(MoviesContract.MovieEntry.TITLE, title);
//                cValues.put(MoviesContract.MovieEntry.RELEASE_DATE, release);
//                cValues.put(MoviesContract.MovieEntry.RATING, vote);
//                cValues.put(MoviesContract.MovieEntry.IS_FAVORITE, "YES");
//                cr.insert(MoviesContract.MovieEntry.CONTENT_URI, cValues);
//                cr.delete(MoviesContract.MovieEntry.CONTENT_URI, "_ID=113", null);
                String selection= MoviesContract.MovieEntry.MOVIE_ID+"='"+id+"'";
                Cursor c = cr.query(MoviesContract.MovieEntry.CONTENT_URI, null,
                        selection, null, null);
                String dbMovieId="";
                if(c.moveToFirst()){
                    do{
                        //assing values
                         dbMovieId = c.getString(1);

                    }while(c.moveToNext());
                }
                Log.v("onclik's id:",dbMovieId);
                if (dbMovieId.equals(id)) {
                    Picasso.with(getActivity()).load(R.drawable.heart_blank).into(favIcon);
                    cr.delete(MoviesContract.MovieEntry.CONTENT_URI, selection, null);

                } else {
                    Picasso.with(getActivity()).load(R.drawable.hearts).into(favIcon);
                    cValues.put(MoviesContract.MovieEntry.MOVIE_ID,id);
                    cr.insert(MoviesContract.MovieEntry.CONTENT_URI, cValues);
                }


            }
        });

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
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
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
            public final CardView mCardView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.videoThumbnails);
                mTextView = (TextView) view.findViewById(R.id.videoTitle);
                mCardView = (CardView) view.findViewById(R.id.card_view_video);

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
            public final CardView mCardView;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_avatar);
                mTextView = (TextView) view.findViewById(R.id.comment_text);
                mAuthorView = (TextView) view.findViewById(R.id.author_name);
                mCardView = (CardView) view.findViewById(R.id.card_view_comments);

            }

        }
    }

}
