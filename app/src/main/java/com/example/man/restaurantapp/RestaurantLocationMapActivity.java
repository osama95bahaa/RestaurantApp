package com.example.man.restaurantapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class RestaurantLocationMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Intent intent;
    double longitude;
    double latitude;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_location_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = findViewById(R.id.googleMapsButton);

        intent = getIntent();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

    }


    public void showRoute(View v){
        Uri.Builder directionsBuilder = new Uri.Builder()
                .scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", intent.getDoubleExtra("lat" ,0) + "," + intent.getDoubleExtra("lon" ,0));

        startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng resLoc = new LatLng(intent.getDoubleExtra("lat" ,0), intent.getDoubleExtra("lon",0));
        mMap.addMarker(new MarkerOptions().position(resLoc).title(intent.getStringExtra("resname")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(resLoc,15));

//        LatLng myLoc = new LatLng(latitude,longitude);
//        mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(resLoc,15));

//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(myLoc);
//        builder.include(resLoc);
//        LatLngBounds bounds = builder.build();

//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
