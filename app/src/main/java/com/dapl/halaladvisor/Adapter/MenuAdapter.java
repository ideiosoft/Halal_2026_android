package com.dapl.halaladvisor.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dapl.halaladvisor.Model.getmenures.Menu;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.Utility.Utils;
import com.dapl.halaladvisor.interfaces.RedeemClickInteface;
//import com.shockwave.pdfium.PdfDocument;
//import com.shockwave.pdfium.PdfiumCore;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context context;

    private ArrayList<Menu> arrayList;

    public MenuAdapter(Context context, ArrayList<Menu> arrayList) {

        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_menu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (arrayList.get(position).getMenuName() != null && !arrayList.get(position).getMenuName().isEmpty()){
            holder.tvName.setText(arrayList.get(position).getMenuName().toString());
        }


        holder.llParent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = arrayList.get(position).getMenuLink();
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                        } else {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(browserIntent);
                        }


                    }
                }
        );
/*
        try {

            Glide.with(context).asBitmap()
                    .load(arrayList.get(position).getMenuLink()).into(holder.ivGallery);
           // Utils.imageWithGlide(holder.ivGallery, arrayList.get(position).getMenuLink());
        //    generateImageFromPdf(Uri.parse(arrayList.get(position).getMenuLink()),holder.ivGallery);
        } catch (Exception e) {

        }*/

/*
        holder.tv_row_name.setText(arrayList.get(position).cuisinesName);

        if (arrayList.get(position).cuisineImage != null) {
            Utils.imageWithGlide(holder.ivCatimage, arrayList.get(position).cuisineImage);
        }*/





/*

        holder.clParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemClickInteface.onClick(arrayList.get(position).id.toString() ,arrayList.get(position).cuisinesName.toString());
            }
        });
*/


//        holder.tvNew.setVisibility(View.GONE);

    }

  /*  void generateImageFromPdf(Uri pdfUri, ImageView imageView) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(context);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            // saveImage(bmp);
            imageView.setImageBitmap(bmp);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            //todo with exception
        }
    }
*/
    @Override
    public int getItemCount() {
        return arrayList.size();


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

        private ImageView ivGallery;
        private LinearLayout llParent;
        TextView tvName;


        MyViewHolder(View view) {
            super(view);
            ivGallery = (ImageView) view.findViewById(R.id.ivGallery);
            llParent = (LinearLayout) view.findViewById(R.id.llParent);
            tvName = (TextView) view.findViewById(R.id.tvName);
            view.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            //onDetailsItemClick.onDetailsItemClick(v, getLayoutPosition());
        }


    }
}