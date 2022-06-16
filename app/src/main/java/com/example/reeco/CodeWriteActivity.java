package com.example.reeco;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Objects;

public class CodeWriteActivity extends AppCompatActivity {
    final CodeView edtCodeWrite;
    final TextView txtFilename;
    final Uri uri;
    final Toolbar toolbar;
    final ActionBar actionBar;
    final LinearLayout layoutFindText;
    final EditText edtFindText;
    final Button btnCompile;
    ArrayList<Integer> findTextList;
    int findTextIndex;
    final Button btnFindPrev;
    final Button btnFindNext;
    final FindText findText;

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
        btnCompile = findViewById(R.id.btn_compile);
        txtFilename = findViewById(R.id.txtFilename);
        edtCodeWrite = findViewById(R.id.edt_codeWrite);
        layoutFindText = findViewById(R.id.layoutFindText);
        edtFindText = findViewById(R.id.edtFindText);
        btnFindNext = findViewById(R.id.btnFindNext);
        btnFindPrev = findViewById(R.id.btnFindPrev);

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

        findText = new FindText();

        edtCodeWrite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2) {
                    return;
                }

                findText.setMainString(s.toString());
                try {
                    findText.search();
                } catch (Exception ignored) {

                }
                findTextIndex = 0;
            }
        });

        edtFindText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 2) {
                    return;
                }

                findText.setFindString(s.toString());
                try {
                    findTextList = findText.search();
                    findTextIndex = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnFindNext.setOnClickListener(v -> {
            try {
                int curIndex = findTextList.get(findTextIndex);
                edtCodeWrite.requestFocus();
                edtCodeWrite.setSelection(curIndex, curIndex + findText.findString.length() - 1);
                findTextIndex = (findTextIndex + 1) % findTextList.size();
            } catch (Exception ignored) {

            }
        });

        btnFindPrev.setOnClickListener(v -> {
            try {
                int curIndex = findTextList.get(findTextIndex);
                edtCodeWrite.requestFocus();
                edtCodeWrite.setSelection(curIndex, curIndex + findText.findString.length() - 1);
                findTextIndex = (findTextIndex + findTextList.size() - 1) % findTextList.size();
            } catch (Exception ignored) {

            }
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

        String[] split = returnCursor.getString(nameIndex).split("[.]");
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
    public boolean onPrepareOptionsMenu(Menu menu) {


        return true;
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

                Menu menu = toolbar.getMenu();

                MenuItem menuSave = menu.findItem(R.id.saveFile);
                MenuItem menuSearch = menu.findItem(R.id.searchText);

                menuSave.setEnabled(true);
                menuSearch.setEnabled(true);

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
            case R.id.searchText:
                layoutFindText.setVisibility(View.VISIBLE);
                break;

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
