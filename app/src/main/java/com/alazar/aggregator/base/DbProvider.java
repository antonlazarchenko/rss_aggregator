package com.alazar.aggregator.base;

import com.alazar.aggregator.model.NewsItem;

import java.util.List;

public interface DbProvider {

    List<NewsItem> findAllNewsItems();

    void saveFreshNewsList(List<NewsItem> list);

}
