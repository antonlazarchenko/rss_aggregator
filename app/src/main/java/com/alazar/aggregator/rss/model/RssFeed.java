package com.alazar.aggregator.rss.model;

import androidx.annotation.NonNull;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;


@Xml(name="rss")

public class RssFeed
{
    @Element
    public RssChannel channel;

    @NonNull
    @Override
    public String toString() {
        return "RssFeed [channel=" + channel + "]";
    }
}
