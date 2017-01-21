package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JsonDataParser;
import com.example.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private TextView mTitleTextView;
    private TextView mOverviewMovie;

    private ImageView mImageViewPoster;

    private TextView mUserRating;
    private TextView mReleaseDate;

    private String mMovieInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_title_textview);
        mOverviewMovie = (TextView) findViewById(R.id.tv_overview);
        mUserRating = (TextView) findViewById(R.id.tv_user_ratings);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);

        mImageViewPoster = (ImageView) findViewById(R.id.iv_image_detail);

        Intent intentThatStartedThis = getIntent();


        if(intentThatStartedThis != null){
            if(intentThatStartedThis.hasExtra(Intent.EXTRA_TEXT)){
                mMovieInfos = intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT);

                String title;
                String overview;
                String releaseDate;
                String userRating;
                try {
                    title = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.ORIGINAL_TITLE);
                    overview = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.OVERVIEW);
                    releaseDate = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.RELEASE_DATE);
                    userRating = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.USER_RATING);
                } catch (JSONException e) {
                    e.printStackTrace();
                    title = "NO TITLE";
                    overview = "NO OVERVIEW";
                    releaseDate = "NO DATA";
                    userRating = "0";
                }

                Log.d(TAG, "title: " + title);
                Log.d(TAG, "overview: " + overview);

                mTitleTextView.setText(title);
                mOverviewMovie.setText(overview);
                mUserRating.append(" " + userRating);
                mReleaseDate.append(" " + releaseDate);

                //TODO picasso image
                String url;

                try {
                    String image = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.POSTER);
                    url = NetworkUtilities.imageURLBuilder(image);
                    Log.d(TAG, "URL BUILDED: " + url);
                    Picasso.with(this)
                            .load(url)
                            .into(mImageViewPoster);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //TODO ELIMIANRE HARDCODING
            }
        }

    }
}
