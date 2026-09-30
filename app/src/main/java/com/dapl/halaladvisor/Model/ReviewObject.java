package com.dapl.halaladvisor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewObject {
    private String u_id;
    public ArrayList<String> getReview_images() {
        return review_images;
    }

    public void setReview_images(ArrayList<String> review_images) {
        this.review_images = review_images;
    }

    @SerializedName("review_images")
    @Expose
    private ArrayList<String> review_images;

    private String date_of_post;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String baseUrl;

    private String rating_id;

    private String userimage;

    private String review;

    private String rating;

    private String username;

    private String timestamp;

    public String getU_id ()
    {
        return u_id;
    }

    public void setU_id (String u_id)
    {
        this.u_id = u_id;
    }

    public String getDate_of_post ()
    {
        return date_of_post;
    }

    public void setDate_of_post (String date_of_post)
    {
        this.date_of_post = date_of_post;
    }

    public String getRating_id ()
    {
        return rating_id;
    }

    public void setRating_id (String rating_id)
    {
        this.rating_id = rating_id;
    }

    public String getUserimage ()
    {
        return userimage;
    }

    public void setUserimage (String userimage)
    {
        this.userimage = userimage;
    }

    public String getReview ()
    {
        return review;
    }

    public void setReview (String review)
    {
        this.review = review;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getTimestamp ()
    {
        return timestamp;
    }

    public void setTimestamp (String timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [u_id = "+u_id+", date_of_post = "+date_of_post+", rating_id = "+rating_id+", userimage = "+userimage+", review = "+review+", rating = "+rating+", username = "+username+", timestamp = "+timestamp+"]";
    }
}
