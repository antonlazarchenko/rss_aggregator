package com.alazar.aggregator.model;

public class NewsItem {

    private final String title;
    private final String date;
    private final String link;
    private final String description;
//    private final String image;

    public NewsItem(String title, String date, String link, String description) {
        this.title = title;
        this.date = date;
        this.link = link;
        this.description = description;
//        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {

        String shortDesc = description;
        shortDesc = shortDesc.replaceAll("<(.*?)>", " "); //Removes all items in brackets
        shortDesc = shortDesc.replaceAll("<(.*?)\n", " "); //Must be undeneath
        shortDesc = shortDesc.replaceFirst("(.*?)>", " "); //Removes any connected item to the last bracket
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

//    public String getImage() {
//        return image;
//    }

}
