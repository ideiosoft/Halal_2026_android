package com.dapl.halaladvisor.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dapl.halaladvisor.Adapter.AddressAdapter;
import com.dapl.halaladvisor.Model.AddressObject;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.extra.AppController;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dapl.halaladvisor.Utility.PrefUtils.getJsonFromObject;
import static com.dapl.halaladvisor.Utility.PrefUtils.getObjectFromJson;

public class AutoCompleteAddressSearchActivity extends Activity implements View.OnClickListener, AddressAdapter.OnAdapterActionListener {

    private static final String TAG_REQUEST_ADDRESS_SEARCH = "req_address_search";
    Context context;
    EditText etAddress;
    ImageView ivBack, ivClearAddress, ivMessage;
    RecyclerView recyclerView;
    ArrayList<Object> arrayList = new ArrayList<>();
    AddressAdapter addressAdapter;
    ProgressBar progressBar;
    ConstraintLayout layoutMessage;
    TextView tvMessage;
    int startValue = 0;
    String searchKEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.TOP);

//Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_auto_cpmplete_address_search);

        this.setFinishOnTouchOutside(true);

        context = this;

        initUi();

        setData();


        etAddress.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchKEY = editable.toString();

                if (editable.toString().length() == 0) {

                    hideCrossButton();

                } else {

                    showCrossButton();
                }

                AppController.getInstance().getRequestQueue().cancelAll(TAG_REQUEST_ADDRESS_SEARCH);
                clearList();

                hideLayoutMessage();
                hideProgressBar();


                if (editable.toString().length() > 0) {
                    startValue = 0;

                    reqAddressSearch(editable.toString());

                }
            }
        });

    }

    private void showCrossButton() {
        ivClearAddress.setVisibility(View.VISIBLE);
    }

    private void hideCrossButton() {
        ivClearAddress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        cancelRequest();
    }

    private void clearList() {
        startValue = 0;
        arrayList.clear();
        addressAdapter.notifyDataSetChanged();
    }

    private void setData() {

/*
        AddressObject object = new AddressObject();
        object.setAddress("445 Mount Eden Road, Mount Eden, Auckland");
        arrayList.add(object);

        AddressObject object2 = new AddressObject();
        object2.setAddress("445 Mount Eden Road, Mount Eden, Auckland");
        arrayList.add(object2);

        AddressObject object3 = new AddressObject();
        object3.setAddress("445 Mount Eden Road, Mount Eden, Auckland");
        arrayList.add(object3);

        AddressObject object4 = new AddressObject();
        object4.setAddress("445 Mount Eden Road, Mount Eden, Auckland");
        // arrayList.add(object4);

        AddressObject object5 = new AddressObject();
        object5.setAddress("445 Mount Eden Road, Mount Eden, Auckland");
        //arrayList.add(object5);


        addressAdapter.notifyDataSetChanged();*/
    }

    private void initUi() {

        layoutMessage = findViewById(R.id.layoutBlankList);
        layoutMessage.setOnClickListener(this);
        hideLayoutMessage();

        tvMessage = findViewById(R.id.tvMessage);
        ivMessage = findViewById(R.id.ivMessage);


        progressBar = findViewById(R.id.progressBar);
        hideProgressBar();
        etAddress = findViewById(R.id.etAddress);
        etAddress.requestFocus();
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);

        ivClearAddress = findViewById(R.id.ivCross);
        hideCrossButton();

        ivClearAddress.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addressAdapter = new AddressAdapter(context, arrayList);
        addressAdapter.setOnAdapterActionListener(this);

        recyclerView.setAdapter(addressAdapter);

    }

    private void hideLayoutMessage() {
        layoutMessage.setVisibility(View.GONE);
    }

    private void showLayoutMessage() {
        layoutMessage.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layoutBlankList: {

                startValue = 0;
                reqAddressSearch(etAddress.getText().toString().trim());
                break;
            }


            case R.id.ivBack: {

                cancelRequest();

                break;
            }

            case R.id.ivCross: {

                etAddress.setText("");

                break;
            }
        }
    }

    private void reqAddressSearch(final String searchKey) {

        hideLayoutMessage();
        AppController.getInstance(). getRequestQueue().cancelAll(TAG_REQUEST_ADDRESS_SEARCH);


        String baseUrl = "https://admin.halaladvisor.com.au/halal_api/halal.php";
      //  String baseUrl = "https://admin.halaladvisor.com.au/halal_new/halal_api/halal.php";


        showProgressBar();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                baseUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                hideProgressBar();

                LogUtils.printf("I am response: " + response);

                parseAddressResponse(response.trim(), searchKey);


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.printf("I am error: " + error.getMessage());

                hideProgressBar();

                try {

                    showLayoutMessage();
                    ivMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_net_conn_fail));
                    String msg1 = "Server not respond. <u><font color='#202c5d'>Click to Retry</font></u>";
                    tvMessage.setText(Html.fromHtml(msg1));
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<>();
                params.put("req", "search_au_towns");
                params.put("key", searchKey);
                params.put("start", String.valueOf(startValue));
                params.put("limit", "8");
                LogUtils.printf("I am params: " + params);
                return params;
            }

        };

        Utils.setRetryPolicy(strReq);
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQUEST_ADDRESS_SEARCH);
    }

    private void parseAddressResponse(String response, final String searchKey) {

        LogUtils.printf("I am address response: " + response);


        try {

            JSONObject jsonObject = new JSONObject(response.trim());

            if (jsonObject.optBoolean("flag")) {

                JSONArray array = jsonObject.optJSONArray("search_list");

                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject address = array.optJSONObject(i);
                        Log.e("dddd",new Gson().toJson(address));
                        if (address != null) {

                            AddressObject object = (AddressObject) getObjectFromJson(address.toString().trim(), AddressObject.class);


                            /*1. Suburb
                            2. Street number
                            3 Street name
                            4. State
                            5. Postcode*/
                            ArrayList<String> tempList = new ArrayList<>();
                            tempList.add(object.getName());
                            //tempList.add(object.getUrban_area());
                            tempList.add(object.getPostcode());
                            //tempList.add(object.getState());
                            tempList.add(object.getState_code());
                            //tempList.add(object.getLocal_government_area());
                            //tempList.add(object.getRegion());


                            Collections.sort(tempList, new Comparator<String>() {
                                @Override
                                public int compare(String s, String t1) {


                                    if (s.toLowerCase().contains(searchKey.toLowerCase())) {
                                        return -1;
                                    }

                                    return 0;
                                }
                            });
                            object.setUserKeywordAddressData(tempList.get(0));
                            //========================================================


                            Collections.sort(tempList, new Comparator<String>() {
                                @Override
                                public int compare(String object1, String object2) {


                                    int dist1 = object1.toLowerCase().split(searchKEY.toLowerCase())[0].length();

                                    int dist2 = object2.toLowerCase().split(searchKEY.toLowerCase())[0].length();

                                    return dist1 - dist2;
                                }
                            });


                            //=============================================================
                            String fullAddress = "";


                            for (int m = 0; m < tempList.size(); m++) {

                                if (tempList.get(m) != null) {
                                    if (m == 0) {
                                        fullAddress = tempList.get(m);
                                    } else {
                                        fullAddress = fullAddress + ", " + tempList.get(m);
                                    }
                                }

                            }

                            LogUtils.printf("I am full address: " + fullAddress);

                          /*  if (object.getUrban_area() != null) {
                                fullAddress = object.getName()
                                        + ", " + object.getUrban_area()
                                        + ", " + object.getPostcode()
                                        + ", " + object.getState()
                                        + ", " + object.getState_code()
                                        + ", " + object.getLocal_government_area()
                                        + ", " + object.getRegion();
                            } else {
                                fullAddress = object.getName()
                                        + ", " + object.getPostcode()
                                        + ", " + object.getState()
                                        + ", " + object.getState_code()
                                        + ", " + object.getLocal_government_area()
                                        + ", " + object.getRegion();
                            }*/


                            object.setFullAddress(fullAddress);

                            object.setSearchKey(searchKey);
                            object.setUserKeywordAddressData(tempList.get(0));

                            arrayList.add(object);

                        }


                    }

                    //============================================================

                    //Sorting=============


                    //===================================================
                    addressAdapter.notifyDataSetChanged();
                } else {
                    try {
                        if (arrayList.size() == 0) {
                            showLayoutMessage();
                            ivMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_data_not_found));
                            String msg2 = "No Address Found. <u><font color='#202c5d'>Click to Retry</font></u>";
                            tvMessage.setText(Html.fromHtml(msg2));

                        }


                        // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {
                try {
                    if (arrayList.size() == 0) {
                        showLayoutMessage();
                        ivMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_data_not_found));
                        String msg2 = "No Address Found. <u><font color='#202c5d'>Click to Retry</font></u>";
                        tvMessage.setText(Html.fromHtml(msg2));


                    }
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onItemAddressClickListener(AddressObject object) {


        object.setHistory(true);

        String jsn = PrefUtils.getJsonFromObject(object, AddressObject.class);
        try {
            JSONObject jsonObject = new JSONObject(jsn);
            PrefUtils.insertToAddressHistory(context, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }


        successResult(object);

        //  Toast.makeText(context, "I am address click", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLastItemVisibleListener() {
        startValue = startValue + 5;
        reqAddressSearch(searchKEY);
    }


    private void successResult(AddressObject object) {
        try {
            Intent returnIntent = getIntent();


            try {

                String jsn = getJsonFromObject(object, AddressObject.class).trim();

                returnIntent.putExtra("result", jsn);
            } catch (Exception e) {
                e.printStackTrace();
            }

            setResult(RESULT_OK, returnIntent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelRequest() {
        Intent returnIntent = getIntent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}
