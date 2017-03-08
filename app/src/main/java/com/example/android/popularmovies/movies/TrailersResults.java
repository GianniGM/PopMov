package com.example.android.popularmovies.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannig on 3/4/17.
 */
public class TrailersResults implements Results {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private Trailer[] trailers;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Object getData() {
        return trailers;
    }

    @Override
    public void setData(Object[] data) {
        this.trailers = (Trailer[]) data;
    }

    public class Trailer{

        @SerializedName("id")
        @Expose
        private String TrailerID;

        @SerializedName("key")
        @Expose
        private String key;

        @SerializedName("name")
        @Expose
        private String movieName;

        @SerializedName("site")
        @Expose
        private String siteName;

        @SerializedName("size")
        @Expose
        private int size;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getTrailerID() {
            return TrailerID;
        }

        public void setTrailerID(String trailerID) {
            TrailerID = trailerID;
        }
    }
}
