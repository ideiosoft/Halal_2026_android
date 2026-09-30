package com.dapl.halaladvisor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AlertDialogManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    EditText etEmail;

    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_pass_dialog);
        context = this;

        initUi();


    }

    private void initUi() {
        etEmail = findViewById(R.id.etForgotPassEmail);
        tvSubmit = findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.tvSubmit: {

                if (isEveryThingOk()) {

                    new ReqForgotPassword().execute();

                }
                break;
            }


        }
    }

    private boolean isEveryThingOk() {

        boolean b;

        ArrayList<String> errorList = new ArrayList<>();

        if (etEmail.getText().toString().trim().length() == 0) {
            errorList.add("Enter your register email id");
        }

        if (!AppData.isValidEmail(etEmail.getText().toString().trim())) {
            errorList.add("Enter an email id");
        }

        if (errorList.size() > 0) {

            etEmail.setError(errorList.get(0));

            b = false;
        } else {
            b = true;
        }


        return b;
    }

    private class ReqForgotPassword extends AsyncTask<Void, Void, String> {

        String response;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Please Wait...");

            progressDialog.setCancelable(false);

            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void... voids) {


            HashMap<String, String> map = new HashMap<>();


            map.put("email", etEmail.getText().toString().trim());


            JSONParser parser = new JSONParser();


            String mURL = AppData.FORGOT_PASSWORD_URL;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am ForgotPass response: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);


                String msg = jsonObject.optString("message").trim();

                if (jsonObject.optInt("status") == 1) {

                    AlertDialogManager.showAlertDialog(context, "", msg.substring(0, msg.length() - 1), false, false);


                } else {

                    AlertDialogManager.showAlertDialog(context, "", msg, false, false);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

}
