package com.alazar.aggregator.db;

import android.content.Context;

import com.alazar.aggregator.base.DbProvider;
import com.alazar.aggregator.di.App;
import com.alazar.aggregator.model.NewsItem;
import com.alazar.aggregator.base.NewsListCallback;

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


    public void findAllNewsItems(NewsListCallback callback) {

        if (newsList.size() == 0) {
            Realm realm = Realm.getInstance(realmConfig);
            RealmResults<NewsItem> realmResults = realm.where(NewsItem.class).findAll();
            newsList.addAll(realmResults);
         }

        callback.onReady(newsList);
    }

    public void saveFreshNewsList(List<NewsItem> list) {
        if (newsList.size() > 0)
            newsList.clear();

        newsList.addAll(list);

        deleteAllFeeds();

        Observable.fromIterable(list).subscribeOn(Schedulers.io())
            .subscribe(newsItem -> {
                Realm realm = Realm.getInstance(realmConfig);
                realm.beginTransaction();

                NewsItem item = realm.createObject(NewsItem.class);
                item.setData(newsItem);
                realm.commitTransaction();
                System.out.println("TRANSACTION");
            }, Throwable::printStackTrace);
    }

    private void deleteAllFeeds() {
        new Thread(() -> {
            Realm realm = Realm.getInstance(realmConfig);
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
            realm.close();
        }).start();
    }
}
