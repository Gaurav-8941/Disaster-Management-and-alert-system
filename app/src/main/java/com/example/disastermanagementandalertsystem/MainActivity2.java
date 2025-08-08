package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    Button skipbtn, nextbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        nextbtn=findViewById(R.id.nextbtn);
        skipbtn=findViewById(R.id.skipbtn);
        String lang = getIntent().getStringExtra("language");
        Toast.makeText(getApplicationContext(),lang,Toast.LENGTH_LONG).show();
        skipbtn.setOnClickListener(v ->
        {
            Intent mainactivity4 = new Intent(this, MainActivity4.class);
            startActivity(mainactivity4);
        });
        nextbtn.setOnClickListener(v -> {
            Intent mainactivity3 =new Intent(this, MainActivity3.class);
            startActivity(mainactivity3);
        });

    }
}