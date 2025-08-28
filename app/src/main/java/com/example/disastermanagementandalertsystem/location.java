package com.example.disastermanagementandalertsystem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class location extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private DisasterDatabaseHelper disasterDatabaseHelper;
    private TextView locationStatus;
    private Button btnSubscribe;
    ImageView homeicon, accountcircle, locationicon, weathericon, alerticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationStatus = findViewById(R.id.locationStatus);
        btnSubscribe = findViewById(R.id.btnSubscribe);

        disasterDatabaseHelper = new DisasterDatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        homeicon=findViewById(R.id.homeicon);
        accountcircle=findViewById(R.id.accpuntcircle);
        locationicon=findViewById(R.id.locationicon);
        weathericon=findViewById(R.id.weathericon);
        alerticon=findViewById(R.id.alerticon);
        homeicon.setOnClickListener(v ->
        {
            Intent home=new Intent(this, Mainpage.class);
            startActivity(home);
        });
        accountcircle.setOnClickListener(v ->
        {
            Intent accpuntcirlce=new Intent(this, MainActivity5.class);
            startActivity(accpuntcirlce);
        });
        locationicon.setOnClickListener(v ->
        {
            Intent locationicon=new Intent(this, location.class);
            startActivity(locationicon);
        });
        weathericon.setOnClickListener(v ->
        {
            Intent weathericon=new Intent(this, WeatherActivity.class);
            startActivity(weathericon);
        });
        alerticon.setOnClickListener(v ->
        {
            Intent alerticon=new Intent(this,  Weather.class);
            startActivity(alerticon);
        });

        btnSubscribe.setOnClickListener(v -> fetchAndSaveLocation());
    }

    private void fetchAndSaveLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                // Save to DB
                disasterDatabaseHelper.addSubscribedLocation(lat, lon);

                // Update UI
                locationStatus.setText("Subscribed at: " + lat + ", " + lon);
            } else {
                locationStatus.setText("Unable to fetch location. Try again.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchAndSaveLocation();
            } else {
                locationStatus.setText("Permission denied.");
            }
        }
    }

}

