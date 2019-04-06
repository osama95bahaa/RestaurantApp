package com.example.man.restaurantapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

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

public class TrendingFragment extends Fragment {

    private ArrayList<String> imageurlArraylist = new ArrayList<>();
    private ArrayList<String> titleArraylist = new ArrayList<>();
    private ArrayList<String> descriptionArraylist = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    public class FetchData extends AsyncTask<String,Void,String>{

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
                JSONArray parentArray = parentObject.getJSONArray("collections");

                for (int i =0; i< parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    JSONObject final1Object = finalObject.getJSONObject("collection");
                    imageurlArraylist.add(final1Object.getString("image_url"));
                    titleArraylist.add(final1Object.getString("title"));
                    descriptionArraylist.add(final1Object.getString("description"));
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
//            Log.i("ImageUrl" , imageurlArraylist.toString());
//            Log.i("Title" , titleArraylist.toString());
//            Log.i("Description" , descriptionArraylist.toString());

            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            TrendingRecycleViewArrayAdapter adapter = new TrendingRecycleViewArrayAdapter(getContext(),imageurlArraylist,titleArraylist,descriptionArraylist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            super.onPostExecute(s);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.trending_layout_fragment, container, false);
        getActivity().setTitle("Trending Restaurants");

        imageurlArraylist.clear();
        titleArraylist.clear();
        descriptionArraylist.clear();

        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.trendingRecycleView);


        FetchData fetchData = new FetchData();
        fetchData.execute("https://developers.zomato.com/api/v2.1/collections?city_id=280");

        return view;
    }
}
