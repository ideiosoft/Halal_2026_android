
package com.dapl.halaladvisor.Model.socialloginres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class SocialLoginRes {

    @SerializedName("message")
    @Expose
    private String message;
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
