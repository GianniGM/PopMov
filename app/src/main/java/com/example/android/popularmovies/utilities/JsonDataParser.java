package com.example.android.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by giannig on 19/01/17.
 * Utility for parsing json received data
 */

public class JsonDataParser {

    public final static String TITLE = "title";
    public final static String ORIGINAL_TITLE = "original_title";
    public final static String POSTER = "poster_path";
    public final static String OVERVIEW = "overview";
    public final static String RELEASE_DATE = "release_date";
    public static final String USER_RATING = "vote_average";


    private final static String RESULTS = "results";
    private final static String SUCCESS = "success";


    /** get interested value from JSONObject
     *
     * @param dataString Row Received jsonString
     * @param key of value i wanna search
     * @return value of key
     * @throws JSONException
     */
    public static String getMovieInfo(String dataString, String key) throws JSONException {

        JSONObject movieJSON = new JSONObject(dataString);

        if(movieJSON.has(SUCCESS)){
            if(!movieJSON.getBoolean(SUCCESS)){
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
    public static String[] getPosters(String dataString) throws JSONException {

        JSONArray results = getResults(dataString);

        if(results == null || results.length() == 0){
            return null;
        }

        String[] posters = new String[results.length()];

        for (int i = 0; i < results.length(); i++) {
            posters[i] = results.getJSONObject(i).getString(POSTER);
        }

        return posters;
    }

    /**Returns movie results from JSONString
     *
     * @param movieStringJson received JSONString
     * @return JSONArray with results
     * @throws JSONException
     */
    private static JSONArray getResults(String movieStringJson) throws JSONException {
        JSONObject movieJSON = new JSONObject(movieStringJson);

        if(movieJSON.has(SUCCESS)){
            if(!movieJSON.getBoolean(SUCCESS)){
                return null;
            }
        }

        JSONArray results = null;

        if(movieJSON.has(RESULTS)) {
            results = movieJSON.getJSONArray(RESULTS);
        }

        return results;
    }

    public static String getMovieInfo(String movieStringJson, int index) throws JSONException {
        JSONArray array = getResults(movieStringJson);

        if( array == null)
            return "";

        return array.getJSONObject(index).toString();
    }
}
