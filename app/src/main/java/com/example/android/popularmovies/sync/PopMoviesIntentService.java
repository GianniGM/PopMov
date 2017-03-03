package com.example.android.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by giannig on 2/28/17.
 */

public class PopMoviesIntentService extends IntentService {
    private final static String servName = PopMoviesIntentService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public PopMoviesIntentService(){
        super(servName);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MovieSync.startSynchronizing(this);
    }
}
