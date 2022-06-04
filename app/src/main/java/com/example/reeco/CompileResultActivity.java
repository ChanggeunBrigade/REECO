package com.example.reeco;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

import java.io.IOException;

public class CompileResultActivity extends AppCompatActivity {
    Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_compile_result);

        Intent receivedIntent = getIntent();

        String ip = receivedIntent.getStringExtra("ip");
        int port = receivedIntent.getIntExtra("port", 0);
        String user = receivedIntent.getStringExtra("user");
        String password = receivedIntent.getStringExtra("password");

        Button btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(view -> finish());

        Connection connection = new Connection(ip, port);
        try { // TODO: java.util.concurrent 모듈 이용해서 짜기. 메인스레드에서 돌리면 오류남.
            connection.connect();
            connection.authenticateWithPassword(user, password);
            session = connection.openSession();
        } catch (IOException e) {
            Toast.makeText(this, "연결이 되지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        connection.close();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (session == null) {
            return true;
        }

        Toast.makeText(this, KeyEvent.keyCodeToString(keyCode), Toast.LENGTH_SHORT).show();

        return true;
    }
}