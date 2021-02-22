package com.alazar.aggregator.di;


import com.alazar.aggregator.base.FeedProvider;
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

    @Provides
    public FeedMvpContract.View provideFeedFragment() {
        return new FeedFragment();
    }

    @Provides
    public FeedMvpContract.Presenter<FeedMvpContract.View> provideFeedPresenter(
        FeedProvider provider,
        DbProvider dbProvider
    ) {
        return new FeedPresenter(provider, dbProvider);
    }

    @Provides
    public FeedProvider providesContent() {
        return new RssService();
    }

    @Provides
    public DbProvider providesDbHandler() {
        return new DbHandler();
    }


}