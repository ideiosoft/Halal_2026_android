package com.dapl.halaladvisor.Model;

import com.dapl.halaladvisor.Model.resdetailsres.Review;

import java.util.ArrayList;

public class RestaurantDetailObject {


    private String ostatus;

    private String states_id;

    private String citys_id;

    private String avg_cost;

    private String avaragecost;

    private String estb_name;
    private String latitude;

    public String getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(String total_distance) {
        this.total_distance = total_distance;
    }

    private String total_distance;
    private String delivary_type;
    private String r_url;
    private String city_name;
    private String s_id;
    private String avaragecost_range;
    private String status_resturent;
    private String state_name;
    private String contact;
    private String cat_id;
    private String rest_avg_rating;
    private String cats_id;
    private String total_rating;
    private String order_now_link;
    private String id;
    private String estb_type;
    private String longitude;
    private String image;
    private String website;
    private String establishment_type;
    private String address;
    private String more_info_id;
    private String cusine_id;
    private String avaragecost_type;
    private String name;
    private String catagory_name;
    private String cash_and_card;
    private String time;
    private String city_id;

    public Integer getRes_fav() {
        return res_fav;
    }

    public void setRes_fav(Integer res_fav) {
        this.res_fav = res_fav;
    }

    private Integer res_fav;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ArrayList<String> getImagesArray() {
        return imagesArray;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }


    private String baseUrl;
    private String more_info_jsn_arr;
    private String photos_jsn_arr;
    private ArrayList<String> imagesArray;
    private ArrayList<Review> reviewArrayList;
    private String reviews_jsn_arr;
    private String open_hour_jsn_arr;
    private String cusine_jsn_arr;

    public String getAvaragecost() {
        return avaragecost;
    }

    public void setAvaragecost(String avaragecost) {
        this.avaragecost = avaragecost;
    }

    public String getEstb_name() {
        return estb_name;
    }

    public void setEstb_name(String estb_name) {
        this.estb_name = estb_name;
    }

    public String getMore_info_jsn_arr() {
        return more_info_jsn_arr;
    }

    public void setMore_info_jsn_arr(String more_info_jsn_arr) {
        this.more_info_jsn_arr = more_info_jsn_arr;
    }

    public String getPhotos_jsn_arr() {
        return photos_jsn_arr;
    }

    public void setPhotos_jsn_arr(String photos_jsn_arr) {
        this.photos_jsn_arr = photos_jsn_arr;
    }

    public  void setImagesArray (ArrayList<String> imagesArray){
        this.imagesArray = imagesArray;
    }
    public ArrayList<String > getResImages(){
        return imagesArray;
    }


    public  void setReviewArrayList (ArrayList<Review> reviewArrayList){
        this.reviewArrayList = reviewArrayList;
    }
    public ArrayList<Review> getReviewsList(){
        return reviewArrayList;
    }
    public String getReviews_jsn_arr() {
        return reviews_jsn_arr;
    }

    public void setReviews_jsn_arr(String reviews_jsn_arr) {
        this.reviews_jsn_arr = reviews_jsn_arr;
    }

    public String getOpen_hour_jsn_arr() {
        return open_hour_jsn_arr;
    }

    public void setOpen_hour_jsn_arr(String open_hour_jsn_arr) {
        this.open_hour_jsn_arr = open_hour_jsn_arr;
    }

    public String getCusine_jsn_arr() {
        return cusine_jsn_arr;
    }

    public void setCusine_jsn_arr(String cusine_jsn_arr) {
        this.cusine_jsn_arr = cusine_jsn_arr;
    }

    public String getOstatus() {
        return ostatus;
    }

    public void setOstatus(String ostatus) {
        this.ostatus = ostatus;
    }

    public String getStates_id() {
        return states_id;
    }

    public void setStates_id(String states_id) {
        this.states_id = states_id;
    }

    public String getCitys_id() {
        return citys_id;
    }

    public void setCitys_id(String citys_id) {
        this.citys_id = citys_id;
    }

    public String getAvg_cost() {
        return avg_cost;
    }

    public void setAvg_cost(String avg_cost) {
        this.avg_cost = avg_cost;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDelivary_type() {
        return delivary_type;
    }

    public void setDelivary_type(String delivary_type) {
        this.delivary_type = delivary_type;
    }

    public String getR_url() {
        return r_url;
    }

    public void setR_url(String r_url) {
        this.r_url = r_url;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getAvaragecost_range() {
        return avaragecost_range;
    }

    public void setAvaragecost_range(String avaragecost_range) {
        this.avaragecost_range = avaragecost_range;
    }

    public String getStatus_resturent() {
        return status_resturent;
    }

    public void setStatus_resturent(String status_resturent) {
        this.status_resturent = status_resturent;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getRest_avg_rating() {
        return rest_avg_rating;
    }

    public void setRest_avg_rating(String rest_avg_rating) {
        this.rest_avg_rating = rest_avg_rating;
    }

    public String getCats_id() {
        return cats_id;
    }

    public void setCats_id(String cats_id) {
        this.cats_id = cats_id;
    }

    public String getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(String total_rating) {
        this.total_rating = total_rating;
    }

    public String getOrder_now_link() {
        return order_now_link;
    }

    public void setOrder_now_link(String order_now_link) {
        this.order_now_link = order_now_link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstb_type() {
        return estb_type;
    }

    public void setEstb_type(String estb_type) {
        this.estb_type = estb_type;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEstablishment_type() {
        return establishment_type;
    }

    public void setEstablishment_type(String establishment_type) {
        this.establishment_type = establishment_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMore_info_id() {
        return more_info_id;
    }

    public void setMore_info_id(String more_info_id) {
        this.more_info_id = more_info_id;
    }

    public String getCusine_id() {
        return cusine_id;
    }

    public void setCusine_id(String cusine_id) {
        this.cusine_id = cusine_id;
    }

    public String getAvaragecost_type() {
        return avaragecost_type;
    }

    public void setAvaragecost_type(String avaragecost_type) {
        this.avaragecost_type = avaragecost_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatagory_name() {
        return catagory_name;
    }

    public void setCatagory_name(String catagory_name) {
        this.catagory_name = catagory_name;
    }

    public String getCash_and_card() {
        return cash_and_card;
    }

    public void setCash_and_card(String cash_and_card) {
        this.cash_and_card = cash_and_card;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [ostatus = " + ostatus + ", states_id = " + states_id + ", citys_id = " + citys_id + ", avg_cost = " + avg_cost + ", latitude = " + latitude + ", delivary_type = " + delivary_type + ", r_url = " + r_url + ", city_name = " + city_name + ", s_id = " + s_id + ", avaragecost_range = " + avaragecost_range + ", status_resturent = " + status_resturent + ", state_name = " + state_name + ", contact = " + contact + ", cat_id = " + cat_id + ", rest_avg_rating = " + rest_avg_rating + ", cats_id = " + cats_id + ", total_rating = " + total_rating + ", order_now_link = " + order_now_link + ", id = " + id + ", estb_type = " + estb_type + ", longitude = " + longitude + ", image = " + image + ", website = " + website + ", establishment_type = " + establishment_type + ", address = " + address + ", more_info_id = " + more_info_id + ", cusine_id = " + cusine_id + ", avaragecost_type = " + avaragecost_type + ", name = " + name + ", catagory_name = " + catagory_name + ", cash_and_card = " + cash_and_card + ", time = " + time + ", city_id = " + city_id + "]";
    }
}
