package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapl.halaladvisor.Model.AddressObject;
import com.dapl.halaladvisor.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    public OnAdapterActionListener adapterActionListener;
    Context context;
    private ArrayList<Object> arrayList;


    public AddressAdapter(Context context, ArrayList<Object> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View mItem = inflater.inflate(R.layout.rowview_address, parent, false);
            return new GenericViewHolder(mItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GenericViewHolder) {

            final GenericViewHolder vh = (GenericViewHolder) holder;

            final AddressObject object = (AddressObject) arrayList.get(position);

            try {
                if (object.isHistory()) {
                    vh.ivHistory.setVisibility(View.VISIBLE);
                } else {
                    vh.ivHistory.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //  String fullAddress = object.getFullAddress();
            String fullAddress = object.getName() + ", " + object.getState_code() + ", " + object.getPostcode();
            //      String fullAddress = object.getName() + "<br />" + object.getState_code() + "<br />" + object.getPostcode();
//     String fullAddress = object.getFullAddress();

            vh.tvFullAddress.setText(Html.fromHtml(fullAddress));

            try {
                if (object.getSearchKey() == null) {
                    vh.tvFullAddress.setText(Html.fromHtml(fullAddress));


                } else {

                    //String serachData = fullAddress;
                    int startLen = fullAddress.toLowerCase().split(object.getSearchKey().toLowerCase())[0].length();

                    String mSearchText = fullAddress.substring(startLen, startLen + object.getSearchKey().length());


                    String string = fullAddress.replace(mSearchText, "<b><font color='#000000'>" + mSearchText + "</font></b>");
                    vh.tvFullAddress.setText(Html.fromHtml(string), TextView.BufferType.SPANNABLE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //=================Address===Title=======================
            try {
                String mText = object.getUserKeywordAddressData();
                if (object.getSearchKey() == null) {
                    vh.tvUserKeyword.setText(mText);


                } else {

                    //String serachData = fullAddress;
                    int startLen = mText.toLowerCase().split(object.getSearchKey().toLowerCase())[0].length();

                    String mSearchText = mText.substring(startLen, startLen + object.getSearchKey().length());


                    String string = mText
                            .replace(mSearchText
                                    , "<b><font color='#000000'>" + mSearchText + "</font></b>");
                    vh.tvUserKeyword.setText(Html.fromHtml(string), TextView.BufferType.SPANNABLE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                vh.rowview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapterActionListener.onItemAddressClickListener(object);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (position == arrayList.size() - 1) {
                adapterActionListener.onLastItemVisibleListener();
            }


            /*Setting user rating*/

        }
    }

    public void setOnAdapterActionListener(OnAdapterActionListener mOnAdapterActionListener) {
        adapterActionListener = mOnAdapterActionListener;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = arrayList.get(position);
        if (item instanceof AddressObject) {
            return ITEM;
        } else {
            return -1;
        }
    }

    public interface OnAdapterActionListener {

        void onItemAddressClickListener(AddressObject object);

        void onLastItemVisibleListener();
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {

        View rowview;
        TextView tvFullAddress, tvUserKeyword;
        ImageView ivHistory;


        GenericViewHolder(View rowView) {
            super(rowView);
            this.rowview = rowView;
            tvUserKeyword = rowView.findViewById(R.id.tvUserKeyword);
            tvFullAddress = rowView.findViewById(R.id.tvFullAddress);
            ivHistory = rowView.findViewById(R.id.ivHistory);

        }

    }
}
