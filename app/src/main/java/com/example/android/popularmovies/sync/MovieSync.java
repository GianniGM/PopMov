package com.example.android.popularmovies.sync;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDBUtility;
import com.example.android.popularmovies.movies.Movies;
import com.example.android.popularmovies.movies.MoviesInterface;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giannig on 3/1/17.
 */

public class MovieSync implements Callback<Movies>{

    private static final String TAG = MovieSync.class.getSimpleName();
    private MoviesInterface instance;
    private Call<Movies> retrofitCall;
    private Context context;

    private static String TYPE = NetworkUtilities.POPULAR;

    synchronized public static MovieSync startSynchronizing(Context ctx){
        Log.d(TAG, "Loading service");

        MovieSync m = new MovieSync();
        m.Start(ctx);
        return m;
    }

    synchronized public static void stopSynchronizing(MovieSync m){
        m.Stop();
    }

    private void Start(Context ctx){

        Log.d(TAG, "Service started");
        instance = buildMovieInstance();
        loadMovieData(TYPE);
        context = ctx;

    }

    private void Stop(){
        if(!retrofitCall.isCanceled())
            retrofitCall.cancel();
    }

    private MoviesInterface buildMovieInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtilities.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MoviesInterface.class);
    }

    private void loadMovieData(String query){
        instance.downloadMoviesData(NetworkUtilities.TYPE, query, NetworkUtilities.api_key).enqueue(this);
    }

    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        retrofitCall = call;

        Log.e(TAG, "looking for data: " + TYPE);
        if(!response.isSuccessful()){
            Log.d(TAG, "Unable to connect");
            return;
        }


        switch (TYPE){

            case NetworkUtilities.POPULAR:

                Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(MoviesDBUtility.NAME_PATH_BEST)
                        .appendPath(MoviesContract.MovieEntry.IS_MOST_POPULAR)
                        .build();

                int inserted = MoviesDBUtility.addInDB(uri, response.body(), context);
                Log.e(TAG, "most popular inserted: " + inserted);
                TYPE = NetworkUtilities.TOP_RATED;
                loadMovieData(NetworkUtilities.TOP_RATED);
                break;

            case NetworkUtilities.TOP_RATED:

                uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(MoviesDBUtility.NAME_PATH_BEST)
                        .appendPath(MoviesContract.MovieEntry.IS_TOP_RATED)
                        .build();

                inserted =MoviesDBUtility.addInDB(uri, response.body(), context);
                Log.e(TAG, "top rated inserted: " + inserted);
                TYPE = NetworkUtilities.POPULAR;
                break;

            default:
                Log.e(TAG, "RECEIVED WRONG VALUE");
                throw new UnsupportedOperationException("wrong type requests");
        }

    }

    @Override
    public void onFailure(Call<Movies> call, Throwable t) {

        if (!call.isCanceled())
            call.cancel();

        Log.e(TAG, "FAILING ON DOWNLOAD DATA", t);
    }
}
