
package com.dapl.halaladvisor.Model.resdetailsres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenHour {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("res_id")
    @Expose
    private String resId;
    @SerializedName("sun_opening_time")
    @Expose
    private String sunOpeningTime;
    @SerializedName("sun_closeing_time")
    @Expose
    private String sunCloseingTime;
    @SerializedName("mdonday_opening_time")
    @Expose
    private String mdondayOpeningTime;
    @SerializedName("mdonday_closeing_time")
    @Expose
    private String mdondayCloseingTime;
    @SerializedName("tuesday_opening_time")
    @Expose
    private String tuesdayOpeningTime;
    @SerializedName("tuesday_closeing_time")
    @Expose
    private String tuesdayCloseingTime;
    @SerializedName("wednesday_opening_time")
    @Expose
    private String wednesdayOpeningTime;
    @SerializedName("wednesday_closeing_time")
    @Expose
    private String wednesdayCloseingTime;
    @SerializedName("thursday_opening_time")
    @Expose
    private String thursdayOpeningTime;
    @SerializedName("thursday_closeing_time")
    @Expose
    private String thursdayCloseingTime;
    @SerializedName("friday_opening_time")
    @Expose
    private String fridayOpeningTime;
    @SerializedName("friday_closeing_time")
    @Expose
    private String fridayCloseingTime;
    @SerializedName("saturday_opening_time")
    @Expose
    private String saturdayOpeningTime;
    @SerializedName("saturday_closeing_time")
    @Expose
    private String saturdayCloseingTime;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getSunOpeningTime() {
        return sunOpeningTime;
    }

    public void setSunOpeningTime(String sunOpeningTime) {
        this.sunOpeningTime = sunOpeningTime;
    }

    public String getSunCloseingTime() {
        return sunCloseingTime;
    }

    public void setSunCloseingTime(String sunCloseingTime) {
        this.sunCloseingTime = sunCloseingTime;
    }

    public String getMdondayOpeningTime() {
        return mdondayOpeningTime;
    }

    public void setMdondayOpeningTime(String mdondayOpeningTime) {
        this.mdondayOpeningTime = mdondayOpeningTime;
    }

    public String getMdondayCloseingTime() {
        return mdondayCloseingTime;
    }

    public void setMdondayCloseingTime(String mdondayCloseingTime) {
        this.mdondayCloseingTime = mdondayCloseingTime;
    }

    public String getTuesdayOpeningTime() {
        return tuesdayOpeningTime;
    }

    public void setTuesdayOpeningTime(String tuesdayOpeningTime) {
        this.tuesdayOpeningTime = tuesdayOpeningTime;
    }

    public String getTuesdayCloseingTime() {
        return tuesdayCloseingTime;
    }

    public void setTuesdayCloseingTime(String tuesdayCloseingTime) {
        this.tuesdayCloseingTime = tuesdayCloseingTime;
    }

    public String getWednesdayOpeningTime() {
        return wednesdayOpeningTime;
    }

    public void setWednesdayOpeningTime(String wednesdayOpeningTime) {
        this.wednesdayOpeningTime = wednesdayOpeningTime;
    }

    public String getWednesdayCloseingTime() {
        return wednesdayCloseingTime;
    }

    public void setWednesdayCloseingTime(String wednesdayCloseingTime) {
        this.wednesdayCloseingTime = wednesdayCloseingTime;
    }

    public String getThursdayOpeningTime() {
        return thursdayOpeningTime;
    }

    public void setThursdayOpeningTime(String thursdayOpeningTime) {
        this.thursdayOpeningTime = thursdayOpeningTime;
    }

    public String getThursdayCloseingTime() {
        return thursdayCloseingTime;
    }

    public void setThursdayCloseingTime(String thursdayCloseingTime) {
        this.thursdayCloseingTime = thursdayCloseingTime;
    }

    public String getFridayOpeningTime() {
        return fridayOpeningTime;
    }

    public void setFridayOpeningTime(String fridayOpeningTime) {
        this.fridayOpeningTime = fridayOpeningTime;
    }

    public String getFridayCloseingTime() {
        return fridayCloseingTime;
    }

    public void setFridayCloseingTime(String fridayCloseingTime) {
        this.fridayCloseingTime = fridayCloseingTime;
    }

    public String getSaturdayOpeningTime() {
        return saturdayOpeningTime;
    }

    public void setSaturdayOpeningTime(String saturdayOpeningTime) {
        this.saturdayOpeningTime = saturdayOpeningTime;
    }

    public String getSaturdayCloseingTime() {
        return saturdayCloseingTime;
    }

    public void setSaturdayCloseingTime(String saturdayCloseingTime) {
        this.saturdayCloseingTime = saturdayCloseingTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
