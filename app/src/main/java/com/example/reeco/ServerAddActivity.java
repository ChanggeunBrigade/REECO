package com.example.reeco;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ServerAddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_server_add);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String name = "";
        String ip = "";
        int port = 0;
        String user = "";
        String password = "";

        final boolean[] is_valid = {false};

        if (extras != null) {
            name = extras.getString("name", "");
            ip = extras.getString("ip", "");
            port = extras.getInt("port", 0);
            user = extras.getString("user", "");
            password = extras.getString("password", "");
        }

        EditText edtName = findViewById(R.id.edt_server_name);
        EditText edtIp = findViewById(R.id.edt_ip_addr);
        EditText edtPort = findViewById(R.id.edt_port_num);
        EditText edtUser = findViewById(R.id.edt_user_name);
        EditText edtPassword = findViewById(R.id.edt_password);
        EditText edtexecuteCom = findViewById(R.id.edt_execute_compiler);
        Button btnServerAdd = findViewById(R.id.btn_server_confirm);



        edtName.setText(name);
        edtIp.setText(ip);
        if (port != 0) {
            edtPort.setText(String.valueOf(port));
        }
        edtUser.setText(user);
        edtPassword.setText(password);

        AppDatabase database = AppDatabase.getInstance(this);

        btnServerAdd.setOnClickListener(v -> {
            if (edtName.getText().toString().replace(" ", "").equals("")
                    || edtIp.getText().toString().replace(" ", "").equals("")
                    || edtPort.getText().toString().replace(" ", "").equals("")
                    || edtUser.getText().toString().replace(" ", "").equals("")
                    || edtPassword.getText().toString().replace(" ", "").equals("")
                    || edtexecuteCom.getText().toString().replace(" ", "").equals("")) {
                if (edtName.getText().toString().replace(" ", "").equals("")) {
                    edtName.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                }
                if (edtIp.getText().toString().replace(" ", "").equals("")) {
                    edtIp.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                }
                if (edtPort.getText().toString().replace(" ", "").equals("")) {
                    edtPort.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                }
                if (edtUser.getText().toString().replace(" ", "").equals("")) {
                    edtUser.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                }
                if (edtPassword.getText().toString().replace(" ", "").equals("")) {
                    edtPassword.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                }

                if (edtexecuteCom.getText().toString().replace(" ", "").equals("")) {
                    edtexecuteCom.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                }

                Toast.makeText(this, "입력을 완료해주십시오.", Toast.LENGTH_SHORT).show();
                return;
            }
            database.serverDao().insertOrUpdate(new Server(
                    edtName.getText().toString(),
                    edtIp.getText().toString(),
                    Integer.parseInt(edtPort.getText().toString()),
                    edtUser.getText().toString(),
                    edtPassword.getText().toString()
            ));

            finish();
        });
    }
}
