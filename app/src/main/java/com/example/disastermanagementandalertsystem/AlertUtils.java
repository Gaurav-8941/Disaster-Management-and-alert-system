package com.example.disastermanagementandalertsystem;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class AlertUtils {

    public static void triggerDisasterAlert(Context context, String disasterType, String description) {
        // Vibrate
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(3000); // vibrate 3 seconds
        }

        // Play sound
        MediaPlayer player = MediaPlayer.create(context, R.raw.buzzer); // replace buzzer with your mp3 in res/raw
        if (player != null) {
            player.start();
        }

        // Open AlertActivity
        Intent intent = new Intent(context, AlertActivity.class);
        intent.putExtra("disasterType", disasterType);
        intent.putExtra("disasterMessage", description);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
