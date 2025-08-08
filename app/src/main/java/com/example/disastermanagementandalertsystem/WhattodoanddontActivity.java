package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WhattodoanddontActivity extends AppCompatActivity {
    ListView doanddont;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.whattodoanddont);
        doanddont =findViewById(R.id.doanddont);
        ArrayList<String> doanddontlist =new ArrayList<>();
        doanddontlist.add("Avalanch");
        doanddontlist.add("Cold Wave");
        doanddontlist.add("Cyclone");
        doanddontlist.add("Drought");
        doanddontlist.add("Earthquacks");
        doanddontlist.add("Fire");
        doanddontlist.add("Flood");
        doanddontlist.add("Gas and Chemical LEakages");
        doanddontlist.add("ThunderStorm");
        doanddontlist.add("Landslide");
        doanddontlist.add("ForestFire");
        doanddontlist.add("Lightning");
        doanddontlist.add("Tsunami");
        doanddontlist.add("UrbanFlood");
        doanddontlist.add("Heat Wave");
        doanddontlist.add("Smog/Air pollution");
        doanddontlist.add("Biological Emergencies");
        doanddontlist.add("Nuclear Radiological Emergencies");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doanddontlist);
        doanddont.setAdapter(arrayAdapter);
        doanddont.setOnItemClickListener((parent, view, position, id) -> {
            Intent info=new Intent(this, Information.class);
            info.putExtra("doanddont", doanddontlist.get(position));
            startActivity(info);
        });
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