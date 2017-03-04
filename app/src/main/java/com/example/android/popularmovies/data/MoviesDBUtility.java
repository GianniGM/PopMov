package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.android.popularmovies.movies.Movie;
import com.example.android.popularmovies.movies.Movies;
import com.example.android.popularmovies.data.MoviesContract.MovieEntry;
import com.example.android.popularmovies.utilities.NetworkUtilities;

/**
 * Created by giannig on 3/1/17.
 * utility function for adding values to database
 */




public class MoviesDBUtility {

    //TODO MIGLIORARE STA ROBA QUA
    public static final String[] MOVIE_PROJECTION = {
            MovieEntry.POSTER,
            MovieEntry.MOVIE_ID,
            MovieEntry.ORIGINAL_TITLE,
            MovieEntry.VOTE_AVERAGE,
            MovieEntry.OVERVIEW,
            MovieEntry.RELEASE_DATE,
            MovieEntry.IS_FAVOURITE,
    };

    public static final int INDEX_POSTER = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_ORIGINAL_TITLE = 2;
    public static final int INDEX_VOTE_AVERAGE = 3;
    public static final int INDEX_OVERVIEW = 4;
    public static final int INDEX_RELEASE_DATE = 5;
    public static final int INDEX_IS_FAVOURITE = 6;

    public static final int IS_TRUE = 1;
    private static final int IS_FALSE = 0;

    public static final String NAME_PATH_BEST = "best";
    public static final String NAME_PATH_ID = "id";

    public static int updateDB(Uri uri, Context ctx, String MovieId,boolean favourite){

        ContentValues contentValues = new ContentValues();
        ContentResolver contentResolver = ctx.getContentResolver();
        String[] selectionArgs = new String[]{MovieId};

        if(favourite)
            contentValues.put(MovieEntry.IS_FAVOURITE, IS_TRUE);
        else
            contentValues.put(MovieEntry.IS_FAVOURITE, IS_FALSE);

        int updated =  contentResolver.update(
                uri,
                contentValues,
                null,
                selectionArgs
        );

        return updated;
    }

    public static int addInDB(Uri uri, Movies body, Context ctx){
        Movie[] moviesArray = body.getMovies();

        int n = moviesArray.length;
        int inserted = 0;

        ContentValues[] contentValuesArray = new ContentValues[n];

        for (int i = 0; i < n; i++) {
            Movie m = moviesArray[i];
            ContentValues c = new ContentValues();
            c.put(MovieEntry.MOVIE_ID, m.getMovieID());
            c.put(MovieEntry.RELEASE_DATE, m.getReleaseDate());
            c.put(MovieEntry.ORIGINAL_TITLE, m.getOriginalTitle());
            c.put(MovieEntry.OVERVIEW, m.getOverview());
            c.put(MovieEntry.VOTE_AVERAGE, m.getVoteAverage());

            String posterPath = NetworkUtilities.imageURLBuilder(m.getPosterPath(), NetworkUtilities.IMAGE_LARGE);
            c.put(MovieEntry.POSTER, posterPath);

            contentValuesArray[i] = c;

        }

        if (contentValuesArray.length != 0){
            ContentResolver contentResolver = ctx.getContentResolver();

            inserted = contentResolver.bulkInsert(uri, contentValuesArray);

        }

        return inserted;
    }

}
