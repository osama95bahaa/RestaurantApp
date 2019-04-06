package com.example.man.restaurantapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {

    EditText keywordEdittext;
    Button searchButton;
    Spinner sortSpinner;
    Spinner orderSpinner;
    static final String [] sort = {"--","cost" , "rating" , "real_distance"};
    static final String [] order = {"--","ascending" , "descending"};
    String sortString = "";
    String orderString = "";

    TextView orderWordTextview;
    RecyclerView searchResultrecycleview;
    CircleImageView searchCircleImg;
    boolean addLogo;
    CardView searchCardview;
    LinearLayout mainLinearLayout;
    ProgressBar searchProgressbar;


    static ArrayList<RestaurantDetails> restaurantDetailsArrayList;



    public class FetchingData extends AsyncTask<String,Void,String>{
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
                JSONArray parentArray = parentObject.getJSONArray("restaurants");

                for (int i = 0; i < parentArray.length(); i++) {
                    RestaurantDetails restaurantDetails = new RestaurantDetails();

                    JSONObject restaurant = parentArray.getJSONObject(i);
                    JSONObject finalObject = restaurant.getJSONObject("restaurant");

                    JSONObject id = finalObject.getJSONObject("R");
                    restaurantDetails.setRes_id(id.getInt("res_id"));

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

            searchProgressbar.setVisibility(View.GONE);
            searchResultrecycleview.setVisibility(View.VISIBLE);
            SearchRecycleViewAdapter searchRecycleViewAdapter= new SearchRecycleViewAdapter(getContext(), restaurantDetailsArrayList);
            searchResultrecycleview.setAdapter(searchRecycleViewAdapter);
            searchResultrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));

            super.onPostExecute(s);
        }
    }


    @Override
    public void onResume() {
        keywordEdittext.setText("");
        sortSpinner.setSelection(0);
        orderSpinner.setSelection(0);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_layout_fragment, container, false);
        getActivity().setTitle("Search");

        keywordEdittext = view.findViewById(R.id.SearchKeyword);
        searchButton = view.findViewById(R.id.search_button);
        orderSpinner = view.findViewById(R.id.searchOrderspinner);
        sortSpinner = view.findViewById(R.id.searchsortspinner);
        orderWordTextview = view.findViewById(R.id.orderWord);
        searchResultrecycleview = view.findViewById(R.id.searchRecyclerView);

        searchCircleImg = view.findViewById(R.id.searchExpand);
        addLogo = false;
        searchCardview = view.findViewById(R.id.searchCardView);
        mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
        searchProgressbar = view.findViewById(R.id.searchProgressBar);

        restaurantDetailsArrayList = new ArrayList<>();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,sort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                            sortString = "";
                            orderString = "";
                            orderWordTextview.setVisibility(View.GONE);
                            orderSpinner.setVisibility(View.GONE);
                            break;

                    case 1: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                            sortString = adapterView.getSelectedItem().toString();
                            orderWordTextview.setVisibility(View.VISIBLE);
                            orderSpinner.setVisibility(View.VISIBLE);
                            break;

                    case 2: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                            sortString = adapterView.getSelectedItem().toString();
                            orderWordTextview.setVisibility(View.VISIBLE);
                            orderSpinner.setVisibility(View.VISIBLE);
                            break;

                    case 3: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                            sortString = adapterView.getSelectedItem().toString();
                            orderWordTextview.setVisibility(View.VISIBLE);
                            orderSpinner.setVisibility(View.VISIBLE);
                            break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> orderAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,order);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                        orderString = "";
                            break;

                    case 1: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                            orderString = "asc";
                            break;

                    case 2: Log.i("SortitemSelected",adapterView.getSelectedItem().toString());
                            orderString = "desc";
                            break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        searchCircleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addLogo){
                    searchCardview.setVisibility(View.VISIBLE);
                    searchCircleImg.setImageDrawable(getContext().getDrawable(R.drawable.remove_logo));
                    addLogo = false;
                }
                else {
                    searchCardview.setVisibility(View.GONE);
                    searchCircleImg.setImageDrawable(getContext().getDrawable(R.drawable.add_logo));
                    addLogo = true;
                }
            }
        });


        // button clicked.
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchKeyword = keywordEdittext.getText().toString();
                searchKeyword = searchKeyword.trim();

                String searchKeywordUpdated = "";
                for (int i =0; i <searchKeyword.length();i++){
                    if (searchKeyword.charAt(i) == ' '){
                        searchKeywordUpdated = searchKeywordUpdated + "%20";
                    }else {
                        searchKeywordUpdated = searchKeywordUpdated + searchKeyword.charAt(i);
                    }
                }
                Log.i("searchKeyword" , searchKeywordUpdated);
                Log.i("seachKeywordLength" , searchKeywordUpdated.length() + "");
                Log.i("Sort" , sortString);
                Log.i("Order" , orderString);


                FetchingData fetchingData = new FetchingData();

                if (searchKeywordUpdated.equals("")){
                    keywordEdittext.setError("Required");
                }
                else if (sortString.equals("")){
                    // search with keyword only
                    restaurantDetailsArrayList.clear();
                    fetchingData.execute("https://developers.zomato.com/api/v2.1/search?q=" + searchKeywordUpdated +"&lat=40.7421&lon=-74.0048&radius=100");
                }
                else if (orderString.equals("")){
                    // search by keyword and sort
                    restaurantDetailsArrayList.clear();
                    fetchingData.execute("https://developers.zomato.com/api/v2.1/search?q=" + searchKeywordUpdated +"&lat=40.7421&lon=-74.0048&radius=100&sort=" + sortString);
                }
                else {
                    // search by keyword and sort and order
                    restaurantDetailsArrayList.clear();
                    fetchingData.execute("https://developers.zomato.com/api/v2.1/search?q=" + searchKeywordUpdated +"&lat=40.7421&lon=-74.0048&radius=100&sort=" +sortString+ "&order=" + orderString);
                }

                //hide keyboard.
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLinearLayout.getWindowToken(), 0);


                searchProgressbar.setVisibility(View.VISIBLE);
                searchResultrecycleview.setVisibility(View.GONE);
            }
        });

        return view;
    }

}
