package com.alazar.aggregator.model;

public class NewsItem {

    private final String title;
    private final String date;
    private final String link;
    private final String description;

    public NewsItem(String title, String date, String link, String description) {
        this.title = title;
        this.date = date;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {

        String shortDesc = description;
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
