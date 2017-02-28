package com.example.android.popularmovies.sync;

import android.content.Context;

/**
 * Created by giannig on 2/28/17.
 */

public class PopularMoviesSyncTask {

    //TODO CANCELLARE STA ROBA DESCRITIVA O PERLOMENO MODIFICARLA
    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncMovies(Context context) {
        //INSERIRE QUI LA ROBA NEL LOADER

        //NON LANCIARE COMUNQUE IL SERVIZIO, PER QUEL CHE FACCIAMO NOI NON HA SENSO

        //CREARE UN SERVIZIO CHE OGNI TOT (MOLTO SALTUARIAMENTE) CERCA AGGIORNAMENTI SUI FILM

        //QUESTO VUOL DIRE CHE VA SCHEDULATO CON UN JOBSERVICE

        //NOTIFICA ALL'AGGIORNAMENT??? QUESTO CI PENSIAMO COME ULTIMA COSA

        //IL LOADER SUL MAIN CERCA ROBA SUL DATABASE
    }
}
