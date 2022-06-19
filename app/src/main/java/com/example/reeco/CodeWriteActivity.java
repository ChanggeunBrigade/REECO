package com.example.reeco;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CodeWriteActivity extends AppCompatActivity implements PickiTCallbacks {
    private CodeView edtCodeWrite;
    private TextView txtFilename;
    private Uri uri;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private LinearLayout layoutFindText;
    private EditText edtFindText;
    private Button btnCompile;
    private ArrayList<Integer> findTextList;
    private int findTextIndex;
    private Button btnFindPrev;
    private Button btnFindNext;
    private Button btnFindExit;
    private FindText findText;
    private TextView testView;

    private Channel channel = null;
    private ChannelSftp channelSftp = null;
    private Session session;
    private ChannelExec channelExec;
    private String src;
    private String fileNameWithExt;
    private String fileNameWithoutExt;
    private File compileFile;
    String compilerResult;
    String tmp = "/tmp";
    PickiT pickiT;

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
        btnFindExit = findViewById(R.id.btnFindExit);
        testView = findViewById(R.id.testView);

        pickiT = new PickiT(this, this, this);

        Intent receivedIntent = getIntent();

        String ip = receivedIntent.getStringExtra("ip");
        int port = receivedIntent.getIntExtra("port", 0);
        String user = receivedIntent.getStringExtra("user");
        String password = receivedIntent.getStringExtra("password");
        String compiler = receivedIntent.getStringExtra("compiler");

        btnCompile.setOnClickListener(view -> {
            src = testView.getText().toString();
            compileFile = new File(src);

            fileNameWithExt="";
            fileNameWithoutExt="";

            String[] slashDivide = src.split("[/]");
            fileNameWithExt = slashDivide[slashDivide.length - 1];

            String[] dotDivide = fileNameWithExt.split(("[.]"));
            for(int i=0; i<dotDivide.length-1; i++){
                if(i == 0) {
                    fileNameWithoutExt = fileNameWithoutExt + dotDivide[i];
                }else
                    fileNameWithoutExt = fileNameWithoutExt + "." + dotDivide[i];
            }

            System.out.println("확장자 포함 = " + fileNameWithExt + " 확장자 미포함 = " + fileNameWithoutExt);

            Intent intent = new Intent(getApplicationContext(), CompileResultActivity.class);

            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            intent.putExtra("user", user);
            intent.putExtra("password", password);
            intent.putExtra("compiler", compiler);

            Thread thread = new Thread(() -> {
                compilerResult = getSSHResponse(user, port, ip, password, src, tmp, compileFile, fileNameWithExt, fileNameWithoutExt);
                intent.putExtra("compilerResult", compilerResult);
                startActivity(intent);
            });

            thread.start();
            thread.interrupt();
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
                edtCodeWrite.setSelection(curIndex, curIndex + findText.findString.length());
                findTextIndex = (findTextIndex + 1) % findTextList.size();
            } catch (Exception ignored) {

            }
        });

        btnFindPrev.setOnClickListener(v -> {
            try {
                int curIndex = findTextList.get(findTextIndex);
                edtCodeWrite.requestFocus();
                edtCodeWrite.setSelection(curIndex, curIndex + findText.findString.length());
                findTextIndex = (findTextIndex + findTextList.size() - 1) % findTextList.size();
            } catch (Exception ignored) {

            }
        });

        btnFindExit.setOnClickListener(v -> {
            try {
                layoutFindText.setVisibility(View.GONE);
            } catch (Exception ignored) {

            }
        });
    }

    public void connectSSH(String user, int port, String ip, String password) {
        System.out.println("==> Connecting to " + ip);
        System.out.println("==> this port " + port);

        JSch jsch = new JSch();

        try {
            session = jsch.getSession(user, ip, port);
            session.setPassword(password);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            System.out.println("연결오류");
            e.printStackTrace();
        }
        channelSftp = (ChannelSftp) channel;
    }

    public void upload(String src, String dst, File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp.cd(dst);
            channelSftp.put(src, dst);
        } catch (JSchException je) {
            je.printStackTrace();
        } catch (FileNotFoundException fe) {
            System.out.println("파일 없음");
            fe.printStackTrace();
        } catch (SftpException se) {
                se.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public void command(String fileExt, String fileNotExt) {
        try {
            channelExec = (ChannelExec) session.openChannel("exec");

            // cd /tmp + javac(.java => .class) 파일이름 + java 파일이름
            channelExec.setCommand("cd /tmp && javac " + fileExt + "&&java " + fileNotExt);

        } catch (JSchException je) {
            System.out.println("커맨드 오류");
            je.printStackTrace();
        } finally {
            this.disConnectSSH();
        }
    }

    public String getSSHResponse(String user, int port, String ip, String password,
                                 String src, String tmp, File compileFile, String fileExt, String fileNotExt) {
        connectSSH(user, port, ip, password);
        StringBuilder response = null;

        try {
            upload(src, tmp, compileFile);

            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand("cd /tmp && java " + fileExt);

            System.out.println("cd /tmp && java " + fileExt);
            InputStream inputStream = channelExec.getInputStream();
            channelExec.connect();

            byte[] buffer = new byte[8192];
            int decodedLength;

            response = new StringBuilder();
            while ((decodedLength = inputStream.read(buffer, 0, buffer.length)) > 0) {
                response.append(new String(buffer, 0, decodedLength));
            }

        } catch (JSchException je) {
            je.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } finally {
            this.disConnectSSH();
        }
        System.out.println("기록 완료");
        return response.toString();
    }

    private void disConnectSSH() {
        if (session != null) session.disconnect();
        if (channelExec != null) channelExec.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();

        Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();

        if (resultCode == RESULT_OK) {
            pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
        }

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
                    writer.flush();
                    writer.close();
                    Toast.makeText(CodeWriteActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    outputStream.close();
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

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        if (wasSuccessful) {
            testView.setText(path);
        }
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {

    }
}
