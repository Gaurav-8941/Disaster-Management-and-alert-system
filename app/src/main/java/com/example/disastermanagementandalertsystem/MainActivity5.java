package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
=======
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.disastermanagementandalertsystem.Mainpage;
import com.example.disastermanagementandalertsystem.ProfileAdapter;
import com.example.disastermanagementandalertsystem.ProfileItem;
import com.example.disastermanagementandalertsystem.R;
import com.example.disastermanagementandalertsystem.Weather;
import com.example.disastermanagementandalertsystem.Weatheractivity;
import com.example.disastermanagementandalertsystem.WhattodoanddontActivity;
import com.example.disastermanagementandalertsystem.location;
>>>>>>> ecfc175 (uploading files)

import java.util.ArrayList;

public class MainActivity5 extends AppCompatActivity {
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;
<<<<<<< HEAD

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
=======
    ListView listView;
    ArrayList<ProfileItem> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        listView = findViewById(R.id.listView);

        profileList = new ArrayList<>();
        profileList.add(new ProfileItem(R.drawable.home, "Home"));
        profileList.add(new ProfileItem(R.drawable.emergency, "Helpline"));
        profileList.add(new ProfileItem(R.drawable.alert, "Alert sound"));
        profileList.add(new ProfileItem(R.drawable.language, "Language"));
        profileList.add(new ProfileItem(R.drawable.outline_unknown_document_24, "Do's and Don't"));
        profileList.add(new ProfileItem(R.drawable.faq, "FAQ"));
        profileList.add(new ProfileItem(R.drawable.about_us, "About us"));
        profileList.add(new ProfileItem(R.drawable.sign_out, "Sign out"));

        ProfileAdapter adapter = new ProfileAdapter(this, profileList);
        listView.setAdapter(adapter);

        homeicon = findViewById(R.id.homeicon);
        accpuntcircle = findViewById(R.id.accpuntcircle);
        locationicon = findViewById(R.id.locationicon);
        weathericon = findViewById(R.id.weathericon);
        alerticon = findViewById(R.id.alerticon);

        homeicon.setOnClickListener(v -> startActivity(new Intent(this, Mainpage.class)));
        accpuntcircle.setOnClickListener(v -> startActivity(new Intent(this, MainActivity5.class)));
        locationicon.setOnClickListener(v -> startActivity(new Intent(this, location.class)));
        weathericon.setOnClickListener(v -> startActivity(new Intent(this, Weatheractivity.class)));
        alerticon.setOnClickListener(v -> startActivity(new Intent(this, Weather.class)));

        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selected = profileList.get(position).getTitle();
            switch (selected) {
                case "Home":
                    startActivity(new Intent(this, Mainpage.class));
                    break;
                case "Do's and Don't":
                    startActivity(new Intent(this, WhattodoanddontActivity.class));
                    break;
//                case "Helpline":
//                    startActivity(new Intent(this, HelplineActivity.class));
//                    break;
//                    case "Alert Sound":
//                    startActivity(new Intent(this, Alert sound.class));
//                    break;
//                    case "Language":
//                    startActivity(new Intent(this, MainActivity.class));
//                    break;
//                    case "FAQ":
//                    startActivity(new Intent(this, FAQ.class));
//                    break;
//                    case "Sign out":
//                    startActivity(new Intent(this, Sign out.class));
//                    break;

            }
        });
    }
}
>>>>>>> ecfc175 (uploading files)
