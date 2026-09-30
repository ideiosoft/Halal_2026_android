package com.dapl.halaladvisor.Model;

public class RestaurentReviewObject {
    private String u_id;

    private String date_of_post;

    private String review;

    private String res_id;

    private String rating;

    private String rru_image;

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

    public String getReview ()
    {
        return review;
    }

    public void setReview (String review)
    {
        this.review = review;
    }

    public String getRes_id ()
    {
        return res_id;
    }

    public void setRes_id (String res_id)
    {
        this.res_id = res_id;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getRru_image ()
    {
        return rru_image;
    }

    public void setRru_image (String rru_image)
    {
        this.rru_image = rru_image;
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
        return "ClassPojo [u_id = "+u_id+", date_of_post = "+date_of_post+", review = "+review+", res_id = "+res_id+", rating = "+rating+", rru_image = "+rru_image+", username = "+username+", timestamp = "+timestamp+"]";
    }
}
