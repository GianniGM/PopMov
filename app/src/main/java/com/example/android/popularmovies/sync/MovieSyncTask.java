package com.example.android.popularmovies.sync;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.data.MoviesDBUtility;
import com.example.android.popularmovies.utilities.Movies;
import com.example.android.popularmovies.utilities.MoviesInterface;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giannig on 3/1/17.
 */

public class MovieSyncTask implements Callback<Movies>{

    private static final String TAG = MovieSyncTask.class.getSimpleName();
    private MoviesInterface instance;
    private Call<Movies> retrofitCall;
    private Context context;

    private static String TYPE = NetworkUtilities.POPULAR;

    synchronized public static MovieSyncTask startSynchronizing(Context ctx){
        MovieSyncTask m = new MovieSyncTask();
        m.Start(ctx);
        return m;
    }

    synchronized public static void stopSynchronizing(MovieSyncTask m){
        m.Stop();
    }

    private void Start(Context ctx){
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

        switch (TYPE){
            case NetworkUtilities.POPULAR:
                MoviesDBUtility.addMostPopularInDB(response.body(), context);
                TYPE = NetworkUtilities.TOP_RATED;
                loadMovieData(NetworkUtilities.TOP_RATED);
                break;

            case NetworkUtilities.TOP_RATED:
                MoviesDBUtility.addTopRatedInDB(response.body(), context);
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
