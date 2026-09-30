
package com.dapl.halaladvisor.Model.profileupdateres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class ProfileUpdateRes {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userdetails")
    @Expose
    private Userdetails userdetails;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Userdetails getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(Userdetails userdetails) {
        this.userdetails = userdetails;
    }

}
