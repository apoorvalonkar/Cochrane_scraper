package com.example.cochraneLibraryScraper.model;

public class Review {
    private String url;
    private String topicName;
    private String title;
    private String author;
    private String date;

    private Topic topic;

    public Review(String url, String topicName, String title, String author, String date) {
        this.url = url;
        this.topicName = topicName;
        this.title = title;
        this.author = author;
        this.date = date;
    }

    public Review(Topic topic) {
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public Topic getTopic() {
        return topic;
    }

    // ToString override to format it to project specifications
    @Override
    public String toString() {
        String delim = "|";
        return String.join(delim, this.url, this.topicName, this.title, this.author, this.date);
    }

}