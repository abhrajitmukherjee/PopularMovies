package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.android.popularmovies.BuildConfig;


public class MoviesContract {

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID+".provider";
    public static final String PATH_MOVIE = "movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
//    public static final String PATH_VIDEO = "videos";
//    public static final String PATH_REVIEW = "reviews";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movieMain";
        public static final String MOVIE_ID = "movie_id";
        public static final String POSTER = "poster";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String RATING = "rating";
        public static final String IS_FAVORITE = "isFavorite";



    }
}


