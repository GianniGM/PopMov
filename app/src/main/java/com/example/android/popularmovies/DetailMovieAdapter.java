package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.movies.TrailersResults;

import java.util.ArrayList;

import static com.example.android.popularmovies.data.MoviesDBUtility.INDEX_MOVIE_ID;

/**
 * Created by giannig on 3/4/17.
 */

public class DetailMovieAdapter extends RecyclerView.Adapter<DetailMovieAdapter.DetailMovieAdapterViewHolder> {

    private final InterfaceOnClickHandler mClickHandler;
    private Context ctx;
    private TrailersResults.Trailer[] mData;


    public interface InterfaceOnClickHandler{
        void onClick(TrailersResults.Trailer trailer);
    }

    public DetailMovieAdapter(InterfaceOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }


    public void setData(TrailersResults.Trailer[] trailers){
        mData = trailers;
        notifyDataSetChanged();
    }

    @Override
    public DetailMovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ctx = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.trailer_item, parent, false);

        return new DetailMovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailMovieAdapterViewHolder holder, int position) {
        TrailersResults.Trailer trailer = mData[position];

        holder.mTrailerTextView.setText(trailer.getMovieName());

        Log.d("ON_BIND_VIEWHOLDER ", trailer.getMovieName());
    }

    @Override
    public int getItemCount() {
        if(mData == null) return 0;
        return mData.length;
    }

    class DetailMovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTrailerTextView;

        public DetailMovieAdapterViewHolder(View v){
            super(v);

            mTrailerTextView = (TextView) v.findViewById(R.id.tv_trailer_item);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //lanciare l'intent esplicito
            Log.d("CLICK", "click");

            TrailersResults.Trailer trailer = mData[getAdapterPosition()];
            mClickHandler.onClick(trailer);
        }
    }
}
