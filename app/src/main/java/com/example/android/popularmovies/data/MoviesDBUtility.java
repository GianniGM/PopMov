package com.example.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.utilities.Movie;
import com.example.android.popularmovies.utilities.Movies;
import com.example.android.popularmovies.data.MoviesContract.MovieEntry;
import com.example.android.popularmovies.utilities.NetworkUtilities;

/**
 * Created by giannig on 3/1/17.
 * utility function for adding values to database
 */




public class MoviesDBUtility {

    public static final String[] MOVIE_PROJECTION = {
            MovieEntry.MOVIE_ID,
            MovieEntry.RELEASE_DATE,
            MovieEntry.ORIGINAL_TITLE,
            MovieEntry.VOTE_AVERAGE,
            MovieEntry.OVERVIEW,
            MovieEntry.POSTER,
    };

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_RELEASE_DATE = 1;
    public static final int INDEX_ORIGINAL_TITLE = 2;
    public static final int INDEX_VOTE_AVERAGE = 3;
    public static final int INDEX_OVERVIEW = 4;
    public static final int INDEX_POSTER = 5;

    private static final int IS_TRUE = 1;
    private static final int IS_FALSE = 0;

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
// TODO            Log.e("POSTERPATH:", posterPath );
            c.put(MovieEntry.POSTER, posterPath);

//            c.put(type, IS_TRUE);

            contentValuesArray[i] = c;

        }

        if (contentValuesArray.length != 0){
            ContentResolver contentResolver = ctx.getContentResolver();

//            TODO
//            contentResolver.delete(
//                    MovieEntry.CONTENT_URI,
//                    null,
//                    null
//            );


//            inserted = contentResolver.bulkInsert(MovieEntry.CONTENT_URI, contentValuesArray);
            inserted = contentResolver.bulkInsert(uri, contentValuesArray);

        }

        return inserted;
    }

//    public static int addTopRatedInDB(Movies body, Context ctx) {
//        return addInDB(body,ctx,MovieEntry.IS_TOP_RATED);
//    }
//
//    public static int addMostPopularInDB(Movies body, Context ctx) {
//        return addInDB(body,ctx,MovieEntry.IS_MOST_POPULAR);
//    }
}
