package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.popularmovies.utilities.Movie;
import com.example.android.popularmovies.utilities.Movies;
import com.example.android.popularmovies.data.MoviesContract.MovieEntry;
import com.example.android.popularmovies.utilities.NetworkUtilities;

/**
 * Created by giannig on 3/1/17.
 * utility function for adding values to database
 */

public class MoviesDBUtility {
    private static final int IS_TRUE = 1;
    private static final int IS_FALSE = 0;

    private static int addInDB(Movies body, Context ctx, String type){
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

            c.put(type, IS_TRUE);

            contentValuesArray[i] = c;

        }


        if (contentValuesArray.length != 0){
            ContentResolver contentResolver = ctx.getContentResolver();
            contentResolver.bulkInsert(MovieEntry.CONTENT_URI, contentValuesArray);
        }

        return inserted;
    }

    public static int addTopRatedInDB(Movies body, Context ctx) {
        return addInDB(body,ctx,MovieEntry.IS_TOP_RATED);
    }

    public static int addMostPopularInDB(Movies body, Context ctx) {
        return addInDB(body,ctx,MovieEntry.IS_MOST_POPULAR);
    }
}
