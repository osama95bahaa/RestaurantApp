package com.example.man.restaurantapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private int res_id;
    private String res_name;
    private String res_url;
    private String address;
    private double latitude;
    private double longitude;
    private String cuisine;
    private int avg_cost_for_two;
    private String currency;
    private double aggregate_rating;
    private String rating_text;
    private String rating_color;
    private int votes_count;
    private String img;
    private int has_online_delivery;
    private int has_table_booking;

    ArrayList<ReviewerDetails> reviewerDetailsArrayList = new ArrayList<>();


    ImageView resDetailsImage;
    TextView resDetailsName;
    TextView resDetailsRatingNumber;
    TextView resDetailsVotes;
    RecyclerView cuisineRecyclerview;
    TextView resDetailsAvgCostAndCurrency;
    TextView resDetailsOnlineDelivery;
    ImageView resDetailsOnlineDelImage;
    TextView resDetailsTableBooking;
    ImageView resDetailsTableImage;
    RecyclerView resDetailsRecycleview;
    TextView resDetailsAddress;
    Button b;
    CircleImageView circleImageView;
    boolean addLogo;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;





    public class DownloadData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept", " application/json");
                urlConnection.setRequestProperty("user-key", "04e2d16199d44bb78358294da5d60fb4");
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                JSONObject parentObject = new JSONObject(result);
                JSONArray user_reviewsArray = parentObject.getJSONArray("user_reviews");

                for (int i =0; i <user_reviewsArray.length();i++){

                    ReviewerDetails reviewerDetails = new ReviewerDetails();

                    JSONObject object = user_reviewsArray.getJSONObject(i);
                    JSONObject finalObject = object.getJSONObject("review");

                    reviewerDetails.setReview_ratingbar(finalObject.getDouble("rating"));
                    reviewerDetails.setRev_review_text(finalObject.getString("review_text"));
                    reviewerDetails.setRev_date(finalObject.getString("review_time_friendly"));
                    reviewerDetails.setRev_rating_text(finalObject.getString("rating_text"));

                    JSONObject finalObject1 = finalObject.getJSONObject("user");

                    reviewerDetails.setRev_name(finalObject1.getString("name"));
                    reviewerDetails.setRev_image(finalObject1.getString("profile_image"));

                    reviewerDetailsArrayList.add(reviewerDetails);
                }

                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

            if (img.equals("")){
                ImageLoader.getInstance().displayImage(String.valueOf(getApplicationContext().getResources().getDrawable(R.drawable.food)),resDetailsImage);
            }else {
                ImageLoader.getInstance().displayImage(img,resDetailsImage);
            }

//            ImageLoader.getInstance().displayImage(img,resDetailsImage);
            resDetailsName.setText(res_name);

            ArrayList<String> cuisineObject = new ArrayList<String>(Arrays.asList(cuisine.split(", ")));
            CuisineRecyclerViewAdapter cuisineRecyclerViewAdapter = new CuisineRecyclerViewAdapter(getApplicationContext(), cuisineObject);
            cuisineRecyclerview.setAdapter(cuisineRecyclerViewAdapter);
            cuisineRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));


            resDetailsAvgCostAndCurrency.setText(avg_cost_for_two + " "+ currency + " (Approx. for 2 persons)");
            resDetailsOnlineDelivery.setText("Has Online Delivery:  " );
            resDetailsTableBooking.setText("Has Table Booking:   " );

            if (has_online_delivery == 0){
                resDetailsOnlineDelImage.setImageDrawable(getApplicationContext().getDrawable(R.drawable.clear));
            }
            else {
                resDetailsOnlineDelImage.setImageDrawable(getApplicationContext().getDrawable(R.drawable.tick));
            }
            if (has_table_booking == 0){
                resDetailsTableImage.setImageDrawable(getApplicationContext().getDrawable(R.drawable.clear));

            }
            else {
                resDetailsTableImage.setImageDrawable(getApplicationContext().getDrawable(R.drawable.tick));
            }

            resDetailsRatingNumber.setText(aggregate_rating + "/5");

            int colorInteger = Color.parseColor("#"+rating_color);
            GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]
                    {colorInteger, colorInteger});
            gradient.setShape(GradientDrawable.RECTANGLE);
            gradient.setCornerRadius((float) 50.0);
            resDetailsRatingNumber.setBackgroundDrawable(gradient);

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (addLogo){
                        resDetailsRecycleview.setVisibility(View.VISIBLE);
                        circleImageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.remove_logo));
                        addLogo = false;
                    }
                    else {
                        resDetailsRecycleview.setVisibility(View.GONE);
                        circleImageView.setImageDrawable(getApplicationContext().getDrawable(R.drawable.add_logo));
                        addLogo = true;
                    }
                }
            });



            resDetailsVotes.setText(votes_count + " Votes");
            resDetailsAddress.setText(address);

            RestaurantDetailsRecyclerViewAdapter restaurantDetailsRecyclerViewAdapter = new RestaurantDetailsRecyclerViewAdapter(getApplicationContext(),reviewerDetailsArrayList);
            resDetailsRecycleview.setAdapter(restaurantDetailsRecyclerViewAdapter);
            resDetailsRecycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            super.onPostExecute(s);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("position" , 0);
        String base = intent.getStringExtra("class");


        resDetailsImage = findViewById(R.id.resDetailsImage);
        resDetailsName = findViewById(R.id.resDetailsName);
        resDetailsRatingNumber = findViewById(R.id.resDetailsRatingNumber);
        resDetailsVotes = findViewById(R.id.resDetailsVotes);
        cuisineRecyclerview = findViewById(R.id.cuisineRecyclerView);
        resDetailsAvgCostAndCurrency = findViewById(R.id.resDetailsAvgCostAndCurrency);
        resDetailsOnlineDelivery = findViewById(R.id.resDetailsOnlineDelivery);
        resDetailsTableBooking = findViewById(R.id.resDetailsTableBooking);
        resDetailsRecycleview = findViewById(R.id.resDetailsRecyclerview);
        resDetailsAddress = findViewById(R.id.resDetailsAddress);
        resDetailsOnlineDelImage = findViewById(R.id.onlineDeliveryImageView);
        resDetailsTableImage = findViewById(R.id.tableBookingImageView);
        circleImageView = findViewById(R.id.circleImageViewreview);
        addLogo = true;

        progressBar = findViewById(R.id.resDetailsProgressbar);
        progressBar.setVisibility(View.VISIBLE);
        relativeLayout = findViewById(R.id.allDetailsRelLayout);
        relativeLayout.setVisibility(View.GONE);

        if (base.equals("nearby")){
            RestaurantDetails nearbyRestaurantDetails = NearbyFragment.restaurantDetailsArrayList.get(pos);

            res_id = nearbyRestaurantDetails.getRes_id();
            res_name = nearbyRestaurantDetails.getRes_name();
            res_url = nearbyRestaurantDetails.getRes_url();
            address = nearbyRestaurantDetails.getAddress();
            latitude = nearbyRestaurantDetails.getLatitude();
            longitude = nearbyRestaurantDetails.getLongitude();
            cuisine = nearbyRestaurantDetails.getCuisine();
            avg_cost_for_two = nearbyRestaurantDetails.getAvg_cost_for_two();
            currency = nearbyRestaurantDetails.getCurrency();
            aggregate_rating = nearbyRestaurantDetails.getAggregate_rating();
            rating_text = nearbyRestaurantDetails.getRating_text();
            rating_color = nearbyRestaurantDetails.getRating_color();
            votes_count = nearbyRestaurantDetails.getVotes_count();
            img = nearbyRestaurantDetails.getImg();
            has_online_delivery = nearbyRestaurantDetails.getHas_online_delivery();
            has_table_booking = nearbyRestaurantDetails.getHas_table_booking();
        }
        else if (base.equals("search")){
            RestaurantDetails searchRestaurantDetails = SearchFragment.restaurantDetailsArrayList.get(pos);

            res_id = searchRestaurantDetails.getRes_id();
            res_name = searchRestaurantDetails.getRes_name();
            res_url = searchRestaurantDetails.getRes_url();
            address = searchRestaurantDetails.getAddress();
            latitude = searchRestaurantDetails.getLatitude();
            longitude = searchRestaurantDetails.getLongitude();
            cuisine = searchRestaurantDetails.getCuisine();
            avg_cost_for_two = searchRestaurantDetails.getAvg_cost_for_two();
            currency = searchRestaurantDetails.getCurrency();
            aggregate_rating = searchRestaurantDetails.getAggregate_rating();
            rating_text = searchRestaurantDetails.getRating_text();
            rating_color = searchRestaurantDetails.getRating_color();
            votes_count = searchRestaurantDetails.getVotes_count();
            img = searchRestaurantDetails.getImg();
            has_online_delivery = searchRestaurantDetails.getHas_online_delivery();
            has_table_booking = searchRestaurantDetails.getHas_table_booking();
        }


        DownloadData downloadData = new DownloadData();
        downloadData.execute("https://developers.zomato.com/api/v2.1/reviews?res_id=" +res_id);

        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),RestaurantLocationMapActivity.class);
                i.putExtra("lat" , latitude);
                i.putExtra("lon" , longitude);
                i.putExtra("resname",res_name);
                startActivity(i);
            }
        });

    }
}
