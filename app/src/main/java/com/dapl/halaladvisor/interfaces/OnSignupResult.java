package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * Created by DAT-Asset-131 on 20-06-2016.
 */
public interface OnSignupResult {

    void onSignupSuccess(JSONObject obj);
    void onSignupError(String error);

}
