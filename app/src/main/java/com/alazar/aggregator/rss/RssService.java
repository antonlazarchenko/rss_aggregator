package com.alazar.aggregator.rss;

import android.util.Log;

import com.alazar.aggregator.base.FeedProvider;
import com.alazar.aggregator.model.NewsItem;
import com.alazar.aggregator.rss.model.RssItem;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RssService implements FeedProvider {

    private static final String TAG = "FeedPresenter";

    private final IRssService rssService;

    private List<NewsItem> newsList;

    public RssService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://base.url/")
            .addConverterFactory(TikXmlConverterFactory.create(
                new TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .addTypeConverter(String.class, new HtmlEscapeStringConverter())
                    .build()
            ))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create());

        builder.client(httpClient.build());

        Retrofit retrofit = builder.build();

        rssService = retrofit.create(IRssService.class);
    }


    private Observable<List<String>> getSourcesUrl() {
        return Observable.fromArray(
            new ArrayList<>(Arrays.asList(
                "https://habr.com/ru/rss/best/daily/?fl=ru/feed",
                "https://lifehacker.com/rss",
                "https://dou.ua/lenta/tags/RSS/feed/"
            )));
    }


    @Override
    public Observable<List<NewsItem>> getFeed() {
        newsList = new CopyOnWriteArrayList<>();

        return getSourcesUrl().subscribeOn(Schedulers.io())
            .flatMap(Observable::fromIterable)
            .flatMap(rssService::getFeed)
            .map(rssFeed -> {
                for (RssItem item : rssFeed.channel.item) {
                    newsList.add(new NewsItem(item.getTitle(), item.getDate(), item.getLink(), item.getDescription()));
                }
                Log.d(TAG, "FEED SIZE: " + newsList.size());
                return newsList;
            });
    }

}
