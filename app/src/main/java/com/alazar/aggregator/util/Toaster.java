package com.alazar.aggregator.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.alazar.aggregator.di.App;

import javax.inject.Inject;


public class Toaster {
    @Inject
    Context context;

    private static Toaster instance;

    public static Toaster getInstance() {
        if (instance == null) {
            instance = new Toaster();
        }
        return instance;
    }

    protected Toaster() {
        App.getComponent().inject(this);
    }

    public void makeText(Integer intResoirce) {
        Toast toast = Toast.makeText(context, context.getString(intResoirce), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, -300);
        toast.show();
    }

}
