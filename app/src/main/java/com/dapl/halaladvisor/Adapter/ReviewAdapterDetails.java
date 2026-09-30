package com.dapl.halaladvisor.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Model.ReviewObject;
import com.dapl.halaladvisor.Model.resdetailsres.Review;
import com.dapl.halaladvisor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewAdapterDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int ITEM = 0;
    Context context;

    private ArrayList<Review> arrayList;


    public ReviewAdapterDetails(Context context, ArrayList<Review> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View mItem = inflater.inflate(R.layout.review_row_new, parent, false);
            return new GenericViewHolder(mItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GenericViewHolder) {

            final GenericViewHolder vh = (GenericViewHolder) holder;

              Review listModel = arrayList.get(position);
            /*Setting user rating*/
            try {
                String rating = listModel.getRating();
                Float count = Float.parseFloat(rating);
                vh.review_ratingBar.setRating(count);
                vh.review_ratingBar.setFocusableInTouchMode(false);
            } catch (Exception e) {
                // TODO: handle exception
            }

            /*Setting user name*/
            try {
                String userName = listModel.getUsername();
                vh.tv_name.setText(userName);
            } catch (Exception e) {
                // TODO: handle exception
            }

            /*Setting user review details*/
            try {
                String review_details = listModel.getReview();
//                vh.tv_postDetails.setText(R.string.dummy_text);
                vh.tv_postDetails.setText(review_details);
            } catch (Exception e) {
                // TODO: handle exception
            }

            /*Setting user time of post*/
            try {
                String timeOfPost = listModel.getTimestamp();
                Log.e("ReviewAdapter: ", "Date of post- " + timeOfPost);
                vh.tv_timeOfPost.setText(timeOfPost);
            } catch (Exception e) {
                // TODO: handle exception
            }

            /*Setting user image*/
            try {
                String imageUrl = listModel.getRruImage();

                if (imageUrl.length() > 1) {
                    Picasso.get().load(imageUrl).placeholder(R.drawable.app_default_image).into(vh.iv_userImage);
                } else {
                    vh.iv_userImage.setImageResource(R.drawable.app_default_image);
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    @Override
    public int getItemCount() {

        if (arrayList.size()>0)
        {
            return 1;
        }else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        Object item = arrayList.get(position);
        if (item instanceof ReviewObject) {
            return ITEM;
        } else {
            return -1;
        }
    }


    class GenericViewHolder extends RecyclerView.ViewHolder {

        View rowview;
        com.dapl.halaladvisor.extra.CircleImageView iv_userImage;
        TextView tv_name, tv_timeOfPost, tv_postDetails;
        RatingBar review_ratingBar;

        GenericViewHolder(View rowView) {
            super(rowView);
            this.rowview = rowView;

            iv_userImage = rowview.findViewById(R.id.iv_review_row_userImage);
            tv_name = rowview.findViewById(R.id.tv_review_row_username);
            tv_postDetails = rowview.findViewById(R.id.tv_review_row_brief);
            tv_timeOfPost = rowview.findViewById(R.id.tv_review_row_time);
            review_ratingBar = rowview.findViewById(R.id.review_row_ratingbar);
        }

    }
}
