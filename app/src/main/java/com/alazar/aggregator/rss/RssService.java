package com.alazar.aggregator.rss;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alazar.aggregator.base.ContentProvider;
import com.alazar.aggregator.base.NewsListCallback;
import com.alazar.aggregator.model.NewsItem;
import com.alazar.aggregator.rss.model.RssFeed;
import com.alazar.aggregator.rss.model.RssItem;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class RssService implements ContentProvider {

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


    public void getFeed(NewsListCallback callback) {
        newsList = new CopyOnWriteArrayList<>();

        getSourcesUrl().subscribeOn(Schedulers.io())
            .flatMap(Observable::fromIterable)
            .flatMap(url -> rssService.getFeed(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
            .doFinally(() -> {
                Log.d(TAG, "NEWS LIST SIZE === " + newsList.size());
                callback.onReady(newsList);
            })
            .subscribe(this::handleResult, Throwable::printStackTrace);
    }

    private void handleResult(RssFeed rssFeed) {
        for (RssItem item : rssFeed.channel.item) {
            newsList.add(new NewsItem(item.getTitle(), item.getDate(), item.getLink(), item.getDescription()));
        }
    }

    public void runRssFeed(NewsListCallback callback) {

        Call<RssFeed> callAsync = rssService.getFeedCall();
        ArrayList<NewsItem> newsList = new ArrayList<>();
        callAsync.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(@NonNull Call<RssFeed> call, @NonNull Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    RssFeed apiResponse = response.body();

                    for (RssItem item : apiResponse.channel.item) {
                        newsList.add(new NewsItem(item.getTitle(), item.getDate(), item.getLink(), item.getDescription()));
                    }
                    callback.onReady(newsList);

                } else {
                    System.out.println("Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RssFeed> call, @NonNull Throwable t) {
                if (call.isCanceled()) {
                    System.out.println("Call was cancelled forcefully");
                } else {
                    System.out.println("Network Error :: " + t.getLocalizedMessage());
                }
            }
        });
    }
}
