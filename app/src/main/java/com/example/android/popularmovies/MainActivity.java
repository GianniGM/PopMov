package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JsonDataParser;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {


    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageTextView;

    private ProgressBar mLoadingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView =(RecyclerView) findViewById(R.id.recyclerview_posters);

        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_msg);

        mLoadingData = (ProgressBar) findViewById(R.id.pb_loading_data);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        GridLayoutManager layoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.cols_number));
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        //TODO
        new FetchMovieTask().execute(NetworkUtilities.POPULAR);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showData(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String movieDetails) {

    }

    public class FetchMovieTask extends AsyncTask <String, Void, String[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingData.setVisibility(View.VISIBLE);
        }

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
//                Log.d("FILM_INFO", JsonDataParser.getMovieInfo(jsonResponse, 3, JsonDataParser.OVERVIEW));

                Log.d("FILM_INFO", jsonResponse);

                return JsonDataParser.getPosters(jsonResponse);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            mLoadingData.setVisibility(View.INVISIBLE);

            if(strings != null){
                mMovieAdapter.setMovieAdapterData(strings);
                mRecyclerView.setAdapter(mMovieAdapter);
                showData();
            }else{
                showErrorMessage();
            }

        }
    }
}
