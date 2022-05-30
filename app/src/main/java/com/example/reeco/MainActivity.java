package com.example.reeco;

import android.Manifest;
import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    private Dialog mDialog;
    private GridView gridList;

    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    @Override
    public void onBackPressed() {
        backKeyHandler.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        gridList = (GridView) findViewById(R.id.grid_test);
        GridAdapter gridAdt = new GridAdapter(this);

        mDialog = new Dialog(MainActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_dialog);

        AppDatabase db = AppDatabase.getInstance(this);
        

        for(int i = 0; i < servers.size(); i++) {
            gridAdt.setItem(servers.get(i).getName());
        }

        gridList.setAdapter(gridAdt);

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Server server = servers.get(position);
                Intent intent = new Intent(getApplicationContext(), CodeWriteActivity.class);

                intent.putExtra("ip", server.getIp());
                intent.putExtra("port", server.getPort());
                intent.putExtra("user", server.getUser());
                intent.putExtra("password", server.getPassword());

                Toast.makeText(getApplicationContext(), "입력을 완료해주십시오.", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });

        gridList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
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


    }
}