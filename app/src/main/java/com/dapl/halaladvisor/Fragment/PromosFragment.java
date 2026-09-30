package com.dapl.halaladvisor.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dapl.halaladvisor.Activity.AutoCompleteAddressSearchActivity;
import com.dapl.halaladvisor.Adapter.FavoriteResturentAdapter;
import com.dapl.halaladvisor.Adapter.HomeRestaurentAdapter;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.AddressObject;
import com.dapl.halaladvisor.Model.LastSearchLocationObject;
import com.dapl.halaladvisor.Model.RestaurantFavoriteListModel;
import com.dapl.halaladvisor.Model.RestaurantListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.application_data.AppData;
import com.dapl.halaladvisor.application_data.CurrentDateTime;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.AppController;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PromosFragment extends Fragment implements FavoriteResturentAdapter.OnRequestRemoveFromFavListListener {

    private Geocoder mGeocoder;
    View view;
    private final String TAG_REQUEST_RESTAURANT_SEARCH = "request_restaurant_search";

    Context context;
    ProgressBar progressBar;
    public ArrayList restaurantListModels = new ArrayList<RestaurantListModel>();
    HomeRestaurentAdapter restaurentAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView tvAddress, rowName;
    ArrayList<RestaurantFavoriteListModel> arrayList = new ArrayList<>();

    ArrayList<Object> restaurentList = new ArrayList<>();

    FavoriteResturentAdapter adapter;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;

    public static Fragment newInstance() {

        PromosFragment favouriteFragment = new PromosFragment();


        return favouriteFragment;
    }

    private void openAutoCompletePlaceActivityResult() {
        Intent intent = new Intent(context, AutoCompleteAddressSearchActivity.class);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            switch (resultCode) {
                case Activity.RESULT_OK: {

                    if (data != null) {


                        //   Place place = Autocomplete.getPlaceFromIntent(data);

                        AddressObject addressObject = (AddressObject) Utils.getObjectFromJson(data.getStringExtra("result"), AddressObject.class);


                        try {
                            LastSearchLocationObject locationObject = new LastSearchLocationObject();
                            locationObject.setAddress(addressObject.getFullAddress());
                            locationObject.setName(addressObject.getName());
                            locationObject.setCode(addressObject.getState_code());
                            locationObject.setLattitude(Double.parseDouble(addressObject.getLatitude()));
                            locationObject.setLongitude(Double.parseDouble(addressObject.getLongitude()));
                            PrefUtils.saveLastSearchedLocation(context, locationObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Geocoder gcd = new Geocoder(context);
                        List<Address> addresses = null;
                        try {
                            // addresses = gcd.getFromLocation(Double.parseDouble(addressObject.getLatitude()), Double.parseDouble(addressObject.getLongitude()), 1);
                            addresses = gcd.getFromLocationName(addressObject.getFullAddress(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {
                            System.out.println(addresses.get(0));
                            // rowName.setText("Current Deals in  "+addresses.get(0).getSubAdminArea()/*+", "+addressObject.getState_code()*/);

                        } else {
                            // do your stuff
                        }
                        tvAddress.setText(addressObject.getFullAddress());
                        Log.e("ff", new Gson().toJson(addressObject));
                        Log.e("f3f", new Gson().toJson(addresses.get(0)));
                        Log.e("f2f", new Gson().toJson(data.getStringExtra("result")));

                        if (addressObject.getLatitude() != null && addressObject.getLongitude() != null)

                            AllConstants.UPlat = String.valueOf(addressObject.getLatitude());
                        AllConstants.UPlng = String.valueOf(addressObject.getLongitude());

                        // restaurentList.clear();
                        Log.e("11111", "111111111" + addressObject.getName());

                        reqRestaurantListVolley(addressObject.getUrban_area());
                        double Lati = Double.parseDouble(addressObject.getLatitude());
                        double Longi = Double.parseDouble(addressObject.getLongitude());
                       // getCityname(Lati,Longi);
                       // getCityname(Lati, Longi);
                        // new ReqRestaurentList(addressObject.getLatitude(), addressObject.getLongitude()).execute();

                      /*  try {
                            LastSearchLocationObject locationObject = new LastSearchLocationObject();
                            locationObject.setAddress(place.getAddress());
                            locationObject.setLattitude(place.getLatLng().latitude);
                            locationObject.setLongitude(place.getLatLng().longitude);
                            PrefUtils.saveLastSearchedLocation(context, locationObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        tvAddress.setText(place.getAddress());

                        if (place.getLatLng() != null)
                            new ReqRestaurentList(String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude)).execute();*/
                    }


                    break;
                }
                case AutocompleteActivity
                        .RESULT_ERROR: {

                    Toast.makeText(context, Autocomplete.getStatusFromIntent(data).getStatusMessage(), Toast.LENGTH_SHORT).show();

                    break;

                }
                case Activity.RESULT_CANCELED: {


                    //
                    break;
                }


            }


            //==========================
        } /*else if (requestCode == LOCATION_REQ) {

            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                String latLngJsonObj = data.getStringExtra("response");


                setAddressFromLocation(latLngJsonObj);


                // Toast.makeText(mContext, latLngJsonObj, Toast.LENGTH_SHORT).show();

                LogUtils.printf("I am in Line:" + latLngJsonObj);
            }


        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        setDataFromLastSearchedLocation(PrefUtils.getLastSearchedLocation(context));

    }

    private void setDataFromLastSearchedLocation(LastSearchLocationObject lastSearchedLocation) {

        if (lastSearchedLocation != null) {
            Log.e("vvv", new Gson().toJson(lastSearchedLocation));
            Geocoder gcd = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(lastSearchedLocation.getLattitude(), lastSearchedLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0));
                //    rowName.setText("Current Deals in  "+addresses.get(0).getSubAdminArea()/*+", "+lastSearchedLocation.getCode()*/);

            } else {
                // do your stuff
            }
            //  Toast.makeText(context, "last location", Toast.LENGTH_SHORT).show();
            tvAddress.setText(lastSearchedLocation.getAddress());
            AllConstants.UPlat = "-33.84790";
            AllConstants.UPlng = "150.86605";
            Log.e("11111", "2222222");
            reqRestaurantListVolley("Sydney");

            //new ReqRestaurentList(String.valueOf(lastSearchedLocation.getLattitude()), String.valueOf(lastSearchedLocation.getLongitude())).execute();

        }


    }




    private void hideProgressBar() {

        progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {

        progressBar.setVisibility(View.VISIBLE);
    }

    private void reqRestaurantListVolley(String name1) {
        showProgressBar();
        //hideLayoutMessage();

        AppController.getInstance().getRequestQueue().cancelAll(TAG_REQUEST_RESTAURANT_SEARCH);

        String baseUrl = AppData.OFFER_LIST_URL;

        LogUtils.printf("I am Restaurant List baseUrl: " + baseUrl);
        /*if (restaurentList.size() > 0) {
            hideProgressBar();
        } else {
            showProgressBar();
        }*/


        //showProgressBar();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                baseUrl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideProgressBar();
                LogUtils.printf("I am Restaurant List response: " + response);


                try {


                    // restaurentList.clear();
                    //   latlngs.clear();


                    setData(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.printf("I am error: " + error.getMessage());
                hideProgressBar();
                //hideProgressBar();

                try {

                    /*showLayoutMessage();
                    ivMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_net_conn_fail));
                    String msg1 = "Server not respond. <u><font color='#202c5d'>Click to Retry</font></u>";
                    tvMessage.setText(Html.fromHtml(msg1));*/
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cat_id", "2,3,6");
                params.put("cuisine_id", "");
                //  params.put("lattitude", "-33.7403018");
                // params.put("longitude", "150.88592429999994");
              /*  String namee="";
                if(name.contains(" "))
                {
                    namee=name.split(" ")[0];
                }
                else
                {
                    namee=name;
                }*/
                params.put("city", name1);
                params.put("lattitude", AllConstants.UPlat);
                params.put("longitude", AllConstants.UPlng);
                params.put("presentdate", CurrentDateTime.getCurrntDate());
                params.put("time", CurrentDateTime.getCurrentTime());
                params.put("page_no", String.valueOf(1));
                params.put("distance", "1000000");
                params.put("u_id", PrefUtils.getProfileObject(context).getU_id());


                LogUtils.printf("I am Restaurant List param: " + params.toString());

                return params;
            }

        };

        Utils.setRetryPolicy(strReq);
        AppController.getInstance().addToRequestQueue(strReq, TAG_REQUEST_RESTAURANT_SEARCH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_promos, container, false);

        initUi(view);

        return view;
    }

    private void initUi(View view) {

        progressBar = view.findViewById(R.id.progressBar);
        tvAddress = view.findViewById(R.id.tvAddress);
        rowName = view.findViewById(R.id.rowName);

        hideProgressBar();
        recyclerView = view.findViewById(R.id.rvCuisine);


        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        restaurentAdapter = new HomeRestaurentAdapter(context, restaurentList, "promo");
        recyclerView.setAdapter(restaurentAdapter);
        recyclerView.setNestedScrollingEnabled(false);
/*
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FavoriteResturentAdapter(context, arrayList);

        adapter.setOnRequestRemoveFromFavListListener(this);

        recyclerView.setAdapter(adapter);*/

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutoCompletePlaceActivityResult();
            }
        });

        //   new ReqFavRestaurentList().execute();

    }


    private void setData(String response) {

        try {


            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
/*
            if (status.equals("0")) {
               restaurentList.clear();
               restaurantListModels.clear();
                googleMap.clear();
                latlngs.clear();
                restaurentAdapter.notifyDataSetChanged();


            }*/

            Log.e("status", "---" + status);
            if (jsonObject.optInt("status") == 1) {
                // progressBarBottom.setVisibility(View.GONE);
                JSONArray array = jsonObject.optJSONArray("result");
                restaurentList.clear();
                for (int i = 0; i < array.length(); i++) {

                    JSONObject jsn = array.getJSONObject(i);

                    RestaurantListModel model = (RestaurantListModel) PrefUtils.getObjectFromJson(jsn.toString(), RestaurantListModel.class);

                    restaurantListModels = new Gson().
                            fromJson(array.toString(), new TypeToken<List<RestaurantListModel>>() {
                            }.getType());


                    Log.e("rest list ", "--" + restaurantListModels);
                    JSONArray cuisineArray = jsn.getJSONArray("cuisines_name");

                    model.setCusineJsnArray(jsn.getJSONArray("cuisines_name").toString().trim());

                    String cuisine_name = "";
                    String latitude = "";
                    String longitude = "";


                    latitude = jsn.getString("latitude");
                    longitude = jsn.getString("longitude");


                    if (!latitude.isEmpty() && !longitude.isEmpty()) {


                        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                        //  latlngs.add(latLng);


                    }


                    //  Log.e("latlngs--", "--" + latlngs);


                    if (cuisineArray.length() >= 0) {

                        for (int j = 0; j < cuisineArray.length(); j++) {

                            if (!TextUtils.isEmpty(cuisineArray.getJSONObject(j).optString("cuisines_name")))

                                cuisine_name = cuisine_name + " " + getString(R.string.bullet_str) + " " + cuisineArray.getJSONObject(j).optString("cuisines_name");
                        }
                    } else {
                        cuisine_name = "NA";
                    }

                    model.setCuisine_type(cuisine_name);

                    LogUtils.printf("I am model before:" + model.toString());

                    restaurentList.add(model);


                }


                //====Sorting Based on Open/Closed======

/*
                Collections.sort(restaurentList, new Comparator<Object>() {
                    @Override
                    public int compare(Object object1, Object object2) {

                        RestaurantListModel obj1 = (RestaurantListModel) object1;
                        RestaurantListModel obj2 = (RestaurantListModel) object2;

                        if (obj1.getStatus().trim().toLowerCase().equalsIgnoreCase("open")) {
                            return -1;
                        }
                        return 0;
                    }
                });
*/


                //===================================
                restaurentAdapter.notifyDataSetChanged();
                // loading = false;


                //  setDataOnMap();
            } else {

                //    progressBarBottom.setVisibility(View.GONE);
                restaurentList.clear();
                restaurentAdapter.notifyDataSetChanged();
                Toast.makeText(context, "No venues found in your area,", Toast.LENGTH_SHORT).show();


            }

        } catch (Exception e) {

            e.printStackTrace();

        }


    }
    /*
    private void setData(String response) {


        arrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.optBoolean("flag")) {

                JSONArray array = jsonObject.optJSONArray("resturant_details");

                for (int i = 0; i < array.length(); i++) {

                    try {
                        String jsnObject = array.getJSONObject(i).toString().trim();

                        RestaurantFavoriteListModel model = (RestaurantFavoriteListModel) PrefUtils.getObjectFromJson(jsnObject, RestaurantFavoriteListModel.class);

                        arrayList.add(model);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                adapter.notifyDataSetChanged();


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }*/

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;


    }

    @Override
    public void onRequestRemoveFromFavListListener(String restId, int position) {

        new ReqRemoveFromFavList(restId, position).execute();


    }

    private class ReqFavRestaurentList extends AsyncTask<Void, Void, String> {

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

            JSONParser parser = new JSONParser();


            String mURL = "https://admin.halaladvisor.com.au/halal_api/halal.php?req=fav_res_list&user_id=" + PrefUtils.getProfileObject(context).getU_id();

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

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

    private class ReqRemoveFromFavList extends AsyncTask<Void, Void, String> {

        String response;
        String restId;
        int position;
        ProgressDialog progressDialog;


        public ReqRemoveFromFavList(String restId, int position) {
            this.restId = restId;
            this.position = position;
        }

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

            JSONParser parser = new JSONParser();


            String mURL = "https://admin.halaladvisor.com.au/halal_api/res_unfav.php?req=unfav_res&key=1702NKDRDA" +
                    "&user_id=" + PrefUtils.getProfileObject(context).getU_id()
                    + "&r_id=" + restId;

            LogUtils.printf("I am url: " + mURL);


            response = parser.makeHttpRequest(mURL, "POST", map);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            LogUtils.printf("I am Restaurant List response: " + response);


            try {

                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.optBoolean("flag")) {

                    arrayList.remove(position);

                    Toast.makeText(context, "You have removed this restaurant from your favourites list.", Toast.LENGTH_SHORT).show();

                    adapter.notifyDataSetChanged();

                }


            } catch (Exception ex) {

                ex.printStackTrace();

            }


        }


    }

}
