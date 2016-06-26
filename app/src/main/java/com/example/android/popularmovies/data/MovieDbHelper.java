package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieList.db";

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " (" +
                        MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY, "+
                        MoviesContract.MovieEntry.MOVIE_ID + " TEXT , " +
                        MoviesContract.MovieEntry.POSTER + " TEXT, " +
                        MoviesContract.MovieEntry.TITLE + " TEXT, " +
                        MoviesContract.MovieEntry.RELEASE_DATE + " TEXT, " +
                        MoviesContract.MovieEntry.RATING+ " TEXT, " +
                        MoviesContract.MovieEntry.IS_FAVORITE + " TEXT);"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }


}