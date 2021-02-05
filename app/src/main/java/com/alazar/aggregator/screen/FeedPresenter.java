package com.alazar.aggregator.screen;

import android.util.Log;

import com.alazar.aggregator.rss.RssCallback;
import com.alazar.aggregator.rss.RssService;

import javax.inject.Inject;

public class FeedPresenter implements FeedMvpContract.Presenter<FeedMvpContract.View> {

    private FeedMvpContract.View view;

    @Inject
    public FeedPresenter() {}


    @Override
    public void getFeed(RssCallback callback) {
        RssService.getInstance().getFeed(callback::onReady);
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
