package com.dapl.halaladvisor.extra;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.dapl.halaladvisor.Model.RestaurantDetailObject;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.Utility.ClientSSLSocketFactory;
import com.facebook.FacebookSdk;

import androidx.multidex.MultiDex;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    private boolean loginNeededForSpecialOffer = false;

    public static RestaurantListModel selectedRestaurent;

    public static RestaurantDetailObject selectedRestaurantDetailObject;

    public static RestaurantDetailObject getSelectedRestaurantDetailObject() {
        return selectedRestaurantDetailObject;
    }

    public static void setSelectedRestaurantDetailObject(RestaurantDetailObject selectedRestaurantDetailObject) {
        AppController.selectedRestaurantDetailObject = selectedRestaurantDetailObject;
    }

    public static RestaurantListModel getSelectedRestaurent() {
        return selectedRestaurent;
    }

    public static void setSelectedRestaurent(RestaurantListModel selectedRestaurent) {
        AppController.selectedRestaurent = selectedRestaurent;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
/*

    public boolean isLoginNeededForSpecialOffer() {
        return loginNeededForSpecialOffer;
    }

    public void setLoginNeededForSpecialOffer(boolean isSpecialOfferNotification) {
        this.loginNeededForSpecialOffer = isSpecialOfferNotification;
    }
*/

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FacebookSdk.sdkInitialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack(null, ClientSSLSocketFactory.getSocketFactory()));
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        getRequestQueue().getCache().clear();
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().getCache().clear();
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
