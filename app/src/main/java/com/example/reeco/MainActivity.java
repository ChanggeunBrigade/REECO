package com.example.reeco;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

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
        checkFilePermission(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

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

    public static void checkFilePermission(Context mContext) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) && !isFileGranted(mContext)){
            Log.i("---","---");
            Log.e("//===========//","================================================");
            Log.i("","\n"+"[C_Permission >> checkFilePermission() :: 앱 파일 접근 권한 상태 확인]");
            Log.i("","\n"+"[상태 :: "+"앱 파일 접근 권한 부여되지 않은 상태 >> 앱 파일 접근 권한 설정 창 이동 실시"+"]");
            Log.e("//===========//","================================================");
            Log.i("---","---");

            // [안드로이드 R 버전 이상 파일 접근 권한 필요]
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
            intent.setData(uri);
            mContext.startActivity(intent);
        }
        else if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) && isFileGranted(mContext)) {
            Log.i("---","---");
            Log.w("//===========//","================================================");
            Log.i("","\n"+"[C_Permission >> checkFilePermission() :: 앱 파일 접근 권한 상태 확인]");
            Log.i("","\n"+"[상태 :: "+"앱 파일 접근 권한 부여된 상태"+"]");
            Log.w("//===========//","================================================");
            Log.i("---","---");
        }
        else {
            Log.i("---","---");
            Log.d("//===========//","================================================");
            Log.i("","\n"+"[C_Permission >> checkFilePermission() :: 앱 파일 접근 권한 상태 확인]");
            Log.i("","\n"+"[상태 :: "+"Android 11 버전 미만 단말기"+"]");
            Log.d("//===========//","================================================");
            Log.i("---","---");
        }
    }

    @TargetApi(Build.VERSION_CODES.R)
    private static boolean isFileGranted(Context mContext) {

        boolean granted = false;

        try {
            if(Environment.isExternalStorageManager() == true) {
                granted = true;
            }
            else {
                granted = false;
            }

        } catch (Throwable why) {
            why.printStackTrace();
        }

        return granted;
    }
}