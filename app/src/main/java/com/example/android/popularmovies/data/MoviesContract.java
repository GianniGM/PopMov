package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by giannig on 2/28/17.
 * This class describes the MoviesContract and respectevely entries
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI =Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    //base columns class
    public static final class MovieEntry implements BaseColumns {

        public final static String NAME_TABLE = "movies";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String ORIGINAL_TITLE = "original_title";
        public static final String POSTER = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final String USER_RATING = "vote_average";
        public static final String MOVIE_ID = "movie-id";

        //these two values can have only values true/false
        public static final String IS_TOP_RATED ="top-rated";
        public static final String IS_MOST_POPULAR = "most-popular";
        public static final String IS_FAVOURITE = "favourite-from-user";

        public static Uri buildMoviesWithDate(long date){
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }
    }
}
