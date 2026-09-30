package com.dapl.halaladvisor.application_data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by DAT-Asset-128 on 13-09-2016.
 */
public class CurrentDateTime {

    public static TimeZone tz = TimeZone.getDefault();

    public static String getCurrntDate() {

        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID());

        Calendar c = Calendar.getInstance();
        Date date = c.getTime(); //current date and time in UTC
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        df.setTimeZone(TimeZone.getTimeZone(tz.getID())); //format in given timezone
        String strDate = df.format(date);

        String strSub = strDate.substring(0, strDate.indexOf(" "));
        System.out.println("sub_string: " + strSub);

        String strSub_new = strDate.substring(strDate.indexOf(" ") + 1, strDate.length());
        System.out.println("sub_string new: " + strSub_new);

        System.out.println("Current Date : " + strDate);

        return strSub;
    }


    public static String getCurrentTime(){
        Calendar cal = Calendar.getInstance(tz);
        //        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String time = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + "." +
                String.format("%02d", cal.get(Calendar.MINUTE))/* + ":" +
                String.format("%02d", cal.get(Calendar.SECOND)) + ":" +
                String.format("%03d", cal.get(Calendar.MILLISECOND))*/;

        System.out.println("Current Time : " + time);

        return time;
    }
}
