package com.dapl.halaladvisor.interfaces;
import org.json.JSONArray
import org.json.JSONObject

interface OnResponseEventListner {

    //method which have parameters
    fun onResponse(jsonObject: JSONObject)
    fun onResponse(jsonArray: JSONArray)
    fun onError(errorString: String)


}