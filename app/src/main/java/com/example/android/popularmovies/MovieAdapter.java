package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by giannig on 20/01/17.
 * this is the adapter that handle RecyclerView
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final static String TAG = MovieAdapter.class.getSimpleName();

    private String[] mPostersData;

    private final MovieAdapterOnClickHandler mClickHandler;

    private Context ctx;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public void setMovieAdapterData(String[] postersData){
        mPostersData = postersData;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(String movieDetails);
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create element view

        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);

        int layoutPosterItem = R.layout.poster_item;

        View v = inflater.inflate(layoutPosterItem,parent,false);

        return new MovieAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String posterPath = mPostersData[position];

        for(String s : mPostersData){
            Log.d(TAG, s);
        }

        String posterURL = NetworkUtilities.imageURLBuilder(posterPath);
        Log.d(TAG, "LOADING IMAGE " + posterURL);

        Picasso.with(ctx).load(posterURL.toString()).into(holder.mPosterView);
    }

    @Override
    public int getItemCount() {
        if(mPostersData == null) return 0;
        return mPostersData.length;
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mPosterView;


        public MovieAdapterViewHolder(View v){
            super(v);
            mPosterView = (ImageView) v.findViewById(R.id.iv_view_poster);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO QUA DEVI PASSARE I DATI CHE TI SERVONO NON IL POSTER
            int adapterPosition = getAdapterPosition();

            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }
}
