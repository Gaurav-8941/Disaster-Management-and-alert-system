package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity5 extends AppCompatActivity {
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main5);

        // 1) get the ListView
        listView = findViewById(R.id.listView);

        // 2) build items with icon + title
        List<ProfileItem> items = new ArrayList<>();
        items.add(new ProfileItem(R.drawable.home, "Home"));
        items.add(new ProfileItem(R.drawable.emergency, "Helpline"));
        items.add(new ProfileItem(R.drawable.alert, "Alert sound"));
        items.add(new ProfileItem(R.drawable.language, "Language"));
        items.add(new ProfileItem(R.drawable.outline_unknown_document_24, "Do's and Don'ts"));
        items.add(new ProfileItem(R.drawable.faq, "FAQ"));
        items.add(new ProfileItem(R.drawable.about_us, "About us"));
        items.add(new ProfileItem(R.drawable.sign_out, "Sign out"));


        listView.setAdapter(new ProfileAdapter(this, items));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, Mainpage.class));
                    break;
                case 1: // Helpline (placeholder)
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(android.net.Uri.parse("tel:112"));
                    startActivity(intent);
                    Toast.makeText(this, "Helpline number is dialed", Toast.LENGTH_SHORT).show();
                    break;
                case 2: // Alert sound
                    Toast.makeText(this, "Buzzer is only available for now", Toast.LENGTH_SHORT).show();
                    break;
                case 3: // Language
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                case 4: // Do's & Don'ts
                    startActivity(new Intent(this, WhattodoanddontActivity.class));
                    break;
                case 5: // FAQ
                    Toast.makeText(this, "FAQ screen not implemented yet", Toast.LENGTH_SHORT).show();
                    break;
                case 6: // About us
                    Toast.makeText(this, "About Us: Safety app made by Gaurav", Toast.LENGTH_SHORT).show();
                    break;
                case 7: // Sign out
                    startActivity(new Intent(this, MainActivity4.class));
                    finishAffinity();
                    break;
            }
        });


        homeicon = findViewById(R.id.homeicon);
        accpuntcircle = findViewById(R.id.accpuntcircle);
        locationicon = findViewById(R.id.locationicon);
        weathericon = findViewById(R.id.weathericon);
        alerticon = findViewById(R.id.alerticon);

        homeicon.setOnClickListener(v -> startActivity(new Intent(this, Mainpage.class)));
        accpuntcircle.setOnClickListener(v -> startActivity(new Intent(this, MainActivity5.class)));
        locationicon.setOnClickListener(v -> startActivity(new Intent(this, location.class)));
        weathericon.setOnClickListener(v -> startActivity(new Intent(this, WeatherActivity.class)));
        alerticon.setOnClickListener(v -> startActivity(new Intent(this, Weather.class)));
    }
}
