
package com.dapl.halaladvisor.Model.profileupdateres;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Userdetails {

    @SerializedName("u_id")
    @Expose
    private String uId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("useremail")
    @Expose
    private String useremail;
    @SerializedName("usermobile")
    @Expose
    private String usermobile;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

}
