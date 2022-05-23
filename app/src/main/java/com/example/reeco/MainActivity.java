package com.example.reeco;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

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
        GridAdapter gridAdt = new GridAdapter(this);

        mDialog = new Dialog(MainActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_dialog);

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

        gridList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                CustomDialog dlg = new CustomDialog(MainActivity.this);
                dlg.show();

                return false;
            }
        });

        Button btnServerAdd = findViewById(R.id.btn_server_add);

        btnServerAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

            startActivity(intent);
        });

        
        // 테스트 용도로 만들어둔 이벤트 리스너, 그리드 아이템은 안되는데 이건 됨
        btnServerAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CustomDialog dlg = new CustomDialog(MainActivity.this);
                dlg.show();

                return false;
            }
        });
    }
}