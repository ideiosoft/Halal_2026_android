package com.dapl.halaladvisor.interfaces;
import org.json.JSONObject

interface OnResponseInstaListner {

    //method which have parameters
    fun onInstaResponse(jsonObject: JSONObject)

    fun onInstaError(errorString: String)


}