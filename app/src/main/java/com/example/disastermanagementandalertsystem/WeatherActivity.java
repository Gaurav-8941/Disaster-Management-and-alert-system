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
import java.util.Locale;
import java.util.TimeZone;

public class WeatherActivity extends AppCompatActivity {

    TextView sunriseTextView, sunsetTextView, locationofweather, temperature;
    TextView []time = new TextView[5];
    TextView []temp = new TextView[5];
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
        locationofweather = findViewById(R.id.locationofweather);
        temperature = findViewById(R.id.temperature);

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
            Intent weathericon=new Intent(this, WeatherActivity.class);
            startActivity(weathericon);
        });
        alerticon.setOnClickListener(v ->
        {
            Intent alerticon=new Intent(this,  Weather.class);
            startActivity(alerticon);
        });

        sunriseTextView = findViewById(R.id.sunrise);
        sunsetTextView = findViewById(R.id.sunset);
        time[0] = findViewById(R.id.time0);
        time[1] = findViewById(R.id.time1);
        time[2] = findViewById(R.id.time2);
        time[3] = findViewById(R.id.time3);
        time[4] = findViewById(R.id.time4);

        temp[0] = findViewById(R.id.time0);
        temp[1] = findViewById(R.id.time1);
        temp[2] = findViewById(R.id.time2);
        temp[3] = findViewById(R.id.time3);
        temp[4] = findViewById(R.id.time4);
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        new Thread(() -> {
            try {
                String [] coordinate = new MapActivity().getCurrentLocation();
                String lat = coordinate[0],lon = coordinate[1];
                // Example: Pune coordinates, auto timezone
                String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude="+lat+"&longitude="+lon+"&hourly=temperature_2m&daily=sunrise,sunset&timezone=auto";

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

            String location = get(daily.getString("timezone"));
            locationofweather.setText(location);

            String sunrise = sunriseArray.getString(0);
            String sunset = sunsetArray.getString(0);

            // Hourly temperature
            JSONObject hourly = root.getJSONObject("hourly");
            JSONArray timeArray = hourly.getJSONArray("time");
            JSONArray tempArray = hourly.getJSONArray("temperature_2m");

            for (int i = 0; i < 4; i++) {
                time[i].setText(timeArray.getString(i).substring(0,10));
                temp[i].setText(tempArray+"°C\n");
            }
            temperature.setText(time[0].getText().toString());

            // Update UI
            runOnUiThread(() -> {

                sunriseTextView.setText(sunrise+"AM Sunrise" );
                sunsetTextView.setText(sunset+":PM Sunset");
            });

        } catch (Exception e) {
            Log.e("WeatherParseError", e.toString());
        }
    }
    String get(String timeZoneId) {

        // Extract parts
        String[] parts = timeZoneId.split("/");
        String continent = parts[0];
        String city = parts.length > 1 ? parts[1].replace("_", " ") : "";

        // Get country name from timezone
        String countryCode = TimeZone.getTimeZone(timeZoneId).getID();
        String countryName = getCountryFromTimeZone(timeZoneId);

        // Output format: city country continent
        return city + " " + countryName + " " + continent;
    }

    // Method to find country from timezone
    public static String getCountryFromTimeZone(String timeZoneId) {
        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            String[] zones = TimeZone.getAvailableIDs(new Locale("", iso).getCountry().hashCode());
            for (String zone : TimeZone.getAvailableIDs()) {
                if (zone.equalsIgnoreCase(timeZoneId)) {
                    return locale.getDisplayCountry();
                }
            }
        }
        // Fallback for known timezones
        if (timeZoneId.equals("Asia/Kolkata")) return "India";
        return "Unknown";
    }

}
