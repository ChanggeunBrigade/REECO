package com.example.reeco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    private GridView m_grid;
    private GridAdapter m_gridAdt;
    Button btnServerAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        m_grid = findViewById(R.id.grid_test);
        m_gridAdt = new GridAdapter(this);

        for(int i = 0; i < 9; i++) {
            String strNo = "서버 " + i;
            m_gridAdt.setItem(strNo);
        }

        m_grid.setAdapter(m_gridAdt);

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