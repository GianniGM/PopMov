package com.example.android.popularmovies.utilities;

import android.nfc.Tag;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giannig on 19/01/17.
 */

public class JsonDataParser {



    public final static String TITLE = "title";
    public final static String ORIGINAL_TITLE = "original_title";
    public final static String POSTER = "poster_path";
    public final static String OVERVIEW = "overview";
    public final static String RELEASE_DATE = "release_date";

    private final static String RESULTS = "results";
    private final static String SUCCES = "succes";


    /** get interested value from JSONObject
     *
     * @param movieJsonString JsonString of received data of the selected movie
     * @param key i want in movieJSONString
     * @return value of key
     */
    public static String getMovieInfo(String movieJsonString, String key) throws JSONException {
        //TODO
        JSONObject movieJSON = new JSONObject(movieJsonString);

        if(movieJSON.has(SUCCES)){
            if(!movieJSON.getBoolean(SUCCES)){
                return null;
            }
        }

        if(movieJSON.has(key)){
            return movieJSON.getString(key);
        }

        return null;
    }

    /** returns an array with URL's of
     *
     * @param dataString row JSONString received from GET request
     * @return url's poster array
     */
    public static String[] getPosters(JSONObject dataString){
        //TODO

        return null;
    }

    /**Returns movie results from JSONString
     *
     * @param movieStringJson received JSONString
     * @return JSONArray with results
     * @throws JSONException
     */
    public static JSONArray getResults(String movieStringJson) throws JSONException {
        JSONObject movieJSON = new JSONObject(movieStringJson);

        if(movieJSON.has(SUCCES)){
            if(!movieJSON.getBoolean(SUCCES)){
                return null;
            }
        }

        JSONArray results = null;

        if(movieJSON.has(RESULTS)) {
            results = movieJSON.getJSONArray(RESULTS);
        }

        return results;
    }
}
