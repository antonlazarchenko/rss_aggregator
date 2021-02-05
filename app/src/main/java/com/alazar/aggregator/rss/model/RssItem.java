package com.alazar.aggregator.rss.model;

import androidx.annotation.NonNull;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;


@Xml(name="item")
public class RssItem
{
    @PropertyElement
    private String title;

    @PropertyElement
    private String link;

    @PropertyElement
    private String pubDate;

    @PropertyElement
    private String description;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "RssItem [title=" + title + ", link=" + link + ", pubDate=" + pubDate
            + ", description=" + description + "]";
    }
}
