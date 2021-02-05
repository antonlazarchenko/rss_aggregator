package com.alazar.aggregator.rss.model;

import androidx.annotation.NonNull;


import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name="channel")
public class RssChannel
{
    @PropertyElement
    private String title;


    @Element
    public List<RssItem> item;

    @NonNull
    @Override
    public String toString() {
        return "Channel [item=" + item + "]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
