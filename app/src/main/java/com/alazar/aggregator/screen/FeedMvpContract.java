package com.alazar.aggregator.screen;

import com.alazar.aggregator.base.MvpContract;
import com.alazar.aggregator.model.NewsItem;

import java.util.List;

public interface FeedMvpContract {

    interface Presenter<V extends MvpContract.View> extends MvpContract.Presenter<V> {
        void getFeed(boolean updateRequired);
    }

    interface View extends MvpContract.View {

        void showFeed(List<NewsItem> newsList);

        void registerBroadcastUpdate();

        void showProgressBar();

        void hideProgressBar();
    }

}
