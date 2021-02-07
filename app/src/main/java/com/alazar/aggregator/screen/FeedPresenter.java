package com.alazar.aggregator.screen;

import com.alazar.aggregator.rss.ContentProvider;
import com.alazar.aggregator.rss.RssCallback;

import javax.inject.Inject;

public class FeedPresenter implements FeedMvpContract.Presenter<FeedMvpContract.View> {

    private FeedMvpContract.View view;

    ContentProvider provider;

    @Inject
    public FeedPresenter(ContentProvider provider) {
        this.provider = provider;
    }


    @Override
    public void getFeed(RssCallback callback) {
        provider.getFeed(callback);
    }


    @Override
    public void attachView(FeedMvpContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

}
