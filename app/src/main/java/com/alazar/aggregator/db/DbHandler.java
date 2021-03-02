package com.alazar.aggregator.db;

import android.content.Context;
import android.util.Log;

import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.di.App;
import com.alazar.aggregator.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DbHandler implements DbProvider {

    private static final String TAG = "DbHandler";

    private static final String FEED_NAME = "newsfeed.realm";

    private final RealmConfiguration realmConfig;

    private List<NewsItem> newsList = new ArrayList<>();

    @Inject
    Context context;

    public DbHandler() {
        App.getComponent().inject(this);

        Realm.init(context);

        realmConfig = new RealmConfiguration.Builder()
            .name(FEED_NAME)
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build();
    }


    @Override
    public List<NewsItem> findAllNewsItems() {

        if (newsList.size() == 0) {
            Realm realm = Realm.getInstance(realmConfig);
            RealmResults<NewsItem> realmResults = realm.where(NewsItem.class).findAllAsync();
            newsList.addAll(realmResults);
         }

        return newsList;
    }

    @Override
    public void saveFreshNewsList(List<NewsItem> list) {
        if (newsList.size() > 0)
            newsList.clear();

        newsList.addAll(list);

        deleteAllFeeds().subscribeOn(Schedulers.io())
            .map(__ ->
                Observable.fromIterable(list)
                .subscribe(newsItem -> {
                    Realm realm = Realm.getInstance(realmConfig);
                    realm.beginTransaction();

                    NewsItem item = realm.createObject(NewsItem.class);
                    item.setData(newsItem);
                    realm.commitTransaction();
                    Log.d(TAG, "TRANSACTION");
                }, Throwable::printStackTrace))
            .subscribe();
    }

    private Observable<Boolean> deleteAllFeeds() {
        return Observable.fromCallable(() -> {
            Realm realm = Realm.getInstance(realmConfig);
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, "CACHE CLEAN");
            return true;
        });
    }
}
