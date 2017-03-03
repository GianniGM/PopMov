package com.example.android.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies.data.MoviesContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by giannig on 3/3/17.
 */

public class PopMoviesSyncUtils {
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String POPMOV_SYNC_TAG = "popMov-sync";

    /**
     * Dispatcher schedule Intent Service every 10 hours, i think it could be the right choise cause we
     * are tallking about films not Forecasts
     * @param ctx Context
     */
    static void scheduleFirebaseJobDispatcher(@NonNull final Context ctx){

        Driver driver = new GooglePlayDriver(ctx);
        FirebaseJobDispatcher firebaseJobDispatcher =  new FirebaseJobDispatcher(driver);

        Job syncMoviesJob = firebaseJobDispatcher.newJobBuilder()
                .setService(PopMoviesJobService.class)
                .setTag(POPMOV_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(
                        Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS
                    )
                )
                .setReplaceCurrent(true)
                .build();

        firebaseJobDispatcher.schedule(syncMoviesJob);
    }

    /**
     * Initialize JobService and check if database is empty or not
     * if empty start now Service that load data
     * else schedule normally
     * @param ctx context
     */
    synchronized public static void initialize(final Context ctx){
        if(sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcher(ctx);

        /*
         * launch a thread that query data from db
         * if empty starts Service
         */
        new Thread(new Runnable() {
            @Override
            public void run() {

                /* URI for every row of weather data in our weather table*/
                Uri forecastQueryUri = MoviesContract.MovieEntry.CONTENT_URI;

                /*
                 * Since this query is going to be used only as a check to see if we have any
                 * data (rather than to display data), we just need to PROJECT the ID of each
                 * row. In our queries where we display data, we need to PROJECT more columns
                 * to determine what weather details need to be displayed.
                 */
                String[] projectionColumns = {MoviesContract.MovieEntry._ID};
                String selectionStatement = MoviesContract.MovieEntry.IS_MOST_POPULAR + " >= 1";

                /* Here, we perform the query to check to see if we have any weather data */
                Cursor cursor = ctx.getContentResolver().query(
                        forecastQueryUri,
                        projectionColumns,
                        selectionStatement,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(ctx);
                }

                /* Make sure to close the Cursor to avoid memory leaks! */
                cursor.close();
            }
        }).start();

    }

    /**
     * Launch Intent if no data exists in db
     * @param ctx context
     */
    public static void startImmediateSync(@NonNull final Context ctx){
        Intent intentToSync = new Intent(ctx, PopMoviesIntentService.class);
        ctx.startService(intentToSync);
    }
}
