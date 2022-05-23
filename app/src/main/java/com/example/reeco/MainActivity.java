package com.example.reeco;

import android.content.Intent;
import android.os.Bundle;
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

        gridList.setOnItemClickListener((parent, view, position, id) -> {
            Server server = servers.get(position);
            Intent intent = new Intent(getApplicationContext(), CodeWriteActivity.class);

            intent.putExtra("ip", server.getIp());
            intent.putExtra("port", server.getPort());
            intent.putExtra("user", server.getUser());
            intent.putExtra("password", server.getPassword());

            startActivity(intent);
        });

        gridList.setOnItemLongClickListener((parent, view, position, id) -> {
            Server server = servers.get(position);
            Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

            intent.putExtra("name", server.getName());
            intent.putExtra("ip", server.getIp());
            intent.putExtra("port", server.getPort());
            intent.putExtra("user", server.getUser());
            intent.putExtra("password", server.getPassword());

            startActivity(intent);

            return true;
        });

        Button btnServerAdd = findViewById(R.id.btn_server_add);

        btnServerAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

            startActivity(intent);
        });

    }
}