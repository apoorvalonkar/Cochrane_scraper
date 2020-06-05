package com.example.cochraneLibraryScraper;

import com.example.cochraneLibraryScraper.model.Topic;
import com.example.cochraneLibraryScraper.model.Topics;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class cochraneLibraryScraper {

    public static StringBuilder scraper(int numPage) {

        Topics topics = new Topics();
        topics.fetchAllTopics();
        StringBuilder outputResultFile = new StringBuilder();
        ArrayList<Topic> topicList = topics.getTopics("Allergy & intolerance");

        for(Topic topic: topicList){
            System.out.println(topic.getReviews(numPage));
            outputResultFile.append(topic.getReviews(numPage));
        }

        return outputResultFile;

    }


    public static void main(String[] args) {
        SpringApplication.run(cochraneLibraryScraper.class, args);
    }
}
