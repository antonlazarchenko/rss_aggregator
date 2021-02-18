package com.alazar.aggregator.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.alazar.aggregator.base.ToastProvider;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;


public class Toaster implements ToastProvider {
    @Inject
    Context context;

    @Inject
    public Toaster() {
        App.getComponent().inject(this);
    }

    public void makeText(Integer intResource) {
        Toast toast = Toast.makeText(context, context.getString(intResource), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -300);
        toast.show();
    }
}
