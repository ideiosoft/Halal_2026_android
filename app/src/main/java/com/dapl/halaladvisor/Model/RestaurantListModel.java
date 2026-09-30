package com.dapl.halaladvisor.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DAT-Asset-128 on 22-07-2016.
 */
public class RestaurantListModel  {
    private String ostatus;

    String Cuisine_type;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Bitmap bitmap;

    public String getCuisine_type() {
        return Cuisine_type;
    }

    public void setCuisine_type(String cuisine_type) {
        Cuisine_type = cuisine_type;
    }

    public String getCusineJsnArray() {
        return cusineJsnArray;
    }

    public void setCusineJsnArray(String cusineJsnArray) {
        this.cusineJsnArray = cusineJsnArray;
    }

    private String cusineJsnArray;

    private String image;

    private String r_rating;

    public String getR_is_featured() {
        return r_is_featured;
    }

    public void setR_is_featured(String r_is_featured) {
        this.r_is_featured = r_is_featured;
    }

    private String r_is_featured;



    private String address;

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String offer_price;
    private String type;


    public String getIs_featured() {
        return is_featured;
    }

    public void setIs_featured(String is_featured) {
        this.is_featured = is_featured;
    }

    private String is_featured;

    public String getR_contact() {
        return r_contact;
    }

    public void setR_contact(String r_contact) {
        this.r_contact = r_contact;
    }

    private String r_contact;

    private String distance;

    private String delivery_tag;

    public String latitude;

    private String avg_cost;

    private String cost_sign;

    private String rating;

    private String pre_order;

    private String cusine_id;
    private String status_resturent;

    private String pick_up;

    private String cat_id;

    public String getStatus_resturent() {
        return status_resturent;
    }

    public void setStatus_resturent(String status_resturent) {
        this.status_resturent = status_resturent;
    }

    private String name;

    private String total_rating;

    private String order_now_link;

    private String id;

    public String longitude;

    private String status;

    public String getOstatus ()
    {
        return ostatus;
    }

    public void setOstatus (String ostatus)
    {
        this.ostatus = ostatus;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getR_rating ()
    {
        return r_rating;
    }

    public void setR_rating (String r_rating)
    {
        this.r_rating = r_rating;
    }


    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getDelivery_tag ()
    {
        return delivery_tag;
    }

    public void setDelivery_tag (String delivery_tag)
    {
        this.delivery_tag = delivery_tag;
    }

    public String getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (String latitude)
    {
        this.latitude = latitude;
    }

    public String getAvg_cost ()
    {
        return avg_cost;
    }

    public void setAvg_cost (String avg_cost)
    {
        this.avg_cost = avg_cost;
    }

    public String getCost_sign ()
    {
        return cost_sign;
    }

    public void setCost_sign (String cost_sign)
    {
        this.cost_sign = cost_sign;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getPre_order ()
    {
        return pre_order;
    }

    public void setPre_order (String pre_order)
    {
        this.pre_order = pre_order;
    }

    public String getCusine_id ()
    {
        return cusine_id;
    }

    public void setCusine_id (String cusine_id)
    {
        this.cusine_id = cusine_id;
    }

    public String getPick_up ()
    {
        return pick_up;
    }

    public void setPick_up (String pick_up)
    {
        this.pick_up = pick_up;
    }

    public String getCat_id ()
    {
        return cat_id;
    }

    public void setCat_id (String cat_id)
    {
        this.cat_id = cat_id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getTotal_rating ()
    {
        return total_rating;
    }

    public void setTotal_rating (String total_rating)
    {
        this.total_rating = total_rating;
    }

    public String getOrder_now_link ()
    {
        return order_now_link;
    }

    public void setOrder_now_link (String order_now_link)
    {
        this.order_now_link = order_now_link;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (String longitude)
    {
        this.longitude = longitude;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ostatus = "+ostatus+", image = "+image+", r_is_featured = "+r_is_featured+", r_rating = "+r_rating+", cuisines_name = "+", address = "+address+", distance = "+distance+", delivery_tag = "+delivery_tag+", latitude = "+latitude+", avg_cost = "+avg_cost+", cost_sign = "+cost_sign+", rating = "+rating+", pre_order = "+pre_order+", cusine_id = "+cusine_id+", pick_up = "+pick_up+", cat_id = "+cat_id+", name = "+name+", total_rating = "+total_rating+", order_now_link = "+order_now_link+", id = "+id+", longitude = "+longitude+", status = "+status+"]";
    }
}
