package com.example.disastermanagementandalertsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

        // ✅ Modern + Legacy internet check
        if (cm != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                android.net.NetworkCapabilities capabilities =
                        cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities == null) {
                    Log.d(TAG, "No internet connection. Retrying later.");
                    return Result.retry();
                }
            } else {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork == null || !activeNetwork.isConnected()) {
                    Log.d(TAG, "No internet connection. Retrying later.");
                    return Result.retry();
                }
            }
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

                // Non-namespaced tags
                String title = getTagValue("title", item);
                String pubDateStr = getTagValue("pubDate", item);
                String description = getTagValue("description", item);

                // Namespaced tags
                String populationStr = getTagValueNS("http://www.gdacs.org", "population", item);
                populationStr = (populationStr.isBlank()) ? "None" : populationStr;

                String severity = getTagValueNS("http://www.gdacs.org", "severity", item);
                severity = (severity.isBlank()) ? "None" : severity;

                String disasterType = getTagValueNS("http://www.gdacs.org", "eventtype", item);
                String alertLevel = getTagValueNS("http://www.gdacs.org", "alertlevel", item);
                String alertScore = getTagValueNS("http://www.gdacs.org", "alertscore", item);
                String country = getTagValueNS("http://www.gdacs.org", "country", item);

                String lat = getTagValueNS("http://www.w3.org/2003/01/geo/wgs84_pos#", "lat", item);
                String lon = getTagValueNS("http://www.w3.org/2003/01/geo/wgs84_pos#", "long", item);

                long pubDateMillis = parsePubDateToMillis(pubDateStr);

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

                    Log.w(TAG, "Inserting disaster: " + title + ", Type: " + disasterType + ", Lat: " + lat + ", Lon: " + lon);
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

    // Non-namespaced
    private String getTagValue(String tag, Element element) {
        NodeList list = element.getElementsByTagName(tag);
        if (list.getLength() > 0 && list.item(0).getFirstChild() != null) {
            return list.item(0).getTextContent();
        }
        return "";
    }

    // Namespaced
    private String getTagValueNS(String namespace, String tag, Element element) {
        NodeList nodeList = element.getElementsByTagNameNS(namespace, tag);
        if (nodeList.getLength() > 0 && nodeList.item(0).getFirstChild() != null) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    private long parsePubDateToMillis(String pubDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
            Date date = format.parse(pubDate);
            if (date != null) {
                return date.getTime();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing pubDate: " + pubDate, e);
        }
        return -1;
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
            case "WF": return "Wildfire";
            default: return "Unknown";
        }
    }
}
