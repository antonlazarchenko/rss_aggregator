package com.alazar.aggregator.screen;

import com.alazar.aggregator.base.MvpContract;
import com.alazar.aggregator.rss.RssCallback;

public interface FeedMvpContract {

    interface Presenter<V extends MvpContract.View> extends MvpContract.Presenter<V> {
        void getFeed(RssCallback callback);
    }

    interface View extends MvpContract.View {

        void showProgressBar();

        void hideProgressBar();
    }

}
