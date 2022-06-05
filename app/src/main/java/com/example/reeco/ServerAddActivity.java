package com.example.reeco;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ServerAddActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_server_add);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String name = "";
        String ip = "";
        int port = 0;
        String user = "";
        String password = "";

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
        EditText edtExecuteCom = findViewById(R.id.edt_execute_compiler);
        Button btnServerAdd = findViewById(R.id.btn_server_confirm);


        edtName.setText(name);
        edtIp.setText(ip);
        if (port != 0) {
            edtPort.setText(String.valueOf(port));
        }
        edtUser.setText(user);
        edtPassword.setText(password);

        AppDatabase database = AppDatabase.getInstance(this);

        edtName.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                edtIp.requestFocus();
                return true;
            }

            return false;
        });

        edtIp.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                edtPort.requestFocus();
                return true;
            }

            return false;
        });

        edtPort.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                edtUser.requestFocus();
                return true;
            }

            return false;
        });

        edtUser.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                edtPassword.requestFocus();
                return true;
            }

            return false;
        });

        edtPassword.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                edtExecuteCom.requestFocus();
                return true;
            }

            return false;
        });

        btnServerAdd.setOnClickListener(v -> {
            boolean isValid = true;

            if (edtName.getText().toString().replace(" ", "").equals("")) {
                edtName.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                isValid = false;
            }
            if (edtIp.getText().toString().replace(" ", "").equals("") ||
                    !edtIp.getText().toString().matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)(\\.(?!$)|$)){4}$")) {
                edtIp.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                isValid = false;
            }
            if (edtPort.getText().toString().replace(" ", "").equals("")) {
                edtPort.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                isValid = false;
            }
            if (edtUser.getText().toString().replace(" ", "").equals("")) {
                edtUser.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                isValid = false;
            }
            if (edtPassword.getText().toString().replace(" ", "").equals("")) {
                edtPassword.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                isValid = false;
            }

            if (edtExecuteCom.getText().toString().replace(" ", "").equals("")) {
                edtExecuteCom.setBackgroundResource(R.drawable.edittext_rectangle_with_redstroke);
                isValid = false;
            }

            if (!isValid) {
                Toast.makeText(this, "입력을 완료해주십시오.", Toast.LENGTH_SHORT).show();
                return;
            }

            Observable<Integer> observable = Observable.just(1);
            //noinspection ResultOfMethodCallIgnored
            observable.subscribeOn(Schedulers.io()).subscribe(tmp -> database.serverDao().insertOrUpdate(new Server(
                    edtName.getText().toString(),
                    edtIp.getText().toString(),
                    Integer.parseInt(edtPort.getText().toString()),
                    edtUser.getText().toString(),
                    edtPassword.getText().toString()
            )));

            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
