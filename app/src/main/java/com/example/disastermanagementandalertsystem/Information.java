package com.example.disastermanagementandalertsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Information extends AppCompatActivity {

    ListView dos,donts;
    Button close;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.information);
        String lang = getIntent().getStringExtra("doanddont");
        Toast.makeText(getApplicationContext(),lang,Toast.LENGTH_LONG).show();
        homeicon=findViewById(R.id.homeicon);
        accpuntcircle=findViewById(R.id.accpuntcircle);
        locationicon=findViewById(R.id.locationicon);
        weathericon=findViewById(R.id.weathericon);
        alerticon=findViewById(R.id.alerticon);

        close = findViewById(R.id.closeButton);
        dos = findViewById(R.id.dosList);
        donts = findViewById(R.id.dontListView);

        DisasterDosDont doobj = new DisasterDosDont();

        ArrayList<String>doList = splitPoints(doobj.dosMap.get(getIntent().getStringExtra("disaster")));
        ArrayList<String>dontList = splitPoints(doobj.dontsMap.get(getIntent().getStringExtra("disaster")));

        ArrayAdapter<String> doarrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, doList);
        dos.setAdapter(doarrayAdapter);

        ArrayAdapter<String> dontarrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dontList);
        dos.setAdapter(dontarrayAdapter);


        close.setOnClickListener(v ->
        {
            Intent back=new Intent(this, WhattodoanddontActivity.class);
            startActivity(back);
        });
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

    // Function to split map value into individual sentences
    public static ArrayList<String> splitPoints(String mapValue) {
        ArrayList<String> pointsList = new ArrayList<>();

        if (mapValue != null && !mapValue.trim().isEmpty()) {
            // Split by semicolon and trim spaces
            String[] points = mapValue.split(";");
            for (String point : points) {
                point = point.trim();
                if (!point.isEmpty()) {
                    pointsList.add(point);
                }
            }
        }

        return pointsList;
    }

}