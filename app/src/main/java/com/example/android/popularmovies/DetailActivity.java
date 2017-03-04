package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDBUtility;
import com.example.android.popularmovies.movies.MoviesInterface;
import com.example.android.popularmovies.movies.TrailersResults;
import com.example.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.android.popularmovies.data.MoviesDBUtility.*;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        Callback<TrailersResults>{

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int ID_MOVIE_DETAILS_LOADER = 78945;

    @BindView(R.id.tv_title_textview) TextView mTitleTextView;
    @BindView(R.id.tv_overview) TextView mOverviewMovie;
    @BindView(R.id.tv_user_ratings) TextView mUserRating;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.iv_image_detail) ImageView mImageViewPoster;
    @BindView(R.id.button) Button mButtonFavourite;

    @BindView(R.id.recycler_view_trailers) RecyclerView mReciclerViewTrailers;

    //TODO AGGIUNGERE UNA PROGRESS BAR

    private int mMovieID;
    MoviesInterface trailersInstance;
    private DetailMovieAdapter posterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this, this);

        Intent intentThatStartedThis = getIntent();

        if(intentThatStartedThis != null){
            if(intentThatStartedThis.hasExtra(Intent.EXTRA_TEXT)){

                 mMovieID = Integer.parseInt(intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT));
            }
        }

        //TODO
        //1. controllare che non sia tra i favoriti
        //2. se è tra i favoriti cambiare il testo del pulsante
        //3. altrimenti lasciare così com'è

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader loader = loaderManager.getLoader(ID_MOVIE_DETAILS_LOADER);

        if(loader == null){
            loaderManager.initLoader(ID_MOVIE_DETAILS_LOADER, null, this);
        }else{
            loaderManager.restartLoader(ID_MOVIE_DETAILS_LOADER, null, this);
        }

        retrofitStartSync();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReciclerViewTrailers.setLayoutManager(linearLayoutManager);
        mReciclerViewTrailers.setVisibility(View.VISIBLE);

        posterAdapter = new DetailMovieAdapter();
        mReciclerViewTrailers.setAdapter(posterAdapter);

    }

    public void retrofitStartSync(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtilities.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        trailersInstance = retrofit.create(MoviesInterface.class);

        trailersInstance.getTrailers(String.valueOf(mMovieID), NetworkUtilities.api_key).enqueue(this);
        Log.d(TAG, "MOVIE ID TRAILER LAUNCHED" + String.valueOf(mMovieID));

    }

    public void clickedMarkAsFavourite(View view) {
        //TODO do stuff here
        //1. se è tra i favoriti settare a zero nel database
        //2. altrimenti settare a uno
        //3. aggiornare il database
        // dovrebbe essere un update

        final Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                .build();
        final Context ctx = this;

        new AsyncTask<String, Void, Void>(){
            private final static String TAG = "ASYNC_TASK";
            private int updated;

            @Override
            protected Void doInBackground(String... params) {

                updated= MoviesDBUtility.updateDB(uri, ctx, params[0]);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "updated: "+ updated);
            }
        }.execute(String.valueOf(mMovieID));

        mButtonFavourite.setText(getString(R.string.button_favourite_label_setted));
        mButtonFavourite.setBackgroundColor(R.color.colorPrimary);
        mButtonFavourite.setClickable(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {



        switch (id){
            case ID_MOVIE_DETAILS_LOADER:
                Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(NAME_PATH_ID)
                        .appendPath(String.valueOf(mMovieID))
                        .build();

                Log.e(TAG, uri.toString());

                String sortOrder = MoviesContract.MovieEntry._ID;
                CursorLoader loader = new CursorLoader(this,
                        uri,
                        MOVIE_PROJECTION,
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

        if(!data.moveToNext()){
            Log.e(TAG, "NO DATA");
            return;
        }

        String title = data.getString(INDEX_ORIGINAL_TITLE);
        String overview = data.getString(INDEX_OVERVIEW);
        String releaseDate = data.getString(INDEX_RELEASE_DATE);
        String userRating = data.getString(INDEX_VOTE_AVERAGE);
        String urlImage = data.getString(INDEX_POSTER);

        if(data.getInt(INDEX_IS_FAVOURITE)>= MoviesDBUtility.IS_TRUE){
            mButtonFavourite.setClickable(false);
            mButtonFavourite.setBackgroundColor(R.color.colorPrimary);
            mButtonFavourite.setText(getString(R.string.button_favourite_label_setted));
        }

        mTitleTextView.setText(title);
        mOverviewMovie.setText(overview);
        mUserRating.setText(userRating);
        mReleaseDate.setText(releaseDate);

        Picasso.with(this)
            .load(urlImage)
            .error(R.drawable.placeholder_error)
            .into(mImageViewPoster);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResponse(Call<TrailersResults> call, Response<TrailersResults> response) {

        Log.e(TAG, "looking for data trailers");
        if(!response.isSuccessful()){
            Log.e(TAG, "Unable to connect");
            return;
        }

        TrailersResults data = response.body();

        posterAdapter.setData(data.getTrailers());
    }

    private void setUpRecyclerView(TrailersResults data) {
        TrailersResults.Trailer[] trailers = data.getTrailers();
        for(TrailersResults.Trailer t : trailers){
            Log.d(TAG, t.getMovieName()+ " " + t.getKey() + " " + t.getTrailerID());
        }
    }

    @Override
    public void onFailure(Call<TrailersResults> call, Throwable t) {
        if (!call.isCanceled())
            call.cancel();

        Log.e(TAG, "FAILING ON DOWNLOAD TRAILERS", t);
    }
}
