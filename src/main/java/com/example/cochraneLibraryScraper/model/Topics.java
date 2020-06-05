package com.example.cochraneLibraryScraper.model;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Topics {
    //input Url
    private static final String baseURL = "https://www.cochranelibrary.com/cdsr/reviews/topics";

    private ArrayList<Topic> topics = new ArrayList<Topic>();

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public ArrayList<Topic> getTopics() {
        return topics;
    }

    //method overloading to get specific topics reviews
    public ArrayList<Topic> getTopics(String topicName) {

        ArrayList<Topic> result = new ArrayList<Topic>();

        for (Topic topic : this.topics) {
            System.out.println(topic.getTitle());
            if (topic.getTitle().equals(topicName)) {
                result.add(topic);
            }
        }
        return result;
    }

    //This method fetches all the topics from Cochrane library

    public void fetchAllTopics() {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(baseURL);
            // add request headers
            request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    Document parsedPage = Jsoup.parse(result, "UTF-8");
                    Elements topicLinks = parsedPage.select("li.browse-by-list-item > a");

                    for (Element topic : topicLinks) {
                        Topic tname = new Topic();
                        tname.setTitle(topic.text());
                        tname.setLink_ref(topic.attr("abs:href"));
                        this.addTopic(tname);

                    }
                }
            } catch (RuntimeException e) { e.printStackTrace(); }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the HTTP client
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Fetch all topics completed");
    }

}