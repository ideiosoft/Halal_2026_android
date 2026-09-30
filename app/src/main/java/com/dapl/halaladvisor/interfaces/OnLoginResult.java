package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * Created by DAT-Asset-131 on 21-06-2016.
 */
public interface OnLoginResult {

    void onLoginSuccess(JSONObject obj);
    void onLoginError(String error);

}
