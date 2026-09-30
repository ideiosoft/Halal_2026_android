package com.dapl.halaladvisor.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ConnectionDetector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import dmax.dialog.SpotsDialog;

public class DirectionActivity extends AppCompatActivity {

    Context context;
    WebView myWebView;
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;



    String webURL = "";
    //custom dialog
    AlertDialog dialog;
    private ProgressDialog pd;
    /*private Toolbar mToolbar;
    private TextView toolbar_header_name, toolbar_header_name2, toolbar_tv_add;*/
    private ImageView toolbar_tag_icon, ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        context = this;

        initToolbarViews();

        //user lat lng
        String tempLat="0.0";
        String tempLng="0.0";
        if(PrefUtils.getUserLatLong(context)!=null)
        {
            tempLat=String.valueOf(PrefUtils.getUserLatLong(context).getLattitude());
            tempLng=String.valueOf(PrefUtils.getUserLatLong(context).getLongitude());

Log.e("tLat",tempLat+"");
Log.e("tLng",tempLng+"");
Log.e("ALat",AllConstants.lat+"");
Log.e("ALat",AllConstants.lng+"");
            webURL = "https://maps.google.com/?saddr=" + AllConstants.UPlat + "," + AllConstants.UPlng + "&daddr=" + AllConstants.lat + "," + AllConstants.lng + "";

        /*   : 30.701461
            2022-07-05 09:40:16.083 22569-22569/com.dapl.halaladvisor E/tLng: 76.7189665
            2022-07-05 09:40:16.083 22569-22569/com.dapl.halaladvisor E/ALat: -33.8674634
            2022-07-05 09:40:16.083 22569-22569/com.dapl.halaladvisor E/ALat: 151.2020689*/
         //  webURL = "https://maps.google.com/?saddr=" + tempLat + "," + tempLng + "&daddr=" + AllConstants.lat + "," + AllConstants.lng + "";

           /* Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("google.navigation:q=an+address+city"));*/

           // String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+tempLat + "," + tempLng +"&daddr="+30.701861 + "," + 76.7189665;
             String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+tempLat + "," + tempLng +"&daddr="+AllConstants.lat + "," + AllConstants.lng;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            startActivityForResult(Intent.createChooser(intent, "Select an application"),444);
             webURL = "https://maps.google.com/?saddr=" + tempLat + "," + tempLng + "&daddr=" + 30.701861 + "," + 76.7189665 + "";

        }


        cd = new ConnectionDetector(DirectionActivity.this);
        Log.e("Direction", webURL);
        myWebView = findViewById(R.id.myWebView);

      //  myWebView.loadUrl(webURL);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.setWebViewClient(new MyWebViewClient());

        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);


        myWebView.getSettings().setUseWideViewPort(false);
        myWebView.setWebChromeClient(new MyWebChromeClient());
        myWebView.setWebViewClient(new WebViewClient());

        // these settings speed up page load into the webview
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

//        enableAd();
        checkStatus();
    }


    /*Initialize all views for toolbar*/
    private void initToolbarViews() {


        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


/*

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar_header_name = mToolbar.findViewById(R.id.tvAddress);
        //  toolbar_header_name2 = (TextView) mToolbar.findViewById(R.id.toolbar_header_name2);
        //  toolbar_tv_add = (TextView) mToolbar.findViewById(R.id.toolbar_tv_add);
        // toolbar_tag_icon = (ImageView) mToolbar.findViewById(R.id.toolbar_tag_icon);
        toolbar_header_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        toolbar_header_name.setText("GET DIRECTIONS");
        toolbar_header_name.setTextSize(18);


        toolbar_header_name.setVisibility(View.VISIBLE);

*/

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack(); // Go to previous page
            return true;
        }
        // Use this as else part
        return super.onKeyDown(keyCode, event);
    }

    void checkStatus() {

        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
//        showAlertDialog(getActivity(), "Internet Connection",
//                "You have internet connection", true);
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(DirectionActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==444)
        {
            finish();
        }
    }

    // TODO Auto-generated method stub
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            if (Uri.parse(url).getHost().equals(webURL)) {
                /*pd = new ProgressDialog(DirectionActivity.this);
                pd.setMessage("Please wait ...");
                pd.show();*/
                dialog = new SpotsDialog(DirectionActivity.this, R.style.CustomDialog);
                dialog.setCancelable(false);
                return false;
            }


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (dialog != null) {
                dialog.dismiss();
            }
        }

    }

    class MyWebChromeClient extends WebChromeClient {

        ProgressDialog pDialog;
        private Context context;

        public MyWebChromeClient() {
            // TODO Auto-generated constructor stub
            /*pDialog = new ProgressDialog(DirectionActivity.this, R.style.CustomDialog); //Use getActivity(); for Fragment
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
            //set custom dialog
            context = DirectionActivity.this;
            dialog = new SpotsDialog(DirectionActivity.this, R.style.CustomDialog);
            dialog.setCancelable(false);

        }

        public void onProgressChanged(WebView view, int progress) {
            if (!((Activity) context).isFinishing()) {
                if (progress < 100 && !dialog.isShowing()) {
                    dialog.show();
                }
//            dialog.setProgress(progress);
                if (progress == 100) {
                    dialog.dismiss();
                }
            }
        }
    }
}