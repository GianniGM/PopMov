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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDBUtility;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.data.MoviesDBUtility.*;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int ID_MOVIE_DETAILS_LOADER = 78945;

    @BindView(R.id.tv_title_textview) TextView mTitleTextView;
    @BindView(R.id.tv_overview) TextView mOverviewMovie;
    @BindView(R.id.tv_user_ratings) TextView mUserRating;
    @BindView(R.id.tv_release_date) TextView mReleaseDate;
    @BindView(R.id.iv_image_detail) ImageView mImageViewPoster;
    @BindView(R.id.movie_details) View mMovieDetails;
    @BindView(R.id.button) Button mButtonFavourite;

    private int mMovieID;

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
        mButtonFavourite.setClickable(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        mMovieDetails.setVisibility(View.INVISIBLE);

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

        mMovieDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
