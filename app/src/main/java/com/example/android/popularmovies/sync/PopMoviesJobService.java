package com.example.android.popularmovies.sync;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by giannig on 3/3/17.
 */

public class PopMoviesJobService extends JobService{


    private static final String TAG = PopMoviesIntentService.class.getSimpleName();
    MovieSync movieSync = null;

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "job started");


        movieSync = MovieSync.startSynchronizing(this);
        jobFinished(job, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "job stopped");

        if(movieSync != null)
            MovieSync.stopSynchronizing(movieSync);
        return true;
    }
}
