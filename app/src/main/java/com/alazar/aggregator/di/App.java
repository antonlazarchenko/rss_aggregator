package com.alazar.aggregator.di;

import android.app.Application;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().baseModule(new BaseModule(this)).build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}