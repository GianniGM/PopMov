package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.utilities.JsonDataParser;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapter mMovieAdapter;
    private String jsonResponse;
    private static String status = NetworkUtilities.POPULAR;

    @BindView(R.id.recyclerview_posters) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_data) ProgressBar mLoadingData;
    @BindView(R.id.tv_error_msg) TextView mErrorMessageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, calculateColumns(this));
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieTask().execute(status);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showData(){
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public static int calculateColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int nColumns = (int) (dpWidth / 180);

        Log.d("MAIN_ACTIVITY", String.valueOf(nColumns ));

        return nColumns;
    }

    @Override
    public void onClick(int movieDetails) {

        String s;
        try {
            s = JsonDataParser.getMovieInfo(jsonResponse, movieDetails);
        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(this, "Error on receiving data", Toast.LENGTH_SHORT).show();
            return;
        }

        Context ctx = this;
         Class destClass = DetailActivity.class;

        Intent intentToStartActivity = new Intent(ctx, destClass);
        intentToStartActivity.putExtra(Intent.EXTRA_TEXT, s);

        startActivity(intentToStartActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_top_rated){
            status = NetworkUtilities.TOP_RATED;
            mMovieAdapter.setMovieAdapterData(null);
            new FetchMovieTask().execute(status);
            showData();

        }

        if(id == R.id.action_popular){
            status = NetworkUtilities.POPULAR;
            mMovieAdapter.setMovieAdapterData(null);
            new FetchMovieTask().execute(status);
            showData();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        TODO
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        TODO

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        TODO

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
                jsonResponse = NetworkUtilities.getResponseFromHttp(movies);

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
