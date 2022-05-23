package com.example.reeco;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class CustomDialog extends Dialog {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);

        Button deleteButton = findViewById(R.id.btn_delete);
        Button cancelButton = findViewById(R.id.btn_cancel);

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 서버 삭제 시 해야할 기능은 여기에다가 추가하길 바람 오바
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public CustomDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }
}
