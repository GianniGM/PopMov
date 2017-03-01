package com.example.android.popularmovies.utilities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by giannig on 3/1/17.
 */

public interface MoviesInterface {
    @GET("/{type}/{request}")
    Call<Movies> downloadMoviesData(@Path("type") String type,
                                   @Path("request") String request,
                                   @Query(NetworkUtilities.QUERY_APY_KEY) String api_key);

}
