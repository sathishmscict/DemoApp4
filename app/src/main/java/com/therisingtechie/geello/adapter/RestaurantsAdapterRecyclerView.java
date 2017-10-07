package com.therisingtechie.geello.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.therisingtechie.geello.R;
import com.therisingtechie.geello.helper.CommonMethods;
import com.therisingtechie.geello.model.CategoryDataResponse;
import com.therisingtechie.geello.model.RestaurantsDataResponse;

import java.util.List;

/**
 * Created by pc on 7/22/2017.
 */

public class RestaurantsAdapterRecyclerView extends RecyclerView.Adapter<RestaurantsAdapterRecyclerView.MyViewHolder> {
    private final String layoutType;
    private ImageView Images_dis;
    private Context context;
    private List<RestaurantsDataResponse.Datum> list_AllRestaurnatsData;
   // private LayoutInflater inflater;
    private String TAG =RestaurantsDataResponse.class.getSimpleName() ;

    public RestaurantsAdapterRecyclerView(Context context, List<RestaurantsDataResponse.Datum> alldatas,String layoutType) {
        this.context = context;
        this.list_AllRestaurnatsData = alldatas;
        //this.inflater = LayoutInflater.from(context);
        this.layoutType = layoutType;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvRestaurantName, tvRestaurantMenuReference;

        private final ImageView iv_restaurnat_main_image,ivRestaurantIcon,ivVeg,ivNonVeg,ivJain;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivRestaurantIcon = (ImageView) itemView.findViewById(R.id.ivRestaurantIcon);
            iv_restaurnat_main_image = (ImageView) itemView.findViewById(R.id.iv_restaurnat_main_image);
            ivVeg = (ImageView) itemView.findViewById(R.id.ivVeg);
            ivNonVeg = (ImageView) itemView.findViewById(R.id.ivNonVeg);
            ivJain = (ImageView) itemView.findViewById(R.id.ivJain);

            tvRestaurantName = (TextView) itemView.findViewById(R.id.tvRestaurantName);
            tvRestaurantMenuReference = (TextView) itemView.findViewById(R.id.tvRestaurantMenuReference);




        }
    }

    @Override
    public RestaurantsAdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        try {
            if(layoutType.toLowerCase().equals("popular"))
            {
                view = LayoutInflater.from(context).inflate(R.layout.row_single_restaurant_popular, parent, false);
            }
            else
            {
                view = LayoutInflater.from(context).inflate(R.layout.row_single_restaurant, parent, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final RestaurantsDataResponse.Datum RD = list_AllRestaurnatsData.get(position);

        try {
           /* Log.d(TAG, "IMAGE URL  : " + CommonMethods.WEBSITE+ RD.getIcon());
           // Glide.with(context).load(MD.getAvatar()).error(R.mipmap.ic_launcher).into(holder.ivProfilePic);
            Picasso.with(context)
                    .load(CommonMethods.WEBSITE+ RD.getIcon())
                    .placeholder(R.drawable.icon_india)
                    .error(R.drawable.icon_india)
                    .into(holder.ivVeg);

            Log.d(TAG, "IMAGE URL  : " + CommonMethods.WEBSITE+ RD.getIcon());
            // Glide.with(context).load(MD.getAvatar()).error(R.mipmap.ic_launcher).into(holder.ivProfilePic);
            Picasso.with(context)
                    .load(CommonMethods.WEBSITE+ RD.getIcon())
                    .placeholder(R.drawable.icon_india)
                    .error(R.drawable.icon_india)
                    .into(holder.ivVeg);
            Log.d(TAG, "IMAGE URL  : " + CommonMethods.WEBSITE+ RD.getIcon());
            // Glide.with(context).load(MD.getAvatar()).error(R.mipmap.ic_launcher).into(holder.ivProfilePic);
            Picasso.with(context)
                    .load(CommonMethods.WEBSITE+ RD.getIcon())
                    .placeholder(R.drawable.icon_india)
                    .error(R.drawable.icon_india)
                    .into(holder.ivVeg);*/

            Picasso.with(context)
                    .load(CommonMethods.WEBSITE+ RD.getImage())
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(holder.ivRestaurantIcon);

            Picasso.with(context)
                    .load(CommonMethods.WEBSITE+ RD.getImage())
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(holder.iv_restaurnat_main_image);



        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.tvRestaurantName.setText(RD.getName());
        holder.tvRestaurantMenuReference.setText(RD.getDescription());



    }

    @Override
    public int getItemCount() {
        return list_AllRestaurnatsData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}