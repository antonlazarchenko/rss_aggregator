package com.alazar.aggregator.di;

import android.app.Application;
import android.content.Context;

import com.alazar.aggregator.base.NetworkProvider;
import com.alazar.aggregator.base.ToastProvider;
import com.alazar.aggregator.util.Networker;
import com.alazar.aggregator.util.Toaster;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseModule {
    private final Application application;

    BaseModule(Application application) {
        this.application = application;
    }

    @Provides
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    public NetworkProvider provideNetworkProvider() {
        return new Networker();
    }

    @Provides
    public ToastProvider provideToastProvider() {
        return new Toaster();
    }

}
