package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import android.text.InputFilter;
import android.text.InputType;

public class MainActivity4 extends AppCompatActivity {
    Button loginbtn4;
    TextView switchto;
    EditText editTextid;
    boolean phone = true; // default to phone
    private String hint = "Enter E-mail", text = "Don't have email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        loginbtn4 = findViewById(R.id.loginbtn4);
        switchto = findViewById(R.id.switchto);
        editTextid = findViewById(R.id.editTextid);

        loginbtn4.setOnClickListener(v -> {
            if (!isValid()) {
                Toast.makeText(this, "Invalid", Toast.LENGTH_LONG).show();
            } else {
                Intent mainactivity5 = new Intent(this, Authenticate.class);
                mainactivity5.putExtra("phone", phone);
                mainactivity5.putExtra("content", editTextid.getText().toString().trim());
                startActivity(mainactivity5);
            }
        });

        switchto.setOnClickListener(v -> {
            String tempHint = editTextid.getHint().toString(), tempText = switchto.getText().toString();
            editTextid.setHint(hint);
            switchto.setText(text);
            hint = tempHint;
            text = tempText;
            phone = !phone; // toggle phone/email mode

            if (!phone) {
                // Switch to email
                editTextid.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                editTextid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
            } else {
                // Switch to phone
                editTextid.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            }
        });
    }

    boolean isValid() {
        if (!phone) {
            return isValidEmail(editTextid.getText().toString());
        }
        return isValidPhoneNumber(editTextid.getText().toString(), "IN");
    }

    public boolean isValidEmail(CharSequence email) {
        if (email == null || email.toString().isEmpty()) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPhoneNumber(String phoneNumber, String region) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, region);
            return phoneUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            return false;
        }
    }
}
