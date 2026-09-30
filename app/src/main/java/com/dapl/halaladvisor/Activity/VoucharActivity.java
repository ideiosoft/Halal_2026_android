package com.dapl.halaladvisor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dapl.halaladvisor.Adapter.VoucherAdapter;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.VoucherModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.extra.AlertDialogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VoucharActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;

    ArrayList<VoucherModel> arrayList = new ArrayList<>();

    VoucherAdapter adapter;

    RecyclerView recyclerView;

    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchar);
        context = this;


        initUi();

        new ReqVoucherList().execute();


    }

    private void initUi() {

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new VoucherAdapter(context, arrayList);

        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_back: {

                onBackPressed();

                break;
            }


        }


    }

    private void setData(JSONArray jsonArray) {

        try {
           // JSONArray jsonArray = result.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                VoucherModel voucherModel = new VoucherModel();
                voucherModel.setOffer_id(jsonObject.optString("offer_id"));
                voucherModel.setPhone(jsonObject.optString("phone"));
                voucherModel.setMinimum_order(jsonObject.optString("minimum_order"));
                 voucherModel.setRestId(jsonObject.optString("rest_id"));
                voucherModel.setRestName(jsonObject.optString("rest_name"));
                voucherModel.setTagLine(jsonObject.optString("tagline"));
                voucherModel.setRestLat(jsonObject.optString("rest_lat"));
                voucherModel.setRestLon(jsonObject.optString("rest_long"));
                voucherModel.setOfferPrice(jsonObject.optString("offer_price"));
                voucherModel.setOfferImg(jsonObject.optString("offer_img"));
                voucherModel.setOffer_type(jsonObject.optString("offer_type"));
                voucherModel.setStartDate(jsonObject.optString("start_date"));
                voucherModel.setEndDate(jsonObject.optString("end_date"));
                voucherModel.setCurrentDate(jsonObject.optString("current_date"));
                voucherModel.setOfferDesc(jsonObject.optString("offer_description"));

                arrayList.add(voucherModel);
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private class ReqVoucherList extends AsyncTask<Void, Void, String> {

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


            HashMap<String, String> params = new HashMap<>();

            params.put("user_id", PrefUtils.getProfileObject(context).getU_id());

            JSONParser parser = new JSONParser();


            String mURL = AppData.VOUCHER_LIST_URL;

            LogUtils.printf("I am url: " + params);


            response = parser.makeHttpRequest(mURL, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Details response: " + response);

            try {

                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("status").equals("1")) {

                    setData(jsonObject.getJSONArray("data"));

                } else {


                    AlertDialogManager.showAlertDialog(context, "Alert", jsonObject.optString("message"), false, false);

                }


                //   Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
