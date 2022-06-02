package com.example.reeco;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private final BackKeyHandler backKeyHandler = new BackKeyHandler(this);
    private GridView gridList;

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

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        gridList = findViewById(R.id.grid_test);
        GridAdapter gridAdt = new GridAdapter(this);

        Dialog mDialog = new Dialog(MainActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_dialog);

        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        db.serverDao().getServers().observe(this, servers -> {
            gridAdt.clear();
            gridAdt.setAllItems(servers);
            gridList.setAdapter(gridAdt);

            gridList.setOnItemClickListener((adapterView, view, position, id) -> {
                Server server = servers.get(position);
                Intent intent = new Intent(getApplicationContext(), CodeWriteActivity.class);

                intent.putExtra("ip", server.getIp());
                intent.putExtra("port", server.getPort());
                intent.putExtra("user", server.getUser());
                intent.putExtra("password", server.getPassword());

                Toast.makeText(getApplicationContext(), "입력을 완료해주십시오.", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            });

            gridList.setOnItemLongClickListener((adapterView, view, position, id) -> {
                CustomDialog dlg = new CustomDialog(MainActivity.this, gridAdt.getItemString(position));
                dlg.show();

                return false;
            });
        });

        Button btnServerAdd = findViewById(R.id.btn_server_add);

        btnServerAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ServerAddActivity.class);

            startActivity(intent);
        });


    }
}