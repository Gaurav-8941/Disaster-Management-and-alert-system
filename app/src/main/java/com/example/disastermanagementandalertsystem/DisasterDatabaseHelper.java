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

    private static final int DATABASE_VERSION = 5; // Incrementing version to trigger onUpgrade
    private static final String DATABASE_NAME = "gdacs.db";
    public static final String TABLE_NAME = "disasters";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_PUB_DATE_MILLIS = "pubDateMillis";

    public DisasterDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "pubDate TEXT UNIQUE," +
                COLUMN_PUB_DATE_MILLIS + " INTEGER," + // Changed to INTEGER for timestamp
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

        // Optional indexes for faster queries
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS idx_pubdate ON " + TABLE_NAME + "(pubDate);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate to handle schema changes.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteOldDisasters(SQLiteDatabase db) {
        // Delete entries older than 5 hours using the INTEGER timestamp
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
        // Order by the timestamp column for correct sorting
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_PUB_DATE_MILLIS + " DESC", null);
    }
}