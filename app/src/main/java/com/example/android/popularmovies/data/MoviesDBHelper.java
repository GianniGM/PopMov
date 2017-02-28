package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO DESTROY AND RECREATE THE DB DROP IF EXISTS
    }
}

