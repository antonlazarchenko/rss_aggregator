package com.alazar.aggregator.screen;

import com.alazar.aggregator.base.MvpContract;
import com.alazar.aggregator.base.NewsListCallback;

public interface FeedMvpContract {

    interface Presenter<V extends MvpContract.View> extends MvpContract.Presenter<V> {
        void getFeed(boolean updateRequired, NewsListCallback callback);
    }

    interface View extends MvpContract.View {

        void showProgressBar();

        void hideProgressBar();
    }

}
