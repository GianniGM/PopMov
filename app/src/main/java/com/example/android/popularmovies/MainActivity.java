package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JsonDataParser;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.tv_try_work);

        new FetchMovieTask().execute(NetworkUtilities.POPULAR);
    }

    public class FetchMovieTask extends AsyncTask <String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            URL movies = NetworkUtilities.BuildUrl(params[0]);

            Log.d("url: ", movies.toString());

            try{
                String jsonResponse = NetworkUtilities.getResponseFromHttp(movies);

                //TODO remove me after added intents
                Log.d("FILM_INFO", JsonDataParser.getMovieInfo(jsonResponse, 3, JsonDataParser.OVERVIEW));

                return JsonDataParser.getPosters(jsonResponse);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {



            if(strings != null){
                for(String s : strings) {
                    mTextView.append(s + "\n");
                }
            }else{
                showErrorMessage();
            }

        }
    }

    private void showErrorMessage() {
        mTextView.setText("!!!ERRORE!!!");
    }

}
