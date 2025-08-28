package com.example.disastermanagementandalertsystem;

import android.Manifest;
import android.content.Intent;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private DisasterDatabaseHelper disasterDatabaseHelper;
    private boolean fromFragment, fromEarthquake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fromEarthquake = getIntent().getBooleanExtra("earthquake", false);
        fromFragment = getIntent().getBooleanExtra("fragment", false);

        disasterDatabaseHelper = new DisasterDatabaseHelper(getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Schedule periodic worker
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
                    for (WorkInfo workInfo : workInfos) {
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED && mMap != null) {
                            Log.d("MapActivity", "Worker finished, refreshing map.");
                            loadMapData();
                        }
                    }
                });

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

        mMap.clear();
        if (fromFragment) onFragmentClick();
        if (fromEarthquake) onEarthquakeClick();

        // Always load subscribed user locations
        loadSubscribedLocations();
    }

    /** Convert drawable into scaled bitmap */
    private BitmapDescriptor bitmapFromDrawable(int resId, int width, int height) {
        Drawable drawable = ContextCompat.getDrawable(this, resId);
        if (drawable == null) return BitmapDescriptorFactory.defaultMarker();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private int getMarkerIconForDisaster(String disasterType) {
        if (disasterType == null) return R.drawable.default_location;

        switch (disasterType.trim().toLowerCase()) {
            case "earthquake":
            case "eq":
                return R.drawable.earthquake;
            case "tropical cyclone":
            case "cyclone":
            case "tc":
                return R.drawable.tornado;
            case "flood":
            case "fl":
                return R.drawable.flood;
            case "volcano":
            case "vo":
                return R.drawable.volcano;
            case "tsunami":
                return R.drawable.tsunami;
            case "drought":
            case "dr":
                return R.drawable.drought;
            case "wildfire":
            case "wf":
                return R.drawable.wildfire;
            default:
                Log.w("MapActivity", "Unknown disaster type: " + disasterType);
                return R.drawable.default_location;
        }
    }

    private void onFragmentClick() {
        Cursor cursor = disasterDatabaseHelper.getAllDisasters();
        try {
            if (cursor != null && cursor.moveToFirst()) {
                double newestLat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
                double newestLon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newestLat, newestLon), 4));

                do {
                    addMarkerFromCursor(cursor);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void onEarthquakeClick() {
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
                    addMarkerFromCursor(cursor);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void addMarkerFromCursor(Cursor cursor) {
        double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LAT));
        double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(DisasterDatabaseHelper.COLUMN_LON));
        String alertLevel = cursor.getString(cursor.getColumnIndexOrThrow("alertLevel"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String disasterType = cursor.getString(cursor.getColumnIndexOrThrow("disaster_type"));

        LatLng point = new LatLng(lat, lon);
        int markerIcon = getMarkerIconForDisaster(disasterType);

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(disasterType.toUpperCase())
                .snippet(description + " - Alert: " + alertLevel)
                .icon(bitmapFromDrawable(markerIcon, 80, 80))
        );

        //Check distance to user
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double userLat = location.getLatitude();
                    double userLon = location.getLongitude();

                    if (isWithinRadius(userLat, userLon, lat, lon, 1000)) { // 1000 meters = 1km
                        // Trigger alert activity
                        Intent intent = new Intent(MapActivity.this, AlertActivity.class);
                        intent.putExtra("disasterType", disasterType);
                        intent.putExtra("disasterMessage", description + " (ALERT LEVEL: " + alertLevel + ")");
                        startActivity(intent);
                    }
                }
            });
        }
    }

    // Blue markers for user-subscribed locations
    private void loadSubscribedLocations() {
        List<LatLng> subscribed = disasterDatabaseHelper.getSubscribedLocations();
        for (LatLng loc : subscribed) {
            mMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title("My Subscribed Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
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
    private boolean isWithinRadius(double lat1, double lon1, double lat2, double lon2, float radiusMeters) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] <= radiusMeters;
    }

}
