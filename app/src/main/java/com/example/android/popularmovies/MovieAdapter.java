package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.movies.TrailersResults;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.data.MoviesDBUtility.INDEX_MOVIE_ID;
import static com.example.android.popularmovies.data.MoviesDBUtility.INDEX_POSTER;

/**
 * Created by giannig on 20/01/17.
 * this is the adapter that handle RecyclerView
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private final static String TAG = MovieAdapter.class.getSimpleName();

    private final MovieAdapterOnClickHandler mClickHandler;

    private Context ctx;
    private Cursor mCursor;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(String movieDetails);
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);

        int layoutPosterItem = R.layout.poster_item;

        View v = inflater.inflate(layoutPosterItem,parent,false);

        return new MovieAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        //TODO index poster
        String posterURL=mCursor.getString(INDEX_POSTER);

        Picasso.with(ctx).load(posterURL).into(holder.mPosterView);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
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
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String movieID = mCursor.getString(INDEX_MOVIE_ID);
            mClickHandler.onClick(movieID);
        }

    }
}
