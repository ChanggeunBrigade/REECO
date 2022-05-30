package com.example.reeco;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

public class CustomDialog extends Dialog {
    private final Context mContext;
    private final String name;

    public CustomDialog(Context mContext, String name) {
        super(mContext);
        this.mContext = mContext;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Button deleteButton = findViewById(R.id.btn_delete);
        Button cancelButton = findViewById(R.id.btn_cancel);

        deleteButton.setOnClickListener(view -> {
            AppDatabase database = AppDatabase.getInstance(mContext);
            ServerDao serverDao = database.serverDao();
            Server target = serverDao.findServerByName(name);
            database.serverDao().delete(target);
            dismiss();
        });

        cancelButton.setOnClickListener(view -> dismiss());
    }
}
