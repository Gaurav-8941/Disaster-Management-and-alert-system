package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Mainpage extends AppCompatActivity {
    TextView location, alert;
    Button call;
    CardView disastrsurvivalguide, recenteathquake;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainpage);
        location=findViewById(R.id.location);
        alert=findViewById(R.id.alert);
        disastrsurvivalguide.setOnClickListener(v -> {
            Intent whatdoanddont=new Intent(this, WhattodoanddontActivity.class);
            startActivity(whatdoanddont);
        });
//        recenteathquake.setOnClickListener(v ->
//        {
//            Intent recentearthquake= new Intent(this, RecentEarthquake.class);
//            startActivity(recentearthquake);
//        });
        homeicon=findViewById(R.id.homeicon);
        accpuntcircle=findViewById(R.id.accpuntcircle);
        locationicon=findViewById(R.id.locationicon);
        weathericon=findViewById(R.id.weathericon);
        alerticon=findViewById(R.id.alerticon);
        homeicon.setOnClickListener(v ->
        {
            Intent home=new Intent(this, Mainpage.class);
            startActivity(home);
        });
        accpuntcircle.setOnClickListener(v ->
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
            Intent weathericon=new Intent(this, Weatheractivity.class);
            startActivity(weathericon);
        });
        alerticon.setOnClickListener(v ->
        {
            Intent alerticon=new Intent(this,  Weather.class);
            startActivity(alerticon);
        });
    }
}