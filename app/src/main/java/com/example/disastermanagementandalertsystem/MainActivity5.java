package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);
        ArrayList<String> profileList= new ArrayList<>();
        profileList.add(R.drawable.home,"Home");
        profileList.add(R.drawable.emergency,"Helpline");
        profileList.add(R.drawable.alert,"Alert sound");
        profileList.add(R.drawable.language,"Language");
        profileList.add(R.drawable.outline_unknown_document_24,"Do's and Don't");
        profileList.add(R.drawable.faq,"FAQ");
        profileList.add(R.drawable.about_us,"About us");
        profileList.add(R.drawable.sign_out,"Sign out");
        ArrayAdapter <String> arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profileList);
        listView.setAdapter(arrayAdapter);
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