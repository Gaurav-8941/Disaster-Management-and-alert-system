package com.example.disastermanagementandalertsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Mainpage extends AppCompatActivity {

    LinearLayout frameLayout;
    TextView location, alert;
    Button call, btnSubscribe;

    ImageButton emergency;
    CardView disastrsurvivalguide, recenteathquake;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mainpage);
        location=findViewById(R.id.location);
        alert=findViewById(R.id.alert);
        frameLayout = findViewById(R.id.map_fragment);
        disastrsurvivalguide=findViewById(R.id.disastrsurvivalguide);
        emergency = findViewById(R.id.emergencycall);

        recenteathquake=findViewById(R.id.recenteathquake);
        recenteathquake.setOnClickListener(v ->
        {
            Intent recentearthquake= new Intent(this, MapActivity.class);
            recentearthquake.putExtra("earthquake",true);
            startActivity(recentearthquake);
        });
        homeicon=findViewById(R.id.homeicon);
        accpuntcircle=findViewById(R.id.accpuntcircle);
        locationicon=findViewById(R.id.locationicon);
        weathericon=findViewById(R.id.weathericon);
        alerticon=findViewById(R.id.alerticon);
        btnSubscribe=findViewById(R.id.btnSubscribe);
        homeicon.setOnClickListener(v ->

        {
            Intent home=new Intent(this, Mainpage.class);
            startActivity(home);
        });
        accpuntcircle.setOnClickListener(v -> {
            Intent accpuntcirlce=new Intent(this, MainActivity5.class);
            startActivity(accpuntcirlce);
        });
        locationicon.setOnClickListener(v -> {
            Intent locationicon=new Intent(this, location.class);
            startActivity(locationicon);
        });
        weathericon.setOnClickListener(v -> {
            Intent weathericon=new Intent(this, WeatherActivity.class);
            startActivity(weathericon);
        });
        alerticon.setOnClickListener(v -> {
            Intent alerticon=new Intent(this,  Weather.class);
            startActivity(alerticon);
        });
        btnSubscribe.setOnClickListener(v ->
        {
            Intent location=new Intent(this, location.class);
            startActivity(location);
        });

        frameLayout.setOnClickListener(view->{
            startActivity(new Intent(this, MapActivity.class).putExtra("fragment",true));
        });
        emergency.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(android.net.Uri.parse("tel:112"));
            startActivity(intent);
        });

        disastrsurvivalguide.setOnClickListener(v -> {
            Intent whatdoanddont = new Intent(this, WhattodoanddontActivity.class);
            startActivity(whatdoanddont);
        });
    }
}