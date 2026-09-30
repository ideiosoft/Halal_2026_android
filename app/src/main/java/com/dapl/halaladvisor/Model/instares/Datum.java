
package com.dapl.halaladvisor.Model.instares;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("permalink")
    @Expose
    private String permalink;
    @SerializedName("caption")
    @Expose
    private String caption;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

}
