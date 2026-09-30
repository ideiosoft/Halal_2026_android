package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;


public interface OnRedeemOfferListener {

    void onRedeemOfferListenerSuccess(JSONObject obj);

    void onRedeemOfferListenerFailed();
}
