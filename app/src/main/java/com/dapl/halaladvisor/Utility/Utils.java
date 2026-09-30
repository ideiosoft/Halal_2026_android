package com.dapl.halaladvisor.Utility;

import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.AppController;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private static final int NO_OF_RETRY = 2;

    private static final int TIME_OUT = 12000;

    public static void setRetryPolicy(StringRequest request) {

        AppController.getInstance().getRequestQueue().getCache().clear();

        request.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }




    public static String convertDateFormat(String datee) {

        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(datee);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }



    public static Object getObjectFromJson(String jsonText, Class<?> classType) {
        Object object = null;
        if (jsonText != null) {
            Gson gson = new Gson();
            object = gson.fromJson(jsonText, classType);
        }
        return object;
    }

    public static void imageWithGlide(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.no_image_available)
                .into(view);
    }
    public static String getJsonFromObject(Object object, Class<?> classType) {
        String jsonText = "";
        if (object != null) {
            Gson gson = new Gson();
            jsonText = gson.toJson(object, classType);
        }
        return jsonText;
    }

}
