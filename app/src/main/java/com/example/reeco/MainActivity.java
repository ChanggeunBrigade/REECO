package com.example.reeco;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        GridView gridList = findViewById(R.id.grid_test);
        GridAdapter gridAdt = new GridAdapter(this);

        AppDatabase db = AppDatabase.getInstance(this);
        List<Server> servers = db.serverDao().getServers();

        for(int i = 0; i < servers.size(); i++) {
            gridAdt.setItem(servers.get(i).getName());
        }

        gridList.setAdapter(gridAdt);

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

            }
        });

        Button btnServerAdd = findViewById(R.id.BtnServerAdd);

        btnServerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

                startActivity(intent);
            }
        });

    }
}