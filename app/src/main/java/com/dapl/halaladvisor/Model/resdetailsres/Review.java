
package com.dapl.halaladvisor.Model.resdetailsres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Review {

    @SerializedName("u_id")
    @Expose
    private String uId;

    public ArrayList<String> getReview_images() {
        return review_images;
    }

    public void setReview_images(ArrayList<String> review_images) {
        this.review_images = review_images;
    }

    @SerializedName("review_images")
    @Expose
    private ArrayList<String> review_images;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("res_id")
    @Expose
    private String resId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("date_of_post")
    @Expose
    private String dateOfPost;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("rru_image")
    @Expose
    private String rruImage;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(String dateOfPost) {
        this.dateOfPost = dateOfPost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRruImage() {
        return rruImage;
    }

    public void setRruImage(String rruImage) {
        this.rruImage = rruImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
