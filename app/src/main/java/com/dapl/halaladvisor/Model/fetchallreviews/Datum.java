
package com.dapl.halaladvisor.Model.fetchallreviews;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 import java.util.ArrayList;

public class Datum {

    @SerializedName("rating_id")
    @Expose
    private String ratingId;


     public String getProfileimage() {
         return profileimage;
     }

     public void setProfileimage(String profileimage) {
         this.profileimage = profileimage;
     }

     @SerializedName("profileimage")
    @Expose
    private String profileimage;



    @SerializedName("res_id")
    @Expose
    private String resId;
    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("date_of_post")
    @Expose
    private String dateOfPost;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("review_image_id")
    @Expose
    private String reviewImageId;
    @SerializedName("images")
    @Expose
    private ArrayList images;

     public String getUsername() {
         return username;
     }

     public void setUsername(String username) {
         this.username = username;
     }

     @SerializedName("username")
    @Expose
    private String username;



    @SerializedName("review_id")
    @Expose
    private String reviewId;

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

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

    public String getDateOfPost() {
        return dateOfPost;
    }

    public void setDateOfPost(String dateOfPost) {
        this.dateOfPost = dateOfPost;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReviewImageId() {
        return reviewImageId;
    }

    public void setReviewImageId(String reviewImageId) {
        this.reviewImageId = reviewImageId;
    }

    public ArrayList getImages() {
        return images;
    }

    public void setImages(ArrayList images) {
        this.images = images;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

}
