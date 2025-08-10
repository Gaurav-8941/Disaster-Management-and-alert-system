package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity4 extends AppCompatActivity {
        Button loginbtn4;
        TextView switchto;
        EditText editTextid;
        private String hint="Enter E-mail",text="Don't have email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        loginbtn4=findViewById(R.id.loginbtn4);
        switchto =findViewById(R.id.switchto);
<<<<<<< HEAD
=======
        editTextid=findViewById(R.id.editTextid);
>>>>>>> ecfc175 (uploading files)
        loginbtn4.setOnClickListener(v -> {
            Intent mainactivity5 =new Intent(this, MainActivity5.class);
            startActivity(mainactivity5);
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
}