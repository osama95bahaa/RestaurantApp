package com.example.man.restaurantapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class SearchRecycleViewAdapter extends RecyclerView.Adapter<SearchRecycleViewAdapter.ViewHolder> {

    Context context;
    ArrayList<RestaurantDetails> restaurantDetails = new ArrayList<>();


    public SearchRecycleViewAdapter(Context context, ArrayList<RestaurantDetails> restaurantDetails) {
        this.context = context;
        this.restaurantDetails = restaurantDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycleview_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (restaurantDetails.get(position).getImg().equals("")){
            ImageLoader.getInstance().displayImage(String.valueOf(context.getResources().getDrawable(R.drawable.food)),holder.search_image);
        }else {
            ImageLoader.getInstance().displayImage(restaurantDetails.get(position).getImg(),holder.search_image);
        }

        int colorInteger = Color.parseColor("#"+restaurantDetails.get(position).getRating_color());
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]
                {colorInteger, colorInteger});
        gradient.setShape(GradientDrawable.RECTANGLE);
        gradient.setCornerRadius((float) 50.0);
        holder.search_rating.setBackgroundDrawable(gradient);

        holder.search_name.setText(restaurantDetails.get(position).getRes_name());
        holder.search_rating.setText(restaurantDetails.get(position).getAggregate_rating() + "/5");
        holder.search_cuisine.setText(restaurantDetails.get(position).getCuisine());
        holder.search_address.setText(restaurantDetails.get(position).getAddress());

        holder.searchCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"you pressed on " + restaurantDetails.get(position).getRes_name(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,RestaurantDetailsActivity.class);
                intent.putExtra("class" , "search");
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

        ImageView search_image;
        TextView search_name;
        TextView search_cuisine;
        TextView search_rating;
        TextView search_address;
        CardView searchCardview;


        public ViewHolder(View itemView) {
            super(itemView);
            search_image = itemView.findViewById(R.id.searchImageview);
            search_name = itemView.findViewById(R.id.searchNameTextview);
            search_cuisine = itemView.findViewById(R.id.searchCuisineTextview);
            search_rating = itemView.findViewById(R.id.searchRating);
            search_address = itemView.findViewById(R.id.searchaddressTextview);
            searchCardview = itemView.findViewById(R.id.searchCardView);
        }
    }

}
