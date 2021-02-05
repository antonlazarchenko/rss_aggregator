package com.alazar.aggregator.rss;

import com.alazar.aggregator.rss.model.RssFeed;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IRssService {
    @GET
    Observable<RssFeed> getFeed(@Url String url);

    @GET()
    Call<RssFeed> getFeedCall();
}
