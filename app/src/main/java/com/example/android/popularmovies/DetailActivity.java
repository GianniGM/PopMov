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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

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
                String mMovieInfos = intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT);

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

                String url;
                try {
                    String image = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.POSTER);
                    url = NetworkUtilities.imageURLBuilder(image, NetworkUtilities.IMAGE_LARGE);
                    Log.d(TAG, "RECEIVED URL: " + url);
                    Picasso.with(this)
                            .load(url)
                            .error(R.drawable.placeholder_error)
                            .into(mImageViewPoster);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
