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
        Button btnServerAdd = findViewById(R.id.btn_server_confirm);

        edtName.setText(name);
        edtIp.setText(ip);
        if (port != 0) {
            edtPort.setText(String.valueOf(port));
        }
        edtUser.setText(user);
        edtPassword.setText(password);

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    is_valid[0] = false;
                } else {
                    is_valid[0] = true;
                }
            }
        });

        edtIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$")) {
                    is_valid[0] = true;
                } else {
                    is_valid[0] = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int num = Integer.parseInt(s.toString());

                    if (num < 1 || 65535 < num) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    is_valid[0] = false;
                }
                is_valid[0] = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    is_valid[0] = false;
                } else {
                    is_valid[0] = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    is_valid[0] = false;
                } else {
                    is_valid[0] = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AppDatabase database = AppDatabase.getInstance(this);

        btnServerAdd.setOnClickListener(v -> {
            if (!is_valid[0]) {
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
