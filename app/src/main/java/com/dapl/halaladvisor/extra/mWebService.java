package com.dapl.halaladvisor.extra;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by himadri on 16/1/16.
 */
public class mWebService {

    OkHttpClient client=new OkHttpClient();


    public String get(String url) throws Exception {

        client=new OkHttpClient().newBuilder().connectTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS).build();

        Request request = new Request.Builder()
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        String r=response.body().string();


        return r;
    }


    public String postParam(String Url, HashMap<String,String> params){
        try{
            // Initialize Builder (not RequestBody)
            FormBody.Builder builder = new FormBody.Builder();
            // Add Params to Builder
            for ( Map.Entry<String, String> entry : params.entrySet() ) {
                builder.add( entry.getKey(), entry.getValue() );
            }
            // Create RequestBody
            RequestBody formBody = builder.build();

            // Create Request (same)
            Request request = new Request.Builder()
                    .url( Url )
                    .post( formBody )
                    .build();
            client=new OkHttpClient().newBuilder().connectTimeout(45, TimeUnit.SECONDS)
                    .writeTimeout(45, TimeUnit.SECONDS)
                    .readTimeout(45, TimeUnit.SECONDS).build();
            Response response = client.newCall(request).execute();
            String r=response.body().string();



            return r;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    public String postMultipart(String url, MultipartBody.Builder buildernew) {
        String r = null;
        try {
            MultipartBody requestBody = buildernew.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client=new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).build();
            Response response = client.newCall(request).execute();
            r=response.body().string();


        }catch (Exception e){
            e.printStackTrace();
        }

        return r;
    }
}
