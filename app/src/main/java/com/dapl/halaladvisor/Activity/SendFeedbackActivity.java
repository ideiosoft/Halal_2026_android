package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.ProfileDetailObject;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.application_data.CurrentDateTime;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendFeedbackActivity extends AppCompatActivity {
    TextView tv_reg_cancel;
    EditText edName;
    EditText edEmail;
    TextView tvSend;
    EditText edFeedback;
    String msg="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        tv_reg_cancel = findViewById(R.id.tv_reg_cancel);
        edName = findViewById(R.id.edName);
        edFeedback = findViewById(R.id.edFeedback);
        edEmail = findViewById(R.id.edEmail);
        tvSend = findViewById(R.id.tvSend);
        progressDialog = new ProgressDialog(SendFeedbackActivity.this);

        progressDialog.setMessage("Please Wait...");

        progressDialog.setCancelable(false);

        tv_reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edName.getText().toString().isEmpty()) {
                    Toast.makeText(SendFeedbackActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (edEmail.getText().toString().isEmpty()) {
                    Toast.makeText(SendFeedbackActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (edFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(SendFeedbackActivity.this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
                } else {
                    //new SendFeeback().execute();


                    callApiWithJsonReqPost();
                }
            }
        });
    }


    private class SendFeeback extends AsyncTask<Void, Void, String> {

        String response;

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SendFeedbackActivity.this);

            progressDialog.setMessage("Please Wait...");

            progressDialog.setCancelable(false);

            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", edName.getText().toString());
            map.put("email", edEmail.getText().toString());
            map.put("feedback", edFeedback.getText().toString());

            JSONParser parser = new JSONParser();


            String mURL = AppData.SEND_FEEDBACK;

            LogUtils.printf("I am url: " + mURL);
            Log.e("params: ",""+ map);


            response = parser.makeHttpRequest(mURL, "POST", map);
            LogUtils.printf("response: " + response);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Restaurant List response: " + response);


            setData(response);


        }


    }

    private void callApiWithJsonReqPost() {

        progressDialog.show();
        boolean failure = false;

        JSONObject jsonObject=null;

        try {


            jsonObject=new JSONObject();
            jsonObject.put("name", edName.getText().toString());
            jsonObject.put("email",  edEmail.getText().toString());
            jsonObject.put("feedback", edFeedback.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                AppData.SEND_FEEDBACK, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        Log.d("new_address" ,"sons=="+response.toString());
                        try {
                            if (response.getString("Status").equals("Success")) {

                                msg = response.getString("Message");

                                Toast.makeText(SendFeedbackActivity.this, msg, Toast.LENGTH_SHORT).show();

                                finish();

                            } else {

                                LogUtils.printf("I am Restaurant List error: " + response);
                                msg = response.getString("Message");
                                Toast.makeText(SendFeedbackActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"1");


    }
    private void setData(String response) {




        String msg = "";
        try {

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("Status").equals("Success")) {

                msg = jsonObject.getString("Message");

                Toast.makeText(SendFeedbackActivity.this, msg, Toast.LENGTH_SHORT).show();

                finish();

            } else {

                LogUtils.printf("I am Restaurant List error: " + response);
                msg = jsonObject.getString("Message");
                Toast.makeText(SendFeedbackActivity.this, msg, Toast.LENGTH_SHORT).show();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}