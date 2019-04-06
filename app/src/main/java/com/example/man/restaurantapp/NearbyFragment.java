package com.example.man.restaurantapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class NearbyFragment extends Fragment {

    static ArrayList<RestaurantDetails> restaurantDetailsArrayList;
    RecyclerView nearbyRecycleView;
    private String location_title;
    private String city_name;
    private String country_name;
    TextView ResNearYouTextView;
    View view;
    ProgressBar nearbyProgressbar;
    LinearLayout textAndRecycleviewLinearLayout;

    public class GetDataOnline extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            // display image.
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
            ImageLoader.getInstance().init(config); // Do it on Application start

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
                JSONObject locationObject = parentObject.getJSONObject("location");
                JSONArray nearbyRestaurant = parentObject.getJSONArray("nearby_restaurants");

                location_title = locationObject.getString("title");
                city_name = locationObject.getString("city_name");
                country_name = locationObject.getString("country_name");


                for (int i = 0; i < nearbyRestaurant.length(); i++) {

                    RestaurantDetails restaurantDetails = new RestaurantDetails();

                    JSONObject object = nearbyRestaurant.getJSONObject(i);
                    JSONObject finalObject = object.getJSONObject("restaurant");

                    restaurantDetails.setRes_id(finalObject.getInt("id"));
                    restaurantDetails.setRes_name(finalObject.getString("name"));
                    restaurantDetails.setRes_url(finalObject.getString("url"));

                    JSONObject location = finalObject.getJSONObject("location");
                    restaurantDetails.setAddress(location.getString("address"));
                    restaurantDetails.setLatitude(location.getDouble("latitude"));
                    restaurantDetails.setLongitude(location.getDouble("longitude"));

                    restaurantDetails.setCuisine(finalObject.getString("cuisines"));
                    restaurantDetails.setAvg_cost_for_two(finalObject.getInt("average_cost_for_two"));
                    restaurantDetails.setCurrency(finalObject.getString("currency"));

                    JSONObject userRating = finalObject.getJSONObject("user_rating");
                    restaurantDetails.setAggregate_rating(userRating.getDouble("aggregate_rating"));
                    restaurantDetails.setRating_text(userRating.getString("rating_text"));
                    restaurantDetails.setRating_color(userRating.getString("rating_color"));
                    restaurantDetails.setVotes_count(userRating.getInt("votes"));

                    restaurantDetails.setImg(finalObject.getString("featured_image"));
                    restaurantDetails.setHas_online_delivery(finalObject.getInt("has_online_delivery"));
                    restaurantDetails.setHas_table_booking(finalObject.getInt("has_table_booking"));

                    restaurantDetailsArrayList.add(restaurantDetails);
                }


                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {

            nearbyProgressbar.setVisibility(View.GONE);
            ResNearYouTextView.setText("Restaurants Near You (" + location_title + ", " + city_name + ", " + country_name + ")");
            NearbyRecycleViewArrayAdapter nearbyRecycleViewArrayAdapter = new NearbyRecycleViewArrayAdapter(getContext(), restaurantDetailsArrayList);
            nearbyRecycleView.setAdapter(nearbyRecycleViewArrayAdapter);
//            nearbyRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.HORIZONTAL,false));
            textAndRecycleviewLinearLayout.setVisibility(View.VISIBLE);
            nearbyRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
            super.onPostExecute(s);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.nearby_layout_fragment, container, false);
        getActivity().setTitle("Nearby Restaurants");

        restaurantDetailsArrayList = new ArrayList<>();

        nearbyRecycleView = view.findViewById(R.id.nearbyRecycleView);
        ResNearYouTextView = view.findViewById(R.id.ResNearYouTextView);
        nearbyProgressbar = view.findViewById(R.id.nearbyProgressbar);
        textAndRecycleviewLinearLayout = view.findViewById(R.id.textAndRecycleviewLinearLayout);


//        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return view;
//        }
//
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//
//        Log.i("OncreateLatitude", latitude+"");
//        Log.i("OncreateLongitude", longitude+"");
//
//        GetDataOnline getDataOnline = new GetDataOnline();
//        getDataOnline.execute("https://developers.zomato.com/api/v2.1/geocode?lat=" + latitude + "&lon=" + longitude);

        return view;
    }


    @Override
    public void onResume() {

        restaurantDetailsArrayList = new ArrayList<>();
        restaurantDetailsArrayList.clear();

        nearbyProgressbar.setVisibility(View.VISIBLE);
        textAndRecycleviewLinearLayout.setVisibility(View.GONE);

        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Log.i("ResumeLatitude", latitude+"");
        Log.i("ResumeLongitude", longitude+"");

        GetDataOnline getDataOnline = new GetDataOnline();
        getDataOnline.execute("https://developers.zomato.com/api/v2.1/geocode?lat="+ latitude +"&lon="+ longitude);

        super.onResume();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
