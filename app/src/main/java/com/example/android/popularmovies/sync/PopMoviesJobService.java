package com.example.android.popularmovies.sync;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by giannig on 3/3/17.
 */

public class PopMoviesJobService extends JobService{

    MovieSync movieSync = null;

    @Override
    public boolean onStartJob(JobParameters job) {

        movieSync = MovieSync.startSynchronizing(this);
        jobFinished(job, false);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(movieSync != null)
            MovieSync.stopSynchronizing(movieSync);
        return false;
    }
}
