package com.alazar.aggregator.base;

import com.alazar.aggregator.model.NewsItem;

import java.util.List;

public interface DbProvider {

    void findAllNewsItems(NewsListCallback callback);

    void saveFreshNewsList(List<NewsItem> list);

}
