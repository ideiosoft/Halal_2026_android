package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * Created by DAT-Asset-131 on 21-06-2016.
 */
public interface OnRestaurantListResult {

    void onRestaurantListSuccess(JSONObject obj);
    void onRestaurantListError(String error);

}
