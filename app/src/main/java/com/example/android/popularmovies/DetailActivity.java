package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.JsonDataParser;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private TextView mOverviewMovie;

    private ImageView mImageViewPoster;

    private String mMovieInfos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_title_textview);
        mOverviewMovie = (TextView) findViewById(R.id.tv_overview);

        mImageViewPoster = (ImageView) findViewById(R.id.iv_view_poster);

        Intent intentThatStartedThis = getIntent();

        if(intentThatStartedThis != null){
            if(intentThatStartedThis.hasExtra(Intent.EXTRA_TEXT)){
                mMovieInfos = intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT);

                String title;
                String overview;

                try {
                    title = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.TITLE);
                    overview = JsonDataParser.getMovieInfo(mMovieInfos, JsonDataParser.OVERVIEW);
                } catch (JSONException e) {
                    e.printStackTrace();
                    title = "NO TITLE";
                    overview = "NO OVERVIEW";
                }

                mTitleTextView.setText(title);
                mOverviewMovie.setText(overview);
                //TODO picasso image

                //TODO ELIMIANRE HARDCODING
            }
        }

    }
}
