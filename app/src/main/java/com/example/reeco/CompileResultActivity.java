package com.example.reeco;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class CompileResultActivity extends AppCompatActivity {
    Session session;
    String ip;
    int port;
    String user;
    String password;
    String compiler;
    String compileResult;
    EditText resultText;

    Button btnRefresh;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_compile_result);
        Intent receivedIntent = getIntent();

        ip = receivedIntent.getStringExtra("ip");
        port = receivedIntent.getIntExtra("port", 0);
        user = receivedIntent.getStringExtra("user");
        password = receivedIntent.getStringExtra("password");
        compiler = receivedIntent.getStringExtra("compiler");
        compileResult = receivedIntent.getStringExtra("compilerResult");

        Button btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(view -> finish());

        resultText = (EditText) findViewById(R.id.resultText);
        resultText.setText(compileResult);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (session == null || !session.isConnected()) {
            return true;
        }

        Toast.makeText(this, KeyEvent.keyCodeToString(keyCode), Toast.LENGTH_SHORT).show();

        return true;
    }

    private void sendFile(String path, String dest) throws JSchException {
        if (session == null || !session.isConnected()) {
            Toast.makeText(this, "연결 오류입니다.", Toast.LENGTH_SHORT).show();
        }

        Channel channel = session.openChannel("sftp");

    }

    private void connectSSH() throws JSchException {
        session = new JSch().getSession(user, ip, port);
        session.setPassword(password);
        session.connect();
    }
}