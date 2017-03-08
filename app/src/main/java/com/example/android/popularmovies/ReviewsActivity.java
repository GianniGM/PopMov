package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.movies.MoviesInterface;
import com.example.android.popularmovies.movies.ReviewsResults;
import com.example.android.popularmovies.utilities.NetworkUtilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewsActivity extends AppCompatActivity
            implements Callback<ReviewsResults>{

    private static final String TAG = ReviewsActivity.class.getSimpleName();
    private int mMovieID;
    private MoviesInterface reviewsInstance;
    private ReviewsAdapter reviewsAdapter;

    @BindView(R.id.rv_reviews_list) RecyclerView mReciclerViewReviews;
    @BindView(R.id.tv_reviews_error)TextView mErrorTextView;
    @BindView(R.id.pb_reviews_loading) ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        ButterKnife.bind(this, this);

        Intent intentThatStartedThis = getIntent();

        if(intentThatStartedThis != null){
            if(intentThatStartedThis.hasExtra(Intent.EXTRA_TEXT)){

                mMovieID = Integer.parseInt(intentThatStartedThis.getStringExtra(Intent.EXTRA_TEXT));
            }
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReciclerViewReviews.setLayoutManager(linearLayoutManager);
        mReciclerViewReviews.setVisibility(View.VISIBLE);

        reviewsAdapter = new ReviewsAdapter();
        mReciclerViewReviews.setAdapter(reviewsAdapter);

        retrofitStartSync();
        mLoading.setVisibility(View.VISIBLE);
    }

    public void retrofitStartSync(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtilities.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        reviewsInstance = retrofit.create(MoviesInterface.class);

        reviewsInstance.getReviews(String.valueOf(mMovieID), NetworkUtilities.api_key).enqueue(this);
        Log.d(TAG, "MOVIE ID REVIEWS LAUNCHED" + String.valueOf(mMovieID));

    }


    @Override
    public void onResponse(Call<ReviewsResults> call, Response<ReviewsResults> response) {
        Log.e(TAG, "looking for data trailers");
        if(!response.isSuccessful()){
            Log.e(TAG, "Unable to connect");
            return;
        }

        mLoading.setVisibility(View.GONE);

        ReviewsResults data = response.body();
        if(data.getData().length != 0) {
            reviewsAdapter.setData(data.getData());
            mErrorTextView.setVisibility(View.GONE);
        }else{
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(Call<ReviewsResults> call, Throwable t) {
        if (!call.isCanceled())
            call.cancel();

        Log.e(TAG, "FAILING ON DOWNLOAD TRAILERS", t);
    }
}

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewAdapterViewHolder> {

    private ReviewsResults.Review[] data;

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.reviews_list_item, parent, false);

        return new ReviewAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.mContentViewItem.setText(data[position].getContent());
        holder.mAuthorViewItem.setText(data[position].getAuthor());
    }

    @Override
    public int getItemCount() {
        if(data == null) return 0;
        return data.length;
    }

    public void setData(ReviewsResults.Review[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView mAuthorViewItem;
        final TextView mContentViewItem;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mAuthorViewItem = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContentViewItem = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }
}