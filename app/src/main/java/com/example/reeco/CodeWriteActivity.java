package com.example.reeco;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class CodeWriteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_code_compile);

        EditText EdtcodeWrite = findViewById(R.id.edt_codeWrite);
        Button btnOpenFile = findViewById(R.id.btn_openFile);
        Button btnSaveFile = findViewById(R.id.btn_saveFile);
        Button btnCompile = findViewById(R.id.btn_compile);

        btnCompile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CompileResultActivity.class);

            startActivity(intent);
        });

        btnOpenFile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/*");
            startActivityForResult(intent, 1);

            String data = intent.getStringExtra("data");
            System.out.println(data);
            EdtcodeWrite.setText(data);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                Intent intent = new Intent("data", uri);
                intent.putExtra("data", uri);
            }
        }
    }
}
