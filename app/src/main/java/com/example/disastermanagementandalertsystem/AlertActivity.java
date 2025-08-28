package com.example.disastermanagementandalertsystem;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlertActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        TextView alertTitle = findViewById(R.id.alertTitle);
        TextView alertMessage = findViewById(R.id.alertMessage);
        Button stopButton = findViewById(R.id.btnStopAlert);

        // Example: Get extra data (disaster info)
        String disasterType = getIntent().getStringExtra("disasterType");
        String disasterMessage = getIntent().getStringExtra("disasterMessage");

        if (disasterType != null) {
            alertTitle.setText(disasterType.toUpperCase() + " ALERT!");
        }
        if (disasterMessage != null) {
            alertMessage.setText(disasterMessage);
        }

        // Play alert sound (choose any mp3 from res/raw)
        mediaPlayer = MediaPlayer.create(this, R.raw.buzzer); // Example
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Vibrate with pattern
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 500, 1000}; // wait, vibrate, pause, vibrate
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));

        // Stop button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlert();
                finish();
            }
        });
    }

    private void stopAlert() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlert();
    }
}
