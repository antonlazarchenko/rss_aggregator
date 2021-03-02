package com.alazar.aggregator.base;

import com.alazar.aggregator.model.NewsItem;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface FeedProvider {
    Observable<List<NewsItem>> getFeed();
}
