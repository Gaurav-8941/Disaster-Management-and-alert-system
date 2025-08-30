package com.example.disastermanagementandalertsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;
import java.util.Random;

public class Authenticate extends AppCompatActivity {
    Button submitBtn;
    EditText input;
    String pinCode;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        input = findViewById(R.id.input);
        submitBtn=findViewById(R.id.submitBtn);

        generateCode();

        submitBtn.setOnClickListener(view -> {
            if (input.getText().toString().equals(pinCode)) {
                Intent intent = new Intent(this, Mainpage.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void generateCode(){
        boolean isPhone = getIntent().getBooleanExtra("phone",true);
        String content = getIntent().getStringExtra("content");
        if(isPhone){

            try {
                long phoneNumber = Long.parseLong(String.valueOf(content));

                Random random = new Random();
                int pin = 100000 + random.nextInt(900000);
                pinCode = String.valueOf(pin);

                String message = "Your verification code is: " + pinCode;

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(content, null, message, null, null);

                Toast.makeText(this, "Verification code sent to " + phoneNumber, Toast.LENGTH_LONG).show();
                Log.d("SMS_SEND", "Sent PIN " + pinCode + " to " + phoneNumber);

            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS. Please check permissions.", Toast.LENGTH_LONG).show();
                Log.e("SMS_SEND_ERROR", "Error sending SMS", e);
            }
        }else {
            try {

                Random random = new Random();
                int pin = 100000 + random.nextInt(900000); // Ensures a 6-digit number
                pinCode = String.valueOf(pin);

                String message = "Your verification code is: " + pinCode;

                composeEmail(this,content,"Verification",message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void composeEmail(Context context, String recipients, String subject, String body) {
        // Use ACTION_SENDTO to ensure only email apps handle this
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // This is the key part

        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        // Check if an email client exists
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}