package com.example.reeco;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_main);

        m_grid = (GridView) findViewById(R.id.grid_test);
        m_gridAdt = new GridAdapter(this);

        for(int i = 0; i < 5; i++) {
            String strNo = "서버 " + i;
            m_gridAdt.setItem(strNo);
        }

        m_grid.setAdapter(m_gridAdt);

        Button btnServerAdd = (Button) findViewById(R.id.BtnServerAdd);

        btnServerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

                startActivity(intent);
            }
        });


    }
}