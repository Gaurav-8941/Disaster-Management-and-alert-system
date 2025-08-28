package com.example.disastermanagementandalertsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Weather extends AppCompatActivity {
    ListView listView;
    DisasterDatabaseHelper databaseHelper;

    ArrayList<String> disaster, alert, dates, descriptions;
    List<DisasterModel> models;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.alert_page);
        homeicon=findViewById(R.id.homeicon);
        accpuntcircle=findViewById(R.id.accpuntcircle);
        locationicon=findViewById(R.id.locationicon);
        weathericon=findViewById(R.id.weathericon);
        alerticon=findViewById(R.id.alerticon);
        databaseHelper = new DisasterDatabaseHelper(this);

        listView = findViewById(R.id.listalertcards); // add a ListView in weather.xml
        models = getAllDisaster();
        DisasterAdapter adapter = new DisasterAdapter(this, models);
        listView.setAdapter(adapter);


        homeicon.setOnClickListener(v -> {
            Intent home=new Intent(this, Mainpage.class);
            startActivity(home);
        });
        accpuntcircle.setOnClickListener(v ->{
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

    }

    public ArrayList<DisasterModel> getAllDisaster() {
        ArrayList<DisasterModel> list = new ArrayList<>();
        SQLiteDatabase db = this.databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM disasters", null);
            if (cursor.moveToFirst()) {
                do {
                    list.add(new DisasterModel(
                            cursor.getString(cursor.getColumnIndexOrThrow("disaster_type")),
                            cursor.getString(cursor.getColumnIndexOrThrow("description")),
                            cursor.getString(cursor.getColumnIndexOrThrow("alertLevel")),
                            cursor.getString(cursor.getColumnIndexOrThrow("severity")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("population"))
                    ));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }


}