package com.alazar.aggregator.screen;

import androidx.lifecycle.LiveData;

import com.alazar.aggregator.base.MvpContract;
import com.alazar.aggregator.model.NewsItem;

import java.util.List;

public interface FeedMvpContract {

    interface Presenter<V extends MvpContract.View> extends MvpContract.Presenter<V> {
        void callFeed();
        LiveData<List<NewsItem>> getNewsFeed();
    }

    interface View extends MvpContract.View {

        void showProgressBar();

        void hideProgressBar();
    }

}
