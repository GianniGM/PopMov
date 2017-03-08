package com.example.android.popularmovies.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by giannig on 3/8/17.
 */

public class ReviewsResults implements Results {


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private ReviewsResults.Review[] reviews;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Review[] getData() {
        return reviews;
    }

    public void setData(Object[] reviews) {
        this.reviews = (Review[]) reviews;
    }

    public class Review{

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("author")
        @Expose
        private String author;

        @SerializedName("content")
        @Expose
        private String content;

        @SerializedName("media_id")
        @Expose
        private String media_id;

        @SerializedName("media_title")
        @Expose
        private String media_title;

        @SerializedName("media_type")
        @Expose
        private String media_type;

        @SerializedName("url")
        @Expose
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getMedia_title() {
            return media_title;
        }

        public void setMedia_title(String media_title) {
            this.media_title = media_title;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
