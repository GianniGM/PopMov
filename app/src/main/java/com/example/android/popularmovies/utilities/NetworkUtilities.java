package com.example.android.popularmovies.utilities;


/**
 * Created by giannig on 19/01/17.
 * this is some utilities for build urls and simple doing GET requests
 */

public class NetworkUtilities {

    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
//    public final static String IMAGE_SMALL = "w185/";
    public final static String IMAGE_LARGE = "w500/";


    private final static String TAG = NetworkUtilities.class.getSimpleName();
    public final static String api_key = "INSERT YOU API KEY HERE";

    public final static String QUERY_APY_KEY = "api_key";
    public final static String TYPE = "movie";

    public final static String BASE_URL = "http://api.themoviedb.org/3/";


    public static String imageURLBuilder(String imagePath, String imageSize){
        final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

        return IMAGE_BASE_URL + imageSize + imagePath;
    }
}
