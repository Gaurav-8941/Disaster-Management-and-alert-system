package com.example.disastermanagementandalertsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.TimeUnit;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private Integer []images = {
            R.drawable.earthquake, R.drawable.tornado,
            R.drawable.flood,R.drawable.volcano,
            R.drawable.tsunami,R.drawable.drought
    };

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private DisasterDatabaseHelper disasterDatabaseHelper;
    private boolean fromFragment, fromEarthquake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        System.out.println("here ");
        fromEarthquake = getIntent().getBooleanExtra("earthquake",false);
        fromFragment = getIntent().getBooleanExtra("fragment",false);
        System.out.println(fromFragment);
        disasterDatabaseHelper = new DisasterDatabaseHelper(getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Schedule the periodic work from the start
        PeriodicWorkRequest fetchWorkRequest =
                new PeriodicWorkRequest.Builder(Disaster.class, 15, TimeUnit.MINUTES)
                        .setInitialDelay(5, TimeUnit.SECONDS) // Add a small delay for the first run
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "Disaster",
                ExistingPeriodicWorkPolicy.KEEP,
                fetchWorkRequest
        );

        // Observe the work to know when it finishes
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("Disaster")
                .observe(this, workInfos -> {
                    boolean hasFinishedSuccessfully = false;
                    for (WorkInfo workInfo : workInfos) {
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            hasFinishedSuccessfully = true;
                            break;
                        }
                    }
                    if (hasFinishedSuccessfully && mMap != null) {
                        Log.d("MainActivity", "Worker finished, refreshing map.");
                        loadMapData(); // Call the new method to refresh the map
                    }
                });

        // Get the map fragment and start the map loading process.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int i = 0;
        loadMapData();
        /*do {
            loadMapData(); // Initial load of map data
            i++;
        }while (i == 10);*/
    }

    private void loadMapData() {
        if (mMap == null) {
            return;
        }

        mMap.clear(); // Clear old markers before adding new ones
        if (fromFragment)
            onFragmentClick();
        if(fromEarthquake)
            onEarthquakeClick();
    }

    // Add this helper method inside your MainActivity.java
/*
    private int getMarkerIconForDisaster(String disasterType) {
        if (disasterType == null) {
            return R.drawable.ic_marker_default;
        }

        // Using toLowerCase() makes the check case-insensitive
        switch (disasterType.toLowerCase()) {
            case "eq": // Earthquake
                return R.drawable.ic_marker_earthquake;
            case "fl": // Flood
                return R.drawable.ic_marker_flood;
            case "tc": // Tropical Cyclone
                return R.drawable.ic_marker_cyclone;
            case "vo": // Volcano
                return R.drawable.ic_marker_volcano;
            // Add more cases for other types like "WF" (Wildfire) or "DR" (Drought)
            default:
                return R.drawable.ic_marker_default;
        }
    }
*/
    void onFragmentClick(){
        Cursor cursor = disasterDatabaseHelper.getAllDisasters();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                Log.d("MapDebug", "Data found in SQLite");

                // Get the first (newest) disaster's location to center the map
                double newestLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                double newestLon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newestLat, newestLon), 4));

                // Loop through all results to place markers
                do {
                    double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                    double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                    String alertLevel = cursor.getString(cursor.getColumnIndexOrThrow("alertLevel"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String disasterType = cursor.getString(cursor.getColumnIndexOrThrow("disaster_type"));

                    LatLng point = new LatLng(lat, lon);
                    Log.d("MapDebug", "Adding marker at lat=" + lat + ", lon=" + lon + ", type=" + disasterType);
                    String details = description + " - Alert: " + alertLevel;

                    // 1. Get the correct icon resource ID
                    int markerIcon = getMarkerIconForDisaster(disasterType);

                    mMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title(disasterType.toUpperCase())
                                    .snippet(details)
                            .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource(markerIcon))
                    ); // Set the custom icon here!);
                } while (cursor.moveToNext());

            } else {
                Log.d("MapDebug", "No data found in SQLite. Waiting for the worker to run.");
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private int getMarkerIconForDisaster(String disasterType) {
        if(disasterType.equalsIgnoreCase("Earthquake")) {
            return images[0];
        }
        else if (disasterType.equalsIgnoreCase("Tropical Cyclone")) {
            return images[1];
        } else if (disasterType.equalsIgnoreCase("Flood")) {
            return images[2];
        } else if (disasterType.equalsIgnoreCase("Volcano")) {
            return images[3];
        } else if (disasterType.equalsIgnoreCase("Tsunami")) {
            return images[4];
        } else if (disasterType.equalsIgnoreCase("Drought")) {
            return images[5];
        }
        else
            return R.drawable.default_location;
    }

    void onEarthquakeClick(){
        Cursor cursor = disasterDatabaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + "disasters" +
                        " WHERE " + "disaster_type" + " = ?" +
                        " ORDER BY " + "pubDateMillis" + " DESC",
                new String[]{"Earthquake"}
        );
        ;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                Log.d("MapDebug", "Data found in SQLite");

                // Get the first (newest) disaster's location to center the map
                double newestLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                double newestLon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newestLat, newestLon), 4));

                // Loop through all results to place markers
                do {
                    double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                    double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                    String alertLevel = cursor.getString(cursor.getColumnIndexOrThrow("alertLevel"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String disasterType = cursor.getString(cursor.getColumnIndexOrThrow("disaster_type"));

                    LatLng point = new LatLng(lat, lon);
                    String details = description + " - Alert: " + alertLevel;

                    /*// 1. Get the correct icon resource ID
                    int markerIcon = getMarkerIconForDisaster(disasterType);*/

                    mMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title(disasterType.toUpperCase())
                                    .snippet(details)
//                            .icon(com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource(markerIcon))); // Set the custom icon here!
                    );
                } while (cursor.moveToNext());

            } else {
                Log.d("MapDebug", "No data found in SQLite. Waiting for the worker to run.");
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public String[] getCurrentLocation() {

        String[] co = new String[2];
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            co[0] = String.valueOf(location.getLatitude());
                            co[1] = String.valueOf(location.getLongitude());
                        }
                    }
                });
        return co;
    }


}