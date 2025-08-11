package com.example.disastermanagementandalertsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Disaster extends Worker {

    private static final String TAG = "DisasterWorker";

    public Disaster(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        Context context = getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            Log.d(TAG, "No internet connection. Retrying later.");
            return Result.retry();
        }

        DisasterDatabaseHelper dbHelper = new DisasterDatabaseHelper(context);
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            dbHelper.deleteOldDisasters(db);

            URL url = new URL("https://www.gdacs.org/xml/rss.xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/rss+xml, text/xml");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(10000); // 10 seconds
            conn.setReadTimeout(15000);    // 15 seconds

            InputStream inputStream = conn.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getTagValue("title", item);
                String pubDateStr = getTagValue("pubDate", item);
                String description = getTagValue("description", item);

                // These are namespaced attributes, not elements
                String alertLevel = item.getAttributeNS("http://www.gdacs.org", "alertlevel");
                String severity = item.getAttributeNS("http://www.gdacs.org", "severity");
                String populationStr = item.getAttributeNS("http://www.gdacs.org", "population");
                String disasterType = item.getAttributeNS("http://www.gdacs.org", "eventtype");
                disasterType = getDisasterTypeFromLink(disasterType);
                String alertScore = item.getAttributeNS("http://www.gdacs.org", "alertscore");
                String country = item.getAttributeNS("http://www.gdacs.org", "country");

                // Latitude and Longitude are namespaced elements
                String lat = getTagValueNS(item, "http://www.w3.org/2003/01/geo/wgs84_pos#", "lat");
                String lon = getTagValueNS(item, "http://www.w3.org/2003/01/geo/wgs84_pos#", "long");

                long pubDateMillis = parsePubDateToMillis(pubDateStr);

                // Insert only if the date is valid and location data is available
                if (pubDateMillis != -1 && !lat.isEmpty() && !lon.isEmpty()) {
                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("pubDate", pubDateStr);
                    values.put(DisasterDatabaseHelper.COLUMN_PUB_DATE_MILLIS, pubDateMillis);
                    values.put("description", description);
                    values.put("population", populationStr);
                    values.put("severity", severity);
                    values.put("disaster_type", disasterType);
                    values.put("alertLevel", alertLevel);
                    values.put("alertScore", alertScore);
                    values.put("country", country);
                    values.put("lat", Double.parseDouble(lat));
                    values.put("lon", Double.parseDouble(lon));

                    db.insertWithOnConflict(DisasterDatabaseHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                }
            }

            inputStream.close();
            conn.disconnect();
            Log.d(TAG, "Work finished successfully.");
            return Result.success();

        } catch (Exception e) {
            Log.e(TAG, "Error in doWork", e);
            return Result.retry();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Helper method to extract non-namespaced tags
    private String getTagValue(String tag, Element element) {
        NodeList list = element.getElementsByTagName(tag);
        if (list.getLength() > 0 && list.item(0).getFirstChild() != null) {
            return list.item(0).getTextContent();
        }
        return "";
    }

    // Helper method to extract namespaced elements
    private String getTagValueNS(@NonNull Element element, String namespace, String tagName) {
        NodeList list = element.getElementsByTagNameNS(namespace, tagName);
        if (list.getLength() > 0 && list.item(0).getFirstChild() != null) {
            return list.item(0).getTextContent();
        }
        return "";
    }

    private long parsePubDateToMillis(String pubDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            Date date = format.parse(pubDate);
            return date.getTime();
        } catch (Exception e) {
            Log.e(TAG, "Error parsing pubDate: " + pubDate, e);
            return -1; // Return -1 to indicate an error
        }
    }

    private String getDisasterTypeFromLink(String eventTypeCode) {
        if (eventTypeCode == null) return "Unknown";

        switch (eventTypeCode) {
            case "EQ": return "Earthquake";
            case "TC": return "Tropical Cyclone";
            case "FL": return "Flood";
            case "VO": return "Volcano";
            case "TS": return "Tsunami";
            case "DR": return "Drought";
            default: return "Unknown";
        }
    }
}