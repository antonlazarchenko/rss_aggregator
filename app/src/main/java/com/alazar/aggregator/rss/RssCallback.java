package com.alazar.aggregator.rss;

import com.alazar.aggregator.model.NewsItem;

import java.util.List;

public interface RssCallback {
    void onReady(List<NewsItem> newsList);
}
