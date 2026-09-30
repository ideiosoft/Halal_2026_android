package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Model.cuisine.CuisineModelNew;
import com.dapl.halaladvisor.Model.resdetailsres.Review;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.CuisineClickInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewDetailsAdapter extends RecyclerView.Adapter<ReviewDetailsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Review> arrayList;
    String baseUrl="";

    public ReviewDetailsAdapter(Context context, ArrayList<Review> arrayList,String baseUrl) {

        this.baseUrl = baseUrl;
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder vh, final int position) {

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
                Picasso.get().load(imageUrl).placeholder(R.drawable.user_defalut).into(vh.iv_userImage);
            } else {
                vh.iv_userImage.setImageResource(R.drawable.user_defalut);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }


        //review images
        try {


            if (listModel.getReview_images().size() > 0) {
                vh.rvImages.setVisibility(View.VISIBLE);
                setReviewImages(position, vh.rvImages,listModel);
            } else {
                vh.rvImages.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }


    }


    @Override
    public int getItemCount() {
        if (arrayList.size() > 0) {
            return 1;
        } else {
            return 0;
        }


    }


    public void setReviewImages(int position, RecyclerView rv,Review listModel) {
        ReviewImagesAdapter reviewImagesAdapter = new ReviewImagesAdapter(context, listModel.getReview_images(),baseUrl);
        LinearLayoutManager linearLayoutManager = new
                GridLayoutManager(context, 3);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(reviewImagesAdapter);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /*
     * Initialize universal image loader
     * */




    /*
     * Interface to set item click
     * */

    /*
     * Interface to set item remove
     * */


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        com.dapl.halaladvisor.extra.CircleImageView iv_userImage;
        TextView tv_name, tv_timeOfPost, tv_postDetails;
        RatingBar review_ratingBar;
        RecyclerView rvImages;

        MyViewHolder(View rowview) {
            super(rowview);

            iv_userImage = rowview.findViewById(R.id.iv_review_row_userImage);
            tv_name = rowview.findViewById(R.id.tv_review_row_username);
            tv_postDetails = rowview.findViewById(R.id.tv_review_row_brief);
            tv_timeOfPost = rowview.findViewById(R.id.tv_review_row_time);
            review_ratingBar = rowview.findViewById(R.id.review_row_ratingbar);
            rvImages = rowview.findViewById(R.id.rvImages);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}