package com.dapl.halaladvisor.Model;

public class UserDataModel {
    private String u_id;

    private String profile_image_url;

    private String upload_path;

    private String profileimage;

    private String username;

    private String useremail;

    public String getU_id ()
    {
        return u_id;
    }

    public void setU_id (String u_id)
    {
        this.u_id = u_id;
    }

    public String getProfile_image_url ()
    {
        return profile_image_url;
    }

    public void setProfile_image_url (String profile_image_url)
    {
        this.profile_image_url = profile_image_url;
    }

    public String getUpload_path ()
    {
        return upload_path;
    }

    public void setUpload_path (String upload_path)
    {
        this.upload_path = upload_path;
    }

    public String getProfileimage ()
    {
        return profileimage;
    }

    public void setProfileimage (String profileimage)
    {
        this.profileimage = profileimage;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getUseremail ()
    {
        return useremail;
    }

    public void setUseremail (String useremail)
    {
        this.useremail = useremail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [u_id = "+u_id+", profile_image_url = "+profile_image_url+", upload_path = "+upload_path+", profileimage = "+profileimage+", username = "+username+", useremail = "+useremail+"]";
    }
}
