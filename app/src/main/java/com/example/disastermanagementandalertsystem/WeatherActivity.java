package com.example.disastermanagementandalertsystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    private static final int REQ_LOCATION = 4242;

    // Fallback: New Delhi
    private static final double FALLBACK_LAT = 28.6139;
    private static final double FALLBACK_LON = 77.2090;

    TextView sunriseTextView, sunsetTextView, locationofweather, temperature;
    TextView[] time = new TextView[5];
    TextView[] temp = new TextView[5];
    ImageView homeicon, accpuntcircle, locationicon, weathericon, alerticon;

    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatheractivity);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Bottom nav
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

        // Weather UI refs
        locationofweather = findViewById(R.id.locationofweather);
        temperature = findViewById(R.id.temperature);
        sunriseTextView = findViewById(R.id.sunrise);
        sunsetTextView = findViewById(R.id.sunset);

        time[0] = findViewById(R.id.time0);
        time[1] = findViewById(R.id.time1);
        time[2] = findViewById(R.id.time2);
        time[3] = findViewById(R.id.time3);
        time[4] = findViewById(R.id.time4);

        temp[0] = findViewById(R.id.temp0);
        temp[1] = findViewById(R.id.temp1);
        temp[2] = findViewById(R.id.temp2);
        temp[3] = findViewById(R.id.temp3);
        temp[4] = findViewById(R.id.temp4);

        requestOrFetchWeather();
    }

    /** Ask for location permission if needed; else fetch with real GPS. Always show fallback if needed. */
    private void requestOrFetchWeather() {
        boolean fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (fine || coarse) {
            resolveLocationAndFetch();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQ_LOCATION
            );
            // Populate UI with fallback immediately so the screen isn’t blank
            fetchWeatherData(FALLBACK_LAT, FALLBACK_LON);
        }
    }

    private void resolveLocationAndFetch() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            fetchWeatherData(location.getLatitude(), location.getLongitude());
                        } else {
                            // Last location can be null on first run—use fallback
                            Log.w(TAG, "getLastLocation() returned null, using fallback.");
                            fetchWeatherData(FALLBACK_LAT, FALLBACK_LON);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Location failure, using fallback.", e);
                        fetchWeatherData(FALLBACK_LAT, FALLBACK_LON);
                    });
        } catch (Exception e) {
            Log.e(TAG, "resolveLocationAndFetch error", e);
            fetchWeatherData(FALLBACK_LAT, FALLBACK_LON);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            boolean granted = false;
            for (int res : grantResults) {
                if (res == PackageManager.PERMISSION_GRANTED) {
                    granted = true;
                    break;
                }
            }
            if (granted) resolveLocationAndFetch();
            // if denied go to fallback
        }
    }

    private void fetchWeatherData(double lat, double lon) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                String apiUrl = "https://api.open-meteo.com/v1/forecast"
                        + "?latitude=" + lat
                        + "&longitude=" + lon
                        + "&hourly=temperature_2m"
                        + "&daily=sunrise,sunset"
                        + "&timezone=auto";

                URL url = new URL(apiUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(15000);

                int code = conn.getResponseCode();
                InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
                reader.close();

                if (code >= 200 && code < 300) {
                    parseWeatherJson(response.toString());
                } else {
                    Log.e(TAG, "Weather HTTP " + code + ": " + response);
                }
            } catch (Exception e) {
                Log.e(TAG, "WeatherFetchError", e);
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }

    private void parseWeatherJson(String json) {
        try {
            JSONObject root = new JSONObject(json);

            // Location text from timezone
            String timeZoneId = root.optString("timezone", "Etc/UTC");
            String location = getReadableLocation(timeZoneId);
            runOnUiThread(() -> locationofweather.setText(location));

            // Sunrise / Sunset
            JSONObject daily = root.getJSONObject("daily");
            JSONArray sunriseArray = daily.getJSONArray("sunrise");
            JSONArray sunsetArray = daily.getJSONArray("sunset");

            String sunrise = sunriseArray.length() > 0 ? sunriseArray.getString(0) : "";
            String sunset = sunsetArray.length() > 0 ? sunsetArray.getString(0) : "";

            // Expected format "yyyy-MM-ddTHH:mm" from Open-Meteo. Take the HH:mm part.
            String sunriseHM = sunrise.contains("T") ? sunrise.split("T")[1] : sunrise;
            String sunsetHM = sunset.contains("T") ? sunset.split("T")[1] : sunset;

            final String sunriseFormatted = to12Hour(sunriseHM);
            final String sunsetFormatted = to12Hour(sunsetHM);

            // Hourly temps
            JSONObject hourly = root.getJSONObject("hourly");
            JSONArray timeArray = hourly.getJSONArray("time");
            JSONArray tempArray = hourly.getJSONArray("temperature_2m");

            runOnUiThread(() -> {
                try {
                    sunriseTextView.setText(sunriseFormatted + "\nSunrise");
                    sunsetTextView.setText(sunsetFormatted + "\nSunset");

                    int count = Math.min(5, Math.min(timeArray.length(), tempArray.length()));
                    for (int i = 0; i < count; i++) {
                        String raw = timeArray.getString(i);
                        String hm = raw.contains("T") ? raw.split("T")[1] : raw; // "HH:mm"
                        time[i].setText(to12Hour(hm));
                        temp[i].setText(tempArray.getString(i) + "°C");
                    }

                    if (tempArray.length() > 0) {
                        temperature.setText(tempArray.getString(0) + "°C");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "WeatherUI update error", e);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "WeatherParseError", e);
        }
    }

    /** Format "HH:mm" -> "hh:mm a", null-safe. */
    private String to12Hour(String hhmm) {
        if (hhmm == null || hhmm.trim().isEmpty()) return "--:--";
        try {
            SimpleDateFormat in = new SimpleDateFormat("HH:mm", Locale.US);
            Date d = in.parse(hhmm);
            if (d == null) return hhmm; // Lint: might be null -> guard
            SimpleDateFormat out = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return out.format(d);
        } catch (Exception e) {
            return hhmm;
        }
    }

    /** Pretty "City Country (Continent)" from timezone string like "Asia/Kolkata". */
    private String getReadableLocation(String timeZoneId) {
        String[] parts = timeZoneId.split("/");
        String continent = parts.length > 0 ? parts[0] : "—";
        String city = (parts.length > 1 ? parts[1] : "").replace("_", " ");

        String countryName = getCountryFromTimeZone(timeZoneId);
        return (city.isEmpty() ? "" : (city + " ")) + countryName + " (" + continent + ")";
    }

    // Simple best-effort country lookup from timezone (kept from your version)
    public static String getCountryFromTimeZone(String timeZoneId) {
        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            for (String zone : TimeZone.getAvailableIDs()) {
                if (zone.equalsIgnoreCase(timeZoneId)) {
                    return locale.getDisplayCountry();
                }
            }
        }
        if ("Asia/Kolkata".equals(timeZoneId)) return "India";
        return "Unknown";
    }
}
