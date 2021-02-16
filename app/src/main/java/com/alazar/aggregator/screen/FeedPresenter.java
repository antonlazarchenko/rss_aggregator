package com.alazar.aggregator.screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alazar.aggregator.R;
import com.alazar.aggregator.base.ContentProvider;
import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.base.NewsListCallback;
import com.alazar.aggregator.model.NewsItem;
import com.alazar.aggregator.util.Networker;
import com.alazar.aggregator.util.Toaster;

import java.util.List;

import javax.inject.Inject;

public class FeedPresenter implements FeedMvpContract.Presenter<FeedMvpContract.View> {

    private FeedMvpContract.View view;

    private final ContentProvider contentProvider;

    private final DbProvider dbProvider;

    private MutableLiveData<List<NewsItem>> mutableList = new MutableLiveData<List<NewsItem>>();

    @Inject
    public FeedPresenter(ContentProvider contentProvider, DbProvider dbProvider) {
        this.contentProvider = contentProvider;
        this.dbProvider = dbProvider;
    }

    public LiveData<List<NewsItem>> getNewsFeed() {
        return mutableList;
    }


    @Override
    public void callFeed() {
        if (Networker.getInstance().isConnected()) {
            contentProvider.getFeed(news -> {
                dbProvider.saveFreshNewsList(news);
                mutableList.postValue(news);
            });
        } else {
            Toaster.getInstance().makeText(R.string.no_internet_cache_loaded);
            dbProvider.findAllNewsItems(newsList -> mutableList.postValue(newsList));
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
