package com.example.disastermanagementandalertsystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DisasterDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6; // bumped version for new table
    private static final String DATABASE_NAME = "gdacs.db";

    //disasster table
    public static final String TABLE_NAME = "disasters";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_PUB_DATE_MILLIS = "pubDateMillis";

    // ----------------- Subscribed Locations Table -----------------
    private static final String TABLE_SUBSCRIBED = "subscribed_locations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_LAT = "lat";
    private static final String COLUMN_USER_LON = "lon";

    public DisasterDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Disasters table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "pubDate TEXT UNIQUE," +
                COLUMN_PUB_DATE_MILLIS + " INTEGER," + // timestamp
                "description TEXT," +
                "population TEXT," +
                "severity TEXT," +
                "disaster_type TEXT," +
                "alertLevel TEXT," +
                "alertScore TEXT," +
                "country TEXT," +
                COLUMN_LAT + " REAL," +
                COLUMN_LON + " REAL" +
                ")");

        // Index for faster queries
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_pubdate ON " + TABLE_NAME + "(pubDate);");

        // Subscribed locations table
        db.execSQL("CREATE TABLE " + TABLE_SUBSCRIBED + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_LAT + " REAL," +
                COLUMN_USER_LON + " REAL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate on schema change
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBSCRIBED);
        onCreate(db);
    }

    // ----------------- Disasters Logic -----------------
    public void deleteOldDisasters(SQLiteDatabase db) {
        long fiveHoursAgo = System.currentTimeMillis() - (5 * 60 * 60 * 1000);
        try {
            int rowsDeleted = db.delete(TABLE_NAME, COLUMN_PUB_DATE_MILLIS + " < ?", new String[]{String.valueOf(fiveHoursAgo)});
            Log.d("DBHelper", "Deleted " + rowsDeleted + " old entries.");
        } catch (Exception e) {
            Log.e("DBHelper", "Error deleting old disasters", e);
        }
    }

    public List<LatLng> getAllCoordinates() {
        List<LatLng> coordinates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_LAT, COLUMN_LON},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LAT));
                double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LON));
                coordinates.add(new LatLng(lat, lon));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return coordinates;
    }

    public Cursor getAllDisasters() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_PUB_DATE_MILLIS + " DESC", null);
    }

    // ----------------- Subscribed Locations Logic -----------------
    public void addSubscribedLocation(double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_SUBSCRIBED + " (" +
                        COLUMN_USER_LAT + ", " + COLUMN_USER_LON + ") VALUES (?, ?)",
                new Object[]{lat, lon});
        db.close();
    }

    public List<LatLng> getSubscribedLocations() {
        List<LatLng> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SUBSCRIBED, null);

        if (cursor.moveToFirst()) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_LAT));
                double lon = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_USER_LON));
                list.add(new LatLng(lat, lon));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
