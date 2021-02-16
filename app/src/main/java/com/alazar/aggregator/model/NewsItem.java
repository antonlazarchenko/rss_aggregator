package com.alazar.aggregator.model;

import com.tickaroo.tikxml.converter.htmlescape.StringEscapeUtils;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class NewsItem extends RealmObject {

    @Required
    private String title;
    @Required
    private String date;
    @Required
    private String link;
    @Required
    private String description;

    public NewsItem() {}

    public NewsItem(String title, String date, String link, String description) {
        this.title = title;
        this.date = date;
        this.link = link;
        this.description = description;
    }


    public void setData(NewsItem item) {
        title = item.getTitle();
        date = item.getDate();
        link = item.getLink();
        description = item.getDescription();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {

        String shortDesc = StringEscapeUtils.unescapeHtml4(description);
        shortDesc = shortDesc.replaceAll("<(.*?)>", " ");
        shortDesc = shortDesc.replaceAll("<(.*?)\n", " ");
        shortDesc = shortDesc.replaceFirst("(.*?)>", " ");
        shortDesc = shortDesc.replaceAll("&nbsp;", " ");
        shortDesc = shortDesc.replaceAll("&amp;", "&");

        return shortDesc.length() > 120
            ? shortDesc.substring(0, 120).trim() + "..."
            : shortDesc.trim();
    }


    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }

}
