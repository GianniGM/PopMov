package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MoviesContract.MovieEntry;

/**
 * Created by giannig on 2/28/17.
 * Manage the database
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "movies.db";
    public static final int DB_VERSION = 1;

    public MoviesDBHelper(Context ctx){
        super (ctx, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO CREATING DATABASE AND RESPECTIVELY FIELDS
        //REMEMBER TWO VALUES TRUE/FALSE

        final String SQL_CREATE_TABLE_STRING = "CREATE TABLE " + MovieEntry.NAME_TABLE +

                " (" +
                MovieEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.ORIGINAL_TITLE       + " TEXT NOT NULL, " +
                MovieEntry.POSTER               + " TEXT NOT NULL, " +
                MovieEntry.OVERVIEW             + " TEXT NOT NULL, " +
                MovieEntry.RELEASE_DATE         + " INTEGER NOT NULL, " +
                MovieEntry.USER_RATING          + " REAL NOT NULL, " +

                //integers values 0 if false,
                // > 0 if true
                MovieEntry.IS_TOP_RATED         + " INTEGER NOT NULL, " +
                MovieEntry.IS_MOST_POPULAR      + " INTEGER NOT NULL, " +
                ");" ;

        db.execSQL(SQL_CREATE_TABLE_STRING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO DESTROY AND RECREATE THE DB DROP IF EXISTS
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.NAME_TABLE);
        onCreate(db);
    }
    
    public static boolean isTopRated(int value){
        if (value > 0) return true;
        else return false;
    }

    public static boolean isMostPopular(int value){
        if (value > 0) return true;
        else return false;
    }
}

