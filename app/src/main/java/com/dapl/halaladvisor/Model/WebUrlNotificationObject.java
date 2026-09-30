package com.dapl.halaladvisor.Model;

public class WebUrlNotificationObject {
    private String image;

    private String color;

    private String sound;

    private String icon;

    private String title;

    private String body;

    private String url;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ClassPojo [image = " + image + ", color = " + color + ", sound = " + sound + ", icon = " + icon + ", title = " + title + ", body = " + body + ", url = " + url + "]";
    }
}
