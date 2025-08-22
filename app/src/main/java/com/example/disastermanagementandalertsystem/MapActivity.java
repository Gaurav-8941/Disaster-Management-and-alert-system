package com.example.disastermanagementandalertsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

        fromEarthquake = getIntent().getBooleanExtra("earthquake",false);
        fromFragment = getIntent().getBooleanExtra("fragment",false);

        disasterDatabaseHelper = new DisasterDatabaseHelper(getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Schedule background work
        PeriodicWorkRequest fetchWorkRequest =
                new PeriodicWorkRequest.Builder(Disaster.class, 15, TimeUnit.MINUTES)
                        .setInitialDelay(5, TimeUnit.SECONDS)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "Disaster",
                ExistingPeriodicWorkPolicy.KEEP,
                fetchWorkRequest
        );

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
                        loadMapData();
                    }
                });

        // Load the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMapData();
    }

    private void loadMapData() {
        if (mMap == null) return;

        mMap.clear(); // Clear old markers
        if (fromFragment) onFragmentClick();
        if (fromEarthquake) onEarthquakeClick();
    }

    // Convert VectorDrawable or PNG into BitmapDescriptor
    private BitmapDescriptor bitmapFromDrawable(int drawableId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        if (drawable == null) return null;

        drawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Return correct icon for disaster type
    private BitmapDescriptor getMarkerIconForDisaster(String disasterType) {
        if (disasterType == null) {
            return bitmapFromDrawable(R.drawable.default_location, 80, 80);
        }

        if (disasterType.equalsIgnoreCase("Earthquake")) {
            return bitmapFromDrawable(images[0], 80, 80);
        } else if (disasterType.equalsIgnoreCase("Tropical Cyclone")) {
            return bitmapFromDrawable(images[1], 80, 80);
        } else if (disasterType.equalsIgnoreCase("Flood")) {
            return bitmapFromDrawable(images[2], 80, 80);
        } else if (disasterType.equalsIgnoreCase("Volcano")) {
            return bitmapFromDrawable(images[3], 80, 80);
        } else if (disasterType.equalsIgnoreCase("Tsunami")) {
            return bitmapFromDrawable(images[4], 80, 80);
        } else if (disasterType.equalsIgnoreCase("Drought")) {
            return bitmapFromDrawable(images[5], 80, 80);
        } else {
            return bitmapFromDrawable(R.drawable.default_location, 100, 100);
        }
    }

    void onFragmentClick(){
        Cursor cursor = disasterDatabaseHelper.getAllDisasters();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                double newestLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                double newestLon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newestLat, newestLon), 4));

                do {
                    double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                    double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                    String alertLevel = cursor.getString(cursor.getColumnIndexOrThrow("alertLevel"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String disasterType = cursor.getString(cursor.getColumnIndexOrThrow("disaster_type"));

                    LatLng point = new LatLng(lat, lon);
                    String details = description + " - Alert: " + alertLevel;

                    BitmapDescriptor markerIcon = getMarkerIconForDisaster(disasterType);

                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(disasterType.toUpperCase())
                            .snippet(details)
                            .icon(markerIcon));
                } while (cursor.moveToNext());

            } else {
                Log.d("MapDebug", "No data found in SQLite.");
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
    }

    void onEarthquakeClick(){
        Cursor cursor = disasterDatabaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM disasters WHERE disaster_type = ? ORDER BY pubDateMillis DESC",
                new String[]{"Earthquake"}
        );

        try {
            if (cursor != null && cursor.moveToFirst()) {
                double newestLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                double newestLon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newestLat, newestLon), 4));

                do {
                    double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                    double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                    String alertLevel = cursor.getString(cursor.getColumnIndexOrThrow("alertLevel"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    String disasterType = cursor.getString(cursor.getColumnIndexOrThrow("disaster_type"));

                    LatLng point = new LatLng(lat, lon);
                    String details = description + " - Alert: " + alertLevel;

                    BitmapDescriptor markerIcon = getMarkerIconForDisaster(disasterType);

                    mMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(disasterType.toUpperCase())
                            .snippet(details)
                            .icon(markerIcon));
                } while (cursor.moveToNext());

            } else {
                Log.d("MapDebug", "No earthquake data found in SQLite.");
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
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
