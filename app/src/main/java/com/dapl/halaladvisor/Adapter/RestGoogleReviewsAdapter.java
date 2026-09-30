package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dapl.halaladvisor.Model.getgooglereviews.Review;
import com.dapl.halaladvisor.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RestGoogleReviewsAdapter extends RecyclerView.Adapter<RestGoogleReviewsAdapter.MyViewHolder> {

    Context context;

    private ArrayList<Review> arrayList;


    public RestGoogleReviewsAdapter(Context context, ArrayList<Review> arrayList) {
        this.context = context;
        this.arrayList = arrayList;


    }

   /* public void setReviewImages(RecyclerView rv, ArrayList<String> arrayList) {
        ReviewImagesAdapter reviewImagesAdapter = new ReviewImagesAdapter(context, arrayList, baseUrl);
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(reviewImagesAdapter);
    }*/

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row_new, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder vh, final int position) {


        final Review listModel = (Review) arrayList.get(position);
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
            String userName = listModel.getAuthorName();
            vh.tv_name.setText(userName);
        } catch (Exception e) {
            // TODO: handle exception
        }

        /*Setting user review details*/
        try {
            String review_details = listModel.getText();
//                vh.tv_postDetails.setText(R.string.dummy_text);
            vh.tv_postDetails.setText(review_details);
        } catch (Exception e) {
            // TODO: handle exception
        }

        /*Setting user time of post*/
        /*Setting user time of post*/
        try {
            long timeOfPost = Long.parseLong(listModel.getTime());
            Log.e("ReviewAdapter: ", "Date of post- " + timeOfPost);
            vh.tv_timeOfPost.setText(getDateFromTimeStamp(timeOfPost));
        } catch (Exception e) {
            // TODO: handle exception
        }

        /*Setting user image*/
        try {
            String imageUrl = listModel.getProfilePhotoUrl();

            if (imageUrl.length() > 1) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.user_defalut).into(vh.iv_userImage);
            } else {
                vh.iv_userImage.setImageResource(R.drawable.user_defalut);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }


      /*  //review images
        try {


            if (listModel.getImages().size() > 0) {
                vh.rvImages.setVisibility(View.VISIBLE);
                setReviewImages(vh.rvImages, listModel.getImages());
            } else {
                vh.rvImages.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
*/

    }


    private String getDateFromTimeStamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
      if (arrayList.size()>0){
          return 1;
      }else {
          return arrayList.size();
      }


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


        RecyclerView rvImages;
        com.dapl.halaladvisor.extra.CircleImageView iv_userImage;
        TextView tv_name, tv_timeOfPost, tv_postDetails;
        RatingBar review_ratingBar;

        MyViewHolder(View view) {
            super(view);
            iv_userImage = view.findViewById(R.id.iv_review_row_userImage);
            rvImages = view.findViewById(R.id.rvImages);
            tv_name = view.findViewById(R.id.tv_review_row_username);
            tv_postDetails = view.findViewById(R.id.tv_review_row_brief);
            tv_timeOfPost = view.findViewById(R.id.tv_review_row_time);
            review_ratingBar = view.findViewById(R.id.review_row_ratingbar);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}