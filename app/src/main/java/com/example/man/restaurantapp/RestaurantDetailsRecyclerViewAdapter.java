package com.example.man.restaurantapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantDetailsRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantDetailsRecyclerViewAdapter.ViewHolder> {


    Context context;
    ArrayList<ReviewerDetails> reviewerDetails = new ArrayList<>();

    public RestaurantDetailsRecyclerViewAdapter(Context context, ArrayList<ReviewerDetails> reviewerDetails) {
        this.context = context;
        this.reviewerDetails = reviewerDetails;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_details_recyclerview_row,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (reviewerDetails.get(position).getRev_image().equals("")){
            ImageLoader.getInstance().displayImage(String.valueOf(context.getResources().getDrawable(R.drawable.food)),holder.circleImageView);
        }else {
            ImageLoader.getInstance().displayImage(reviewerDetails.get(position).getRev_image(),holder.circleImageView);
        }

        holder.reviewerName.setText(reviewerDetails.get(position).getRev_name());
        holder.reviewerDate.setText(reviewerDetails.get(position).getRev_date());
        holder.reviewerReviewText.setText(reviewerDetails.get(position).getRev_review_text());
        holder.reviewerRatingText.setText(reviewerDetails.get(position).getRev_rating_text());
        holder.ratingBar.setRating((float) reviewerDetails.get(position).getReview_ratingbar());
    }

    @Override
    public int getItemCount() {
        return reviewerDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView reviewerName;
        TextView reviewerDate;
        TextView reviewerRatingText;
        TextView reviewerReviewText;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.reviewerImage);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewerDate = itemView.findViewById(R.id.reviewerDate);
            reviewerRatingText = itemView.findViewById(R.id.reviewerRatingText);
            reviewerReviewText = itemView.findViewById(R.id.reviewText);
            ratingBar = itemView.findViewById(R.id.reviewerRatingBar);
        }
    }
}
