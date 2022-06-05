package com.example.reeco;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CustomDialog extends Dialog {
    private final Context mContext;
    private final String serverName;

    public CustomDialog(Context mContext, String name) {
        super(mContext);
        this.mContext = mContext;
        serverName = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Button deleteButton = findViewById(R.id.btn_delete);
        Button cancelButton = findViewById(R.id.btn_cancel);

        AppDatabase database = AppDatabase.getInstance(mContext);
        ServerDao serverDao = database.serverDao();

        Observable<String> nameObservable = Observable.just(serverName);

        deleteButton.setOnClickListener(view -> {
            //noinspection ResultOfMethodCallIgnored
            nameObservable.subscribeOn(Schedulers.io()).subscribe(name -> {
                Server target = serverDao.findServerByName(serverName);
                database.serverDao().delete(target);
            });
            dismiss();
        });

        cancelButton.setOnClickListener(view -> dismiss());
    }
}
