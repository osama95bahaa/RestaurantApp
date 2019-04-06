package com.example.man.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;

public class NearbyRecycleViewArrayAdapter extends RecyclerView.Adapter<NearbyRecycleViewArrayAdapter.ViewHolder>{

    Context context;
    ArrayList<RestaurantDetails> restaurantDetails = new ArrayList<>();

    public NearbyRecycleViewArrayAdapter(Context context, ArrayList<RestaurantDetails> restaurantDetails) {

        this.context = context;
        this.restaurantDetails = restaurantDetails;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_layout_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (restaurantDetails.get(position).getImg().equals("")){
            ImageLoader.getInstance().displayImage(String.valueOf(context.getResources().getDrawable(R.drawable.food)),holder.nearby_image);
        }else {
            ImageLoader.getInstance().displayImage(restaurantDetails.get(position).getImg(),holder.nearby_image);
        }

        int colorInteger = Color.parseColor("#"+restaurantDetails.get(position).getRating_color());
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]
                {colorInteger, colorInteger});
        gradient.setShape(GradientDrawable.RECTANGLE);
        gradient.setCornerRadius((float) 50.0);
        holder.nearby_rating.setBackgroundDrawable(gradient);

        holder.nearby_name.setText(restaurantDetails.get(position).getRes_name());
//        holder.nearby_cuisine.setText(restaurantDetails.get(position).getCuisine());
        holder.nearby_rating.setText(restaurantDetails.get(position).getAggregate_rating() + "/5");

        holder.nearbyCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"you pressed on " + restaurantDetails.get(position).getRes_name(),Toast.LENGTH_SHORT).show();
                RestaurantDetails resDet = restaurantDetails.get(position);
                Intent intent = new Intent(context,RestaurantDetailsActivity.class);

//                intent.putExtra("res_id",resDet.getRes_id());
//                intent.putExtra("res_name", resDet.getRes_name());
//                intent.putExtra("res_url" , resDet.getRes_url());
//                intent.putExtra("address" , resDet.getAddress());
//                intent.putExtra("latitude" , resDet.getLatitude());
//                intent.putExtra("longitude" , resDet.getLongitude());
//                intent.putExtra("cuisine" , resDet.getCuisine());
//                intent.putExtra("avg_cost_for_two" , resDet.getAvg_cost_for_two());
//                intent.putExtra("currency" , resDet.getCurrency());
//                intent.putExtra("agg_rating" , resDet.getAggregate_rating());
//                intent.putExtra("rating_text" , resDet.getRating_text());
//                intent.putExtra("rating_color" , resDet.getRating_color());
//                intent.putExtra("votes" , resDet.getVotes_count());
//                intent.putExtra("image" , resDet.getImg());
//                intent.putExtra("online_delivery" , resDet.getHas_online_delivery());
//                intent.putExtra("table_booking" , resDet.getHas_table_booking());
                intent.putExtra("class" , "nearby");
                intent.putExtra("position" , position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView nearby_image;
        TextView nearby_name;
        TextView nearby_cuisine;
        TextView nearby_rating;
        CardView nearbyCardview;


        public ViewHolder(View itemView) {
            super(itemView);
            nearby_image = itemView.findViewById(R.id.nearbyImage);
            nearby_name = itemView.findViewById(R.id.nearbyName);
//            nearby_cuisine = itemView.findViewById(R.id.nearbyCuisine);
            nearby_rating = itemView.findViewById(R.id.nearbyRatingTextView);
            nearbyCardview = itemView.findViewById(R.id.nearbyCardview);
        }
    }

}
