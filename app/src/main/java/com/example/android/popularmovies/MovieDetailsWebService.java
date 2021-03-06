package com.example.android.popularmovies;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MovieDetailsWebService {

    FragmentActivity mParentActivity;
    MovieDetailsFragment mFragment;

    MovieDetailsWebService(FragmentActivity inpContext, MovieDetailsFragment fg) {
        mParentActivity = inpContext;
        mFragment = fg;
    }

    public void getReviews(String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService service = retrofit.create(MovieService.class);
        Call<RepoReviews> repos = service.listRepos(id, "reviews");
        repos.enqueue(new Callback<RepoReviews>() {
            @Override
            public void onResponse(Call<RepoReviews> call, Response<RepoReviews> response) {
                Log.d("Test", "onResponse - Status : " + response.code());

                if (response.isSuccessful()) {
                    RepoReviews rr = response.body();
                    if (rr.results.size() > 0)
                        mFragment.reviewList = new ArrayList<>(rr.results);
                    mFragment.setupRecyclerViewReviews(mFragment.rvReviews);


                    //
                    // tasks available
                } else {
                    try {
                        Log.d("Test", "Error - Status : " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("Test", e.toString());
                    }

                }
            }


            @Override
            public void onFailure(Call<RepoReviews> call, Throwable t) {
                Log.d("Error----", t.getMessage());
            }
        });


    }

    public void getVideos(String id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService service = retrofit.create(MovieService.class);
        Call<MovieVideos> repos = service.listVideos(id, "videos");
        repos.enqueue(new Callback<MovieVideos>() {
            @Override
            public void onResponse(Call<MovieVideos> call, Response<MovieVideos> response) {
                Log.d("Test", "onResponse - Status : " + response.code());

                if (response.isSuccessful()) {
                    MovieVideos rv = response.body();
                    if (rv.results.size() > 0) {


                        String utube = "http://img.youtube.com/vi/" + rv.results.get(0).key + "/0.jpg";
                        mFragment.utube.add(rv.results.get(0).key);
                        mFragment.videoList = new ArrayList<>(rv.results);
                        mFragment.setupRecyclerViewVideos(mFragment.rvVideos);

                        ImageView iv = (ImageView) mParentActivity.findViewById(R.id.videoHeader);
                        Picasso.with(mParentActivity).load(utube).into(iv);

                        ImageView iv1 = (ImageView) mParentActivity.findViewById(R.id.playButton);
                        Picasso.with(mParentActivity).load(R.drawable.play_button).into(iv1);

                    } else {
                        ImageView iv = (ImageView) mParentActivity.findViewById(R.id.videoHeader);
                        Picasso.with(mParentActivity).load(mFragment.posterPath).into(iv);

                        ImageView iv1 = (ImageView) mParentActivity.findViewById(R.id.playButton);
                        Picasso.with(mParentActivity).load("http://img.youtube.com/vi/0.jpg").into(iv1);
                    }


                    //
                    // tasks available
                } else {

                    try {
                        Log.d("Test", "Error - Status : " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("Test", e.toString());
                    }

                }
            }


            @Override
            public void onFailure(Call<MovieVideos> call, Throwable t) {
                Log.d("Error----", t.getMessage());
            }
        });


    }

    public interface MovieService {
        @GET("3/movie/{id}/{type}?api_key=" + BuildConfig.MOVIES_API_KEY)
        Call<RepoReviews> listRepos(@Path("id") String id, @Path("type") String type);

        @GET("3/movie/{id}/{type}?api_key=" + BuildConfig.MOVIES_API_KEY)
        Call<MovieVideos> listVideos(@Path("id") String id, @Path("type") String type);
    }

    //For Movie Details Reviews
    public class RepoResults {
        String id;
        String author;
        String content;
        String url;
    }

    public class RepoReviews {
        public List<RepoResults> results = new ArrayList<RepoResults>();
        String id;
        String page;
        String total_pages;
        String total_results;
    }

    //For Movie Videos
    public class MovieVideos {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("results")
        @Expose
        public ArrayList<VideoResults> results = new ArrayList<VideoResults>();

    }

    public class VideoResults {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("iso_639_1")
        @Expose
        public String iso6391;
        @SerializedName("iso_3166_1")
        @Expose
        public String iso31661;
        @SerializedName("key")
        @Expose
        public String key;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("site")
        @Expose
        public String site;
        @SerializedName("size")
        @Expose
        public Integer size;
        @SerializedName("type")
        @Expose
        public String type;

    }


}
