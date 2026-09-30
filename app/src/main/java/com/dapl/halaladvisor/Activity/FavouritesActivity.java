package com.dapl.halaladvisor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dapl.halaladvisor.Adapter.FavoriteResturentAdapter;
import com.dapl.halaladvisor.Fragment.FavouriteFragment;
import com.dapl.halaladvisor.JSONParser;
import com.dapl.halaladvisor.Model.RestaurantFavoriteListModel;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.LogUtils;
import com.dapl.halaladvisor.Utility.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouritesActivity extends AppCompatActivity implements FavoriteResturentAdapter.OnRequestRemoveFromFavListListener {


    Context context;

    RecyclerView recyclerView;

    ArrayList<RestaurantFavoriteListModel> arrayList = new ArrayList<>();

    FavoriteResturentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        context= this;
        initUi();
    }

    private void initUi() {

        recyclerView = findViewById(R.id.rvFav);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FavoriteResturentAdapter(context, arrayList);

        adapter.setOnRequestRemoveFromFavListListener(this);

        recyclerView.setAdapter(adapter);

       new ReqFavRestaurentList().execute();

    }

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