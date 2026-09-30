package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;


public interface LocationDirectionListener {

    void onLocationDirectionSuccess(JSONObject obj);

    void onLocationDirectionFailed();

}
