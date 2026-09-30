package com.dapl.halaladvisor.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dapl.halaladvisor.Activity.AddReviewActivity;
import com.dapl.halaladvisor.Activity.DetailsActivity;
import com.dapl.halaladvisor.Activity.DroidWebViewActivity;
import com.dapl.halaladvisor.Activity.VoucharActivity;
import com.dapl.halaladvisor.Activity.WelcomeActivity;
import com.dapl.halaladvisor.MainActivity;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.SplashActivity;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.AppController;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //   MyFirebaseMessagingService
    private static final String TAG = "Fire base Msg Service";
    Bitmap largeIcon = null;


    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getBitmap(String image_link) {
        URL website;

        try {
            website = new URL(image_link);
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            InputStream is = connection.getInputStream();

         /*   if(req_width == 0)
            {
                return BitmapFactory.decodeStream(is);
            }*/


            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
            is.close();


            connection = (HttpURLConnection) website.openConnection();
            is = connection.getInputStream();
            options.inSampleSize = calculateInSampleSize(options, 600, 600);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(is, null, options);

        } catch (Exception e) {
            return null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LogUtils.printf("I am MyFirebaseMessagingService**  " + remoteMessage.getData().get("message"));


        try {
            Log.e("11", "11");
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("message"));
            Log.e("11", "22");
            try {
                Log.e("11", "33");

                setNotificationMessage(jsonObject.getString("title"), jsonObject.getString("body"), jsonObject.getString("image"), getRespectiveIntent(remoteMessage.getData().get("message")), remoteMessage.getData().get("message")

                );

            } catch (ActivityNotFoundException e) {
                Log.e("11", "44");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        LogUtils.showErrorLog("**MyFirebaseMessagingService**",remoteMessage.getNotification().getBody());

      /*  if (remoteMessage.getNotification().getBody() != null) {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
        } else {
            Log.i("PVL", "RECEIVED MESSAGE: " + remoteMessage.getData().get("message"));
        }*/


    }




    private Intent getRespectiveIntent(String remoteMessageData) {
        Log.e("ffff", "fff");
        Intent myIntent = null;

        if (remoteMessageData != null) {
            try {
                JSONObject jsonObjectRemote = new JSONObject(remoteMessageData);

                if (jsonObjectRemote.has("noti_tag")) {

                    String noti_tag = jsonObjectRemote.getString("noti_tag");


                    Log.d("Notidata", "getRespectiveIntent: " + noti_tag);

                    if (noti_tag.equals("review")) {
                        Log.d(TAG, "review:");
                        openRestaurantDetailPage(jsonObjectRemote.getJSONArray("details").toString());
                        myIntent = new Intent(getApplicationContext(), AddReviewActivity.class).putExtra("noti_type", "yes");
                    } else if (noti_tag.equals("noti_for_url")) {
                        Log.d(TAG, "details Rest:");
                        try {
                            Log.e("ffff", "111");
                            LogUtils.printf("I am restDetails: " + jsonObjectRemote.getJSONArray("details").toString());
                            openRestaurantDetailPage(jsonObjectRemote.getJSONArray("details").toString());
                            myIntent = new Intent(getApplicationContext(), DetailsActivity.class);
                        } catch (Exception e) {
                            Log.e("ffff", "2222");
                            e.printStackTrace();
                        }


                    }
                    else if (!jsonObjectRemote.has("details")) {
                        Log.d(TAG, "details:");
                        String url = jsonObjectRemote.optString("url");

                        if (url != null && !url.isEmpty()) {
                            try {
                                AllConstants.webUrl = url;
                                myIntent = new Intent(Intent.ACTION_VIEW);
                                myIntent.setData(Uri.parse(url));
                                startActivity(myIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(this, "No application can handle this request. Please install a Web Browser", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Log.d(TAG, "details Rest:");
                        try {
                            Log.e("ffff", "111");
                            LogUtils.printf("I am restDetails: " + jsonObjectRemote.getJSONArray("details").toString());
                            openRestaurantDetailPage(jsonObjectRemote.getJSONArray("details").toString());
                            myIntent = new Intent(getApplicationContext(), DetailsActivity.class);
                        } catch (Exception e) {
                            Log.e("ffff", "2222");
                            e.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "noti_tag is missing in the JSON data");
                    String notification_type = jsonObjectRemote.getString("notification_type");
                    Log.d("Notidata", "getRespectiveIntent: " + notification_type);
                    if (notification_type.equals("Offer")) {
                        Log.d(TAG, "Offer:");
                        String notification = jsonObjectRemote.optString("notification_type");
                        String lattitude = jsonObjectRemote.optString("lattitude");
                        String longitude = jsonObjectRemote.optString("longitude");
                        Log.d(TAG, "notification_type: " + notification);
                        myIntent = new Intent(getApplicationContext(), SplashActivity.class);
                        myIntent.putExtra("notification_type", notification);
                        myIntent.putExtra("longitude", longitude);
                        myIntent.putExtra("lattitude", lattitude);
                    }
                }
            } catch (JSONException e) {
                Log.e("ffff", "333" + e);
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "datanull Rest:");
            Log.e("hhhhhhhh", "hhhhhhh---->" + "else");
            myIntent = new Intent(getApplicationContext(), MainActivity.class);
        }

        return myIntent;
    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.e("hhhhhhhh", "hhhhhhh---->" + s);
        // LogUtils.showErrorLog("**MyFirebaseMessagingService**",s);
    }

    private void setNotificationMessage(String title, String body, String imageUrl, Intent mIntent, String remoteMessageData) {

        //  LogUtils.showErrorLog("**title**",title+"**body**"+body);
        PendingIntent pendingIntent = null;
        if (mIntent != null) {
            pendingIntent = PendingIntent.getActivity(this, 100/* Request code */, mIntent, PendingIntent.FLAG_MUTABLE);

        }

        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/abc");


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.vector_new);


        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app_icon));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle("" + title);
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationBuilder.setContentText("" + body);
        //  notificationBuilder   .setContentText("" + body);

        if (remoteMessageData != null) {
            try {
                JSONObject jsonObjectRemote = new JSONObject(remoteMessageData);
                if (jsonObjectRemote.optString("noti_tag").equalsIgnoreCase("review")) {


                } else {
                    if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(imageUrl)));
                    }
                }
            } catch (Exception e) {
            }
        }
        // .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmap(imageUrl)));

        notificationBuilder.setSound(alarmSound);
        if (mIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.vector_new);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.vector_new);
            //   notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));

            Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                    getResources(),
                    R.mipmap.ic_app_icon);

            try {
                if (largeIcon != null) {
                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeIcon)); // big image setting
                }
                //LogUtils.showErrorLog("LogNotification Image ", "testUrlBit Map " + largeIcon);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            notificationBuilder.setLargeIcon(notificationLargeIconBitmap);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Date now = new Date();
        long uniqueId = now.getTime();
        try {
            NotificationChannel mChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel("mChannelID", "mChannelName", NotificationManager.IMPORTANCE_HIGH);
                notificationBuilder.setChannelId("mChannelID");
                notificationManager.createNotificationChannel(mChannel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (remoteMessageData != null) {
            try {
                JSONObject jsonObjectRemote = new JSONObject(remoteMessageData);
                if (jsonObjectRemote.optString("noti_tag").equalsIgnoreCase("review")) {
                    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body));
                } else {
                    //   notificationBuilder  .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(imageUrl)));

                }
            } catch (Exception e) {
            }
        }

        notificationManager.notify((int) uniqueId, notificationBuilder.build());
        //notificationManager.notify(0, notificationBuilder.build());
    }

    private void openRestaurantDetailPage(String notificationData) {

        try {

            JSONArray array = new JSONArray(notificationData);

            JSONObject jsonObject = array.optJSONObject(0);

            RestaurantListModel model = new RestaurantListModel();

            if (jsonObject.has("id")) {
                PrefUtils.saveResId(getApplicationContext(), jsonObject.optString("id"));
                model.setId(jsonObject.optString("id"));
            } else {
                PrefUtils.saveResId(getApplicationContext(), jsonObject.optString("r_id"));
                model.setId(jsonObject.optString("r_id"));

            }
            if (jsonObject.optString("name").equalsIgnoreCase("")) {
                model.setName(jsonObject.optString("r_name"));

            } else {
                model.setName(jsonObject.optString("name"));

            }
            model.setStatus(jsonObject.optString("ostatus"));
            model.setImage(jsonObject.optString("image"));
            AppController.setSelectedRestaurent(model);

            PrefUtils.saveActivityOpenFromNotification(getApplicationContext(), true);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
