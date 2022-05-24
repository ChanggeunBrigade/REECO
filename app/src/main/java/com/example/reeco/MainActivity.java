package com.example.reeco;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Dialog mDialog;
    private GridView gridList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        gridList = (GridView) findViewById(R.id.grid_test);

        mDialog = new Dialog(MainActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_dialog);

        updateItems();

        Button btnServerAdd = findViewById(R.id.btn_server_add);

        btnServerAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

            startActivity(intent);
            updateItems();
        });
    }

    private void updateItems() {
        AppDatabase db = AppDatabase.getInstance(this);
        List<Server> servers = db.serverDao().getServers();
        GridAdapter gridAdt = new GridAdapter(this);

        for (int i = 0; i < servers.size(); i++) {
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
            updateItems();
        });

        gridList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            CustomDialog dlg = new CustomDialog(MainActivity.this);

            dlg.show();

            updateItems();

            return false;
        });
    }
}