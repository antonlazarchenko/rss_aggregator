package com.alazar.aggregator.screen;

import com.alazar.aggregator.R;
import com.alazar.aggregator.base.FeedProvider;
import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.base.NetworkProvider;
import com.alazar.aggregator.base.ToastProvider;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FeedPresenter implements FeedMvpContract.Presenter<FeedMvpContract.View> {

    private FeedMvpContract.View view;

    private final FeedProvider feedProvider;

    private final DbProvider dbProvider;

    private boolean loadedFreshResult = false;

    @Inject
    NetworkProvider networkProvider;
    @Inject
    ToastProvider toastProvider;

    @Inject
    public FeedPresenter(FeedProvider feedProvider, DbProvider dbProvider) {
        this.feedProvider = feedProvider;
        this.dbProvider = dbProvider;
        App.getComponent().inject(this);
    }


    @Override
    public void getFeed(boolean updateRequired) {
        if (networkProvider.isConnected()
            && (updateRequired || !loadedFreshResult)) {

            feedProvider.getFeed()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(news -> {
                    dbProvider.saveFreshNewsList(news);
                    loadedFreshResult = true;
                    view.showFeed(news);
                });
        } else {
            toastProvider.makeText(R.string.no_internet_cache_loaded);
            view.registerBroadcastUpdate();
            view.showFeed(dbProvider.findAllNewsItems());
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
