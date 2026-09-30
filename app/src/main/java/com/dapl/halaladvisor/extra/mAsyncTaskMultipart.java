package com.dapl.halaladvisor.extra;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import okhttp3.MultipartBody;

/**
 * Created by himadri on 16/1/16.
 */
public class mAsyncTaskMultipart extends AsyncTask<Void, Void, Void> {

    String respond,url;
    Context context;
    //ProgressDialog pd;
    ProgressDialog dialog;
    AsyncResponse delegate = null;
    MultipartBody.Builder buildernew;
    boolean interruption=true;

    public mAsyncTaskMultipart(Context context, String url, MultipartBody.Builder buildernew,
                               AsyncResponse asyncResponse ){
        this.context=context;
        this.url=url;
        this.buildernew=buildernew;
        this.delegate = asyncResponse;
    }

    public mAsyncTaskMultipart(Context context, String url, MultipartBody.Builder buildernew,
                               AsyncResponse asyncResponse, boolean interruption) {
        this.context=context;
        this.url=url;
        this.buildernew=buildernew;
        this.delegate = asyncResponse;
        this.interruption=interruption;
    }



    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }

    @Override
    public void onPreExecute(){


    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            respond=new mWebService().postMultipart(url,buildernew);
            respond = respond.replaceAll("null", "\"\"");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(Void m_adapter){
        try {
            if (dialog!=null){
                dialog.dismiss();
            }
        }catch (Exception e){e.printStackTrace();}

        try{
           /* if(pd.isShowing())
                pd.dismiss();*/
            delegate.processFinish(respond);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
