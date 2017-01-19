package com.example.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by giannig on 19/01/17.
 */

public class NetworkUtilities {

    public final static String POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";

    private final static String TAG = NetworkUtilities.class.getSimpleName();
    private final static String api_key = "[INSERT APY KEY HERE]";

    final static String QUERY_APY_KEY = "api_key";
    final static String TYPE = "movie";

    //TODO sistemare questo
    private final static String BASE_URL = "https://api.themoviedb.org/3/";


    public static URL BuildUrl(String request){
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendEncodedPath(TYPE)
                .appendEncodedPath(request)
                .appendQueryParameter(QUERY_APY_KEY, api_key)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "URI: " + url);

        return url;
    }

    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream input = connection.getInputStream();

            Scanner scanner = new Scanner(input);
            scanner.useDelimiter("//A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        }finally {
            connection.disconnect();
        }

    }
}
