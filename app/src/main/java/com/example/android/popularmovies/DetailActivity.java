package com.example.android.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MoviesContract;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this, this);

        Intent intentThatStartedThis = getIntent();

        if(intentThatStartedThis != null){
            if(intentThatStartedThis.hasExtra(Intent.EXTRA_TEXT)){

//                int mMovie_ID = Integer.parseInt(intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT));
                //todo sistemare qui che c' ètanto da fa fare
//                String mMovieInfos = intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT);
//
//                try {
//                    movieId = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.MOVIE_ID);
//                    title = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.ORIGINAL_TITLE);
//                    overview = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.OVERVIEW);
//                    releaseDate = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.RELEASE_DATE);
//                    userRating = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.USER_RATING);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    title = "NO TITLE";
//                    overview = "NO OVERVIEW";
//                    releaseDate = "NO DATA";
//                    userRating = "0";
//                }
//
//                Log.d(TAG, "title: " + title);
//                Log.d(TAG, "overview: " + overview);
//
//
//                String url;
//
//                try {
//                    String image = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.POSTER);
//                    url = NetworkUtilities.imageURLBuilder(image, NetworkUtilities.IMAGE_LARGE);
//                    Log.d(TAG, "RECEIVED URL: " + url);

//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }

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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case ID_MOVIE_DETAILS_LOADER:
                Uri uri = MoviesContract.MovieEntry.CONTENT_URI.buildUpon()
                        .appendPath(MoviesContract.MovieEntry.MOVIE_ID)
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
//        String movieId = data.getString(INDEX_MOVIE_ID);
        String urlImage = data.getString(INDEX_POSTER);

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
}
