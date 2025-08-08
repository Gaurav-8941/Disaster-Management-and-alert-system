package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class location extends AppCompatActivity {
    Button btnsubscribe;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);
        btnsubscribe=findViewById(R.id.btnSubscribe);
//        btnsubscribe.setOnClickListener(v ->
//        {
//            Intent selectlocationsubscribe=new Intent(this, SelectLocationSubscribe.class);
//            startActivity(selectlocationsubscribe);
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