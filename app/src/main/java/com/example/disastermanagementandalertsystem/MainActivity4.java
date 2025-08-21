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

public class MainActivity4 extends AppCompatActivity {
        Button loginbtn4;
        TextView switchto;
        EditText editTextid;
        boolean phone = false;
        private String hint="Enter E-mail",text="Don't have email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        loginbtn4=findViewById(R.id.loginbtn4);
        switchto =findViewById(R.id.switchto);
        editTextid=findViewById(R.id.editTextid);
        loginbtn4.setOnClickListener(v -> {
            if (!isValid()) {
                Toast.makeText(this,"Invaild",Toast.LENGTH_LONG);
            }else {
                Intent mainactivity5 = new Intent(this, Authenticate.class);
                mainactivity5.putExtra("phone",phone);
                mainactivity5.putExtra("content",editTextid.getText().toString());
                startActivity(mainactivity5);
            }
        });

        switchto.setOnClickListener(v ->
        {
           String tempHint = editTextid.getHint().toString(),tempText = switchto.getText().toString();
           editTextid.setHint(hint);
           switchto.setText(text);
           hint = tempHint;
           text = tempText;
        });
    }
    boolean isValid() {
        if (switchto.getText().toString().equalsIgnoreCase("Don't have email")){
            return isValidEmail(editTextid.getText().toString());
        }
        return isValidPhoneNumber(editTextid.getText().toString(),"+91");
    }
    public boolean isValidEmail(CharSequence email) {
        // Return false if the email is null or empty
        if (email == null || email.toString().isEmpty()) {
            return false;
        }
        // Use the built-in Android pattern for email validation
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public boolean isValidPhoneNumber(String phoneNumber, String countryCode) {
        phone = true;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, countryCode);
            // isValidNumber() checks if the number is a possible number for the region.
            return phoneUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
            return false;
        }

    }
}

