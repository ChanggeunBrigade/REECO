package com.example.reeco;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.amrdeveloper.codeview.CodeView;
import com.example.reeco.syntax.LanguageManager;
import com.example.reeco.syntax.LanguageName;
import com.example.reeco.syntax.ThemeName;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class CodeWriteActivity extends AppCompatActivity {
    CodeView edtCodeWrite;
    TextView txtFilename;
    Uri uri;
    Toolbar toolbar;
    ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 다크모드 무효화
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_code_compile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button btnCompile = findViewById(R.id.btn_compile);

        txtFilename = findViewById(R.id.txtFilename);
        edtCodeWrite = findViewById(R.id.edt_codeWrite);

        Intent receivedIntent = getIntent();

        String ip = receivedIntent.getStringExtra("ip");
        int port = receivedIntent.getIntExtra("port", 0);
        String user = receivedIntent.getStringExtra("user");
        String password = receivedIntent.getStringExtra("password");

        btnCompile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CompileResultActivity.class);

            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            intent.putExtra("user", user);
            intent.putExtra("password", password);

            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 1 || resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }

        uri = data.getData();

        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();

        txtFilename.setText(returnCursor.getString(nameIndex));


        LanguageManager langManager = new LanguageManager(this, edtCodeWrite);

        String[] split = uri.getPath().split("[.]");
        String fileExt = split[split.length - 1];

        switch (fileExt) {
            case "java":
                langManager.applyTheme(LanguageName.JAVA, ThemeName.WHITE);
                break;
            case "py":
                langManager.applyTheme(LanguageName.PYTHON, ThemeName.WHITE);
                break;
            case "go":
                langManager.applyTheme(LanguageName.GO_LANG, ThemeName.WHITE);
                break;
        }

        try {
            edtCodeWrite.setText(readTextFromUri(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.code_write_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openFile:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                //noinspection deprecation
                startActivityForResult(intent, 1);

                break;

            case R.id.saveFile:
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                            Objects.requireNonNull(outputStream)));
                    writer.write(edtCodeWrite.getText().toString());
                    Toast.makeText(CodeWriteActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
