package com.example.man.restaurantapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class TrendingRecycleViewArrayAdapter extends RecyclerView.Adapter<TrendingRecycleViewArrayAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> imageurlArraylist = new ArrayList<>();
    private ArrayList<String> titleArraylist = new ArrayList<>();
    private ArrayList<String> descriptionArraylist = new ArrayList<>();


    public TrendingRecycleViewArrayAdapter(Context context, ArrayList<String> imageurlArraylist, ArrayList<String> titleArraylist, ArrayList<String> descriptionArraylist) {
        this.context = context;
        this.imageurlArraylist = imageurlArraylist;
        this.titleArraylist = titleArraylist;
        this.descriptionArraylist = descriptionArraylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_layout_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Glide.with(context).load(imageurlArraylist.get(position)).into(holder.imageView);
        ImageLoader.getInstance().displayImage(imageurlArraylist.get(position), holder.imageView);
        holder.titleTextview.setText(titleArraylist.get(position));
        holder.descriptionTextview.setText(descriptionArraylist.get(position));
    }

    @Override
    public int getItemCount() {
        return titleArraylist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextview;
        TextView descriptionTextview;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trendingImage);
            titleTextview = itemView.findViewById(R.id.titleTextView);
            descriptionTextview = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
