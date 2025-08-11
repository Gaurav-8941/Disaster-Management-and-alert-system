package com.example.disastermanagementandalertsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    TextView sunriseTextView, sunsetTextView, hourlyWeatherTextView;
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        sunriseTextView = findViewById(R.id.sunrise);
        sunsetTextView = findViewById(R.id.sunset);
        hourlyWeatherTextView = findViewById(R.id.hourlyWeather);

        fetchWeatherData();
    }

    private void fetchWeatherData() {
        new Thread(() -> {
            try {
                // Example: Pune coordinates, auto timezone
                String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=18.5204&longitude=73.8567&hourly=temperature_2m&daily=sunrise,sunset&timezone=auto";

                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                parseWeatherJson(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void parseWeatherJson(String json) {
        try {
            JSONObject root = new JSONObject(json);

            // Daily sunrise/sunset
            JSONObject daily = root.getJSONObject("daily");
            JSONArray sunriseArray = daily.getJSONArray("sunrise");
            JSONArray sunsetArray = daily.getJSONArray("sunset");

            String sunrise = sunriseArray.getString(0);
            String sunset = sunsetArray.getString(0);

            // Hourly temperature
            JSONObject hourly = root.getJSONObject("hourly");
            JSONArray timeArray = hourly.getJSONArray("time");
            JSONArray tempArray = hourly.getJSONArray("temperature_2m");

            StringBuilder hourlyData = new StringBuilder();
            for (int i = 0; i < timeArray.length(); i++) {
                hourlyData.append(timeArray.getString(i))
                        .append(" : ")
                        .append(tempArray.getDouble(i))
                        .append("°C\n");
            }

            // Update UI
            runOnUiThread(() -> {
                sunriseTextView.setText(sunrise+"AM Sunrise" );
                sunsetTextView.setText(sunset+":PM Sunset");
                hourlyWeatherTextView.setText(hourlyData.toString());
            });

        } catch (Exception e) {
            Log.e("WeatherParseError", e.toString());
        }
    }
}
