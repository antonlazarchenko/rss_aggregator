package com.alazar.aggregator.di;


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
    public FeedMvpContract.Presenter<FeedMvpContract.View> provideFeedPresenter() {
        return new FeedPresenter();
    }


}