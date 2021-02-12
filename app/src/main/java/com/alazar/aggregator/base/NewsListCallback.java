package com.alazar.aggregator.base;

import com.alazar.aggregator.model.NewsItem;

import java.util.List;

public interface NewsListCallback {
    void onReady(List<NewsItem> newsList);
}
