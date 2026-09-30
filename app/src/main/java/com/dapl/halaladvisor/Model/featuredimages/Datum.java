
package com.dapl.halaladvisor.Model.featuredimages;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Datum {

    @SerializedName("id")
    @Expose
    private String id;

     public String getRes_id() {
         return res_id;
     }

     public void setRes_id(String res_id) {
         this.res_id = res_id;
     }

     @SerializedName("res_id")
    @Expose
    private String res_id;
    @SerializedName("image")
    @Expose
    private String image;

     public String getRes_url() {
         return res_url;
     }

     public void setRes_url(String res_url) {
         this.res_url = res_url;
     }

     @SerializedName("res_url")
    @Expose
    private String res_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
