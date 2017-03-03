package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.sync.PopMoviesSyncUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.data.MoviesDBUtility.NAME_PATH_BEST;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ID_MOVIE_LOADER = 8522;

    private static String status = MoviesContract.MovieEntry.IS_MOST_POPULAR;

    private MovieAdapter mMovieAdapter;

    private int mPosition = RecyclerView.NO_POSITION;

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

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(ID_MOVIE_LOADER);

        if(loader == null){
            loaderManager.initLoader(ID_MOVIE_LOADER, null, this);
        }else{
            loaderManager.restartLoader(ID_MOVIE_LOADER, null, this);
        }

        PopMoviesSyncUtils.initialize(this);

        mLoadingData.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);

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
    public void onClick(String movieID) {

        Context ctx = this;
        Class destClass = DetailActivity.class;

        Intent intentToStartActivity = new Intent(ctx, destClass);
        intentToStartActivity.putExtra(Intent.EXTRA_TEXT, movieID);
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
            status = MoviesContract.MovieEntry.IS_TOP_RATED;
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
            mLoadingData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mErrorMessageTextView.setVisibility(View.GONE);

        }

        if(id == R.id.action_popular){
            status = MoviesContract.MovieEntry.IS_MOST_POPULAR;
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, null, this);
            mLoadingData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mErrorMessageTextView.setVisibility(View.GONE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case ID_MOVIE_LOADER:
                Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(NAME_PATH_BEST)
                        .appendPath(status)
                        .build();

                Log.d(TAG, uri.toString());

                String[] projection = new String[]{
                        MoviesContract.MovieEntry.POSTER,
                        MoviesContract.MovieEntry.MOVIE_ID,
                        MoviesContract.MovieEntry.OVERVIEW
                };

                String sortOrder = MoviesContract.MovieEntry._ID;
                CursorLoader loader = new CursorLoader(this,
                        uri,
                        projection,
                        null,
                        null,
                        sortOrder
                );

                return loader;

            default:
                throw new RuntimeException("Loader Not Implemented" + id);
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.d(TAG, "loader finished");

        mMovieAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);

        mLoadingData.setVisibility(View.GONE);
        if (data.getCount() != 0){
            showData();
        }else {
            Log.e("ERROR", "data field is empty");
            showErrorMessage();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

}
