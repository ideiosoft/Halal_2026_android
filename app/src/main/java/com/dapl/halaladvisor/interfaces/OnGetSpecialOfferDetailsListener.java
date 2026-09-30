package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;


public interface OnGetSpecialOfferDetailsListener {

    void onGetSpecialOfferDetailsListenerSuccess(JSONObject obj);

    void onGetSpecialOfferDetailsListenerFailed();
}
