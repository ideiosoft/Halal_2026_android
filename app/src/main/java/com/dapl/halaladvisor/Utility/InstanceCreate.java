package com.dapl.halaladvisor.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class InstanceCreate {
    public static String getfilterselect(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
        return sharedPreferences.getString("filterselect", "");

    }
    public static void setfilterselect(Context context, String currentLong) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("filterselect", currentLong);
        myEdit.apply();
    }
}
