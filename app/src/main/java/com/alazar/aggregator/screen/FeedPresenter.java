package com.alazar.aggregator.screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alazar.aggregator.R;
import com.alazar.aggregator.base.ContentProvider;
import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.base.NetworkProvider;
import com.alazar.aggregator.base.ToastProvider;
import com.alazar.aggregator.di.App;
import com.alazar.aggregator.model.NewsItem;

import java.util.List;

import javax.inject.Inject;

public class FeedPresenter implements FeedMvpContract.Presenter<FeedMvpContract.View> {

    private FeedMvpContract.View view;

    private final ContentProvider contentProvider;

    private final DbProvider dbProvider;

    private boolean loadedFreshResult = false;

    @Inject
    NetworkProvider networkProvider;
    @Inject
    ToastProvider toastProvider;

    @Inject
    public FeedPresenter(ContentProvider contentProvider, DbProvider dbProvider) {
        this.contentProvider = contentProvider;
        this.dbProvider = dbProvider;
        App.getComponent().inject(this);
    }


    @Override
    public void getFeed(boolean updateRequired) {
        if (networkProvider.isConnected()
            && (updateRequired || !loadedFreshResult)) {

            contentProvider.getFeed(news -> {
                dbProvider.saveFreshNewsList(news);
                loadedFreshResult = true;
                view.showFeed(news);
            });
        } else {
            toastProvider.makeText(R.string.no_internet_cache_loaded);
            dbProvider.findAllNewsItems(newsList -> {
                view.showFeed(newsList);
            });
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
