package com.therisingtechie.geello.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class CategoryAdapterRecyclerView extends RecyclerView.Adapter<CategoryAdapterRecyclerView.MyViewHolder> {
    private ImageView Images_dis;
    private Context context;
    private List<CategoryDataResponse.Datum> list_AllRestaurnatsData;
   // private LayoutInflater inflater;
    private String TAG =RestaurantsDataResponse.class.getSimpleName() ;

    public CategoryAdapterRecyclerView(Context context, List<CategoryDataResponse.Datum> alldatas) {
        this.context = context;
        this.list_AllRestaurnatsData = alldatas;
        //this.inflater = LayoutInflater.from(context);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView tvCategoryTitle;

        private final ImageView ivRestaurantIcon;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivRestaurantIcon = (ImageView) itemView.findViewById(R.id.iv_category_image);


            tvCategoryTitle = (TextView) itemView.findViewById(R.id.tvCategoryTitle);





        }
    }

    @Override
    public CategoryAdapterRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_single_category, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CategoryDataResponse.Datum RD = list_AllRestaurnatsData.get(position);

        try {

            String image_url =CommonMethods.WEBSITE+"attachments/w600/"+ RD.getIconId();
            Log.d(TAG, "IMAGE URL  : " + image_url);



            Picasso.with(context)
                    .load(image_url)
                    .placeholder(R.color.image_load_color)
                    .error(R.color.image_load_color)
                    .into(holder.ivRestaurantIcon);

          //  holder.ivRestaurantIcon.setBackgroundColor(Color.TRANSPARENT);
            //holder.ivRestaurantIcon.setBackgroundColor(Color.parseColor("#8E8E8E"));



        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.tvCategoryTitle.setText(RD.getName());




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