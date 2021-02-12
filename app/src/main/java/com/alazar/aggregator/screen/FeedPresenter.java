package com.alazar.aggregator.screen;

import com.alazar.aggregator.base.ContentProvider;
import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.base.NewsListCallback;
import com.alazar.aggregator.db.DbHandler;
import com.alazar.aggregator.util.NetworkWrapper;

import javax.inject.Inject;

public class FeedPresenter implements FeedMvpContract.Presenter<FeedMvpContract.View> {

    private FeedMvpContract.View view;

    private ContentProvider contentProvider;

    private DbProvider dbProvider;

    @Inject
    public FeedPresenter(ContentProvider contentProvider, DbProvider dbProvider) {
        this.contentProvider = contentProvider;
        this.dbProvider = dbProvider;
    }


    @Override
    public void getFeed(NewsListCallback callback) {
        if (NetworkWrapper.getInstance().isConnected()) {
            contentProvider.getFeed(news -> {
                dbProvider.saveFreshNewsList(news);
                callback.onReady(news);
            });
        } else {
            dbProvider.findAllNewsItems(callback);
        }
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
