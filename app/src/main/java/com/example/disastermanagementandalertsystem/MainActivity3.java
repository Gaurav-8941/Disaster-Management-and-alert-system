package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {
    Button lastbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        lastbtn = findViewById(R.id.lastbtn);
        lastbtn.setOnClickListener(v ->
        {
            Intent mainactivity4 = new Intent(this, MainActivity4.class);
            startActivity(mainactivity4);
        });
    }
}