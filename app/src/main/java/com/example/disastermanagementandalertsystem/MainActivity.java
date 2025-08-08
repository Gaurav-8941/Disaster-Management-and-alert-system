package com.example.disastermanagementandalertsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView language;
    ArrayList<String> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        language=findViewById(R.id.language);
        dataList = new ArrayList<>();
        dataList.add("English");
        dataList.add("Marathi: मराठी");
        dataList.add("Hindi: हिन्दी");
        dataList.add("Bengali: বাংলা");
        dataList.add("Telugu: తెలుగు");
        dataList.add("Tamil: தமிழ்");
        dataList.add("Gujarati: ગુજરાતી");
        dataList.add("Urdu: اردو");
        dataList.add("Kannada: ಕನ್ನಡ");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        language.setAdapter(arrayAdapter);

        language.setOnItemClickListener((parent, view, position, id) -> {
            Intent mainactivity2=new Intent(this, MainActivity2.class);
            mainactivity2.putExtra("language", dataList.get(position));
            startActivity(mainactivity2);
        });
    }
    

}