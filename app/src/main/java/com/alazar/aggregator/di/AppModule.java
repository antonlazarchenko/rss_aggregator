package com.alazar.aggregator.di;


import com.alazar.aggregator.rss.ContentProvider;
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
    public FeedMvpContract.Presenter<FeedMvpContract.View> provideFeedPresenter(ContentProvider provider) {
        return new FeedPresenter(provider);
    }

    @Provides
    public ContentProvider providesContent() {
        return new RssService();
    }
}