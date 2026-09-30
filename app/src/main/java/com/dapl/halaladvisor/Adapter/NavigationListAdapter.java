package com.dapl.halaladvisor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.application_data.AppData;

/**
 * Created by DAT-Asset-128 on 12-09-2016.
 */
public class NavigationListAdapter extends BaseAdapter {

    // Keep all Images in array
  /*  public Integer[] imageId = {
            R.drawable.menu_takeaway, R.drawable.menu_eatout, R.drawable.menu_delivery, 0, R.drawable.menu_viewprofile,
            R.drawable.voucher, R.drawable.menu_sendfeedback, R.drawable.menu_invitefriends,
            R.drawable.menu_rateus, R.drawable.menu_logout};*/

/*    public Integer[] imageId = {
            R.drawable.home, R.drawable.heart, R.drawable.ic_delivery, R.drawable.ic_offers,
            R.drawable.ic_share, R.drawable.ic_rate_us, R.drawable.ic_profile, R.drawable.ic_profile};  */


    public Integer[] imageId = {
            R.drawable.home, R.drawable.ic_new_scan, R.drawable.heart, R.drawable.user,R.drawable.offers, R.drawable.email,
            R.drawable.star, R.drawable.speaker, R.drawable.support, R.drawable.logout};


    public String[] nameId;
    //  ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Context ctx;
    private LayoutInflater inflater;

    private ViewHolder holder;
    private int VIEWTYPE_ONE = 1;
    private int VIEWTYPE_TWO = 2;

    public NavigationListAdapter(Context context, String[] nameId) {
        this.ctx = context;
        this.nameId = nameId;

        System.out.println("dcdsv===" + nameId.length);
//        this.rItems = restItems;
//        System.out.println("List_row: " +this.rItems.size());
    }

    @Override
    public int getCount() {
        return imageId.length;
    }

    @Override
    public Object getItem(int position) {
        return imageId[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //   int type = getItemViewType(position);

        // if (type == VIEWTYPE_ONE) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.navigation_listview_row, null);
            holder = new ViewHolder();

            holder.iv_rowIcon = convertView.findViewById(R.id.iv_nav_list_row);
            holder.tv_rowName = convertView.findViewById(R.id.tv_nav_list_row);
            holder.iv_rightArrow = convertView.findViewById(R.id.iv_right_arrow);
            holder.separator_line = convertView.findViewById(R.id.separator_view);

          //  AppData.changefonts(holder.tv_rowName, ctx, "regular.ttf");

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        System.out.println("cdvfbgnh==" + position);

        holder.iv_rowIcon.setImageResource(imageId[position]);
        holder.tv_rowName.setText(nameId[position]);
        holder.separator_line.setVisibility(View.GONE);

        /*
         * For grey bold divider in navigation listview
         * */
            /*if (position == 2){
                holder.separator_line.setVisibility(View.VISIBLE);
            }
            else {
                holder.separator_line.setVisibility(View.GONE);
            }*/

        /*
         * For right arrow in every row in navigation listview
         * */
           /* if (position < 3) {
                holder.iv_rightArrow.setVisibility(View.VISIBLE);
                holder.iv_rightArrow.setImageResource(R.drawable.menu_green_arrow);
            } else {
                holder.iv_rightArrow.setImageResource(R.drawable.menu_grey_arrow);
            }*/

        /*
         * For setting right arrow visibility gone (logout row) in navigation listview
         * */
/*            if (position == nameId.length - 1) {
                holder.iv_rightArrow.setVisibility(View.INVISIBLE);
            } else {
                //else visible
            }*/
       /* } else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.navigation_row_specialoffer, null);
                holder = new ViewHolder();

                holder.iv_rowIcon_specialOffer = (ImageView) convertView.findViewById(R.id.iv_list_row_specialoffer);
                holder.tv_rowName_specialOffer = (TextView) convertView.findViewById(R.id.tv_list_row_specialoffer);

                AppData.changefonts(holder.tv_rowName_specialOffer, ctx, "regular.ttf");

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            holder.tv_rowName_specialOffer.setText(nameId[position]);
        }*/


        return convertView;
    }


    private class ViewHolder {
        ImageView iv_rowIcon, iv_rightArrow, iv_rowIcon_specialOffer;
        TextView tv_rowName, tv_rowName_specialOffer;
        View separator_line;
    }
}
