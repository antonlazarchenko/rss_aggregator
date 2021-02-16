package com.alazar.aggregator.di;


import android.app.Application;
import android.content.Context;

import com.alazar.aggregator.base.ContentProvider;
import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.db.DbHandler;
import com.alazar.aggregator.rss.RssService;
import com.alazar.aggregator.screen.FeedFragment;
import com.alazar.aggregator.screen.FeedMvpContract;
import com.alazar.aggregator.screen.FeedPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application application;

    AppModule(Application application) {
        this.application = application;
    }

    @Provides
    public Context provideContext() {
        return application.getApplicationContext();
    }

    
    @Provides
    public FeedMvpContract.View provideFeedFragment() {
        return new FeedFragment();
    }

    @Provides
    public FeedMvpContract.Presenter<FeedMvpContract.View> provideFeedPresenter(
        ContentProvider provider,
        DbProvider dbProvider
    ) {
        return new FeedPresenter(provider, dbProvider);
    }

    @Provides
    public ContentProvider providesContent() {
        return new RssService();
    }

    @Provides
    public DbProvider providesDbHandler() {
        return new DbHandler();
    }


}