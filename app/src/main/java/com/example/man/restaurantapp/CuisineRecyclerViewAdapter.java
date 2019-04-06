package com.example.man.restaurantapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CuisineRecyclerViewAdapter extends RecyclerView.Adapter<CuisineRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<String> cuisineObject = new ArrayList<>();

    public CuisineRecyclerViewAdapter(Context context, ArrayList<String> cuisineObject) {
        this.context = context;
        this.cuisineObject = cuisineObject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cuisine_recycler_view,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cuisineTV.setText(cuisineObject.get(position));
    }

    @Override
    public int getItemCount() {
        return cuisineObject.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cuisineTV;

        public ViewHolder(View itemView) {

            super(itemView);
            cuisineTV = itemView.findViewById(R.id.resDetailsCuisineTextview);

        }
    }
}
