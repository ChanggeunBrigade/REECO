package com.example.reeco;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoadingDialog extends Dialog {
    private final Context mContext;

    public LoadingDialog(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }
}