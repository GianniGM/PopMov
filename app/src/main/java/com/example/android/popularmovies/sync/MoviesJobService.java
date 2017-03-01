package com.example.android.popularmovies.sync;

import android.util.Log;

import com.example.android.popularmovies.data.MoviesDBUtility;
import com.example.android.popularmovies.utilities.MoviesInterface;
import com.example.android.popularmovies.utilities.Movies;
import com.example.android.popularmovies.utilities.NetworkUtilities;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by giannig on 3/1/17.
 * in this jobService i use retrofit to call data from server and load it into database
 */

public class MoviesJobService extends JobService implements Callback<Movies>{

    private static final String TAG = MoviesJobService.class.getSimpleName();

    private MoviesInterface instance;
    private JobParameters jobParameters;
    private Call<Movies> retrofitCall;
    private static String TYPE = NetworkUtilities.POPULAR;

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
    public boolean onStartJob(JobParameters job) {
        instance = buildMovieInstance();
        jobParameters = job;

        loadMovieData(TYPE);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        if(retrofitCall == null)
            return true;

        if(!retrofitCall.isCanceled())
            retrofitCall.cancel();

        return true;
    }

    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        retrofitCall = call;

        Log.e(TAG, "looking for data: " + TYPE);

        switch (TYPE){
            case NetworkUtilities.POPULAR:
                MoviesDBUtility.addMostPopularInDB(response.body(), this);
                TYPE = NetworkUtilities.TOP_RATED;
                loadMovieData(NetworkUtilities.TOP_RATED);
                break;

            case NetworkUtilities.TOP_RATED:
                MoviesDBUtility.addTopRatedInDB(response.body(), this);
                TYPE = NetworkUtilities.POPULAR;
                jobFinished(jobParameters, false);
                break;

            default:
                Log.e(TAG, "RECEIVED WRONG VALUE");
                    throw new UnsupportedOperationException("wrong type requests");
        }

    }

    @Override
    public void onFailure(Call<Movies> call, Throwable t) {

        if(!call.isCanceled())
            call.cancel();

        Log.e(TAG, "FAILING ON DOWNLOAD DATA" , t);
    }

}
