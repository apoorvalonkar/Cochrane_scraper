package com.example.cochraneLibraryScraper.model;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Topic {
    private String title;
    private String link_ref;
    private ArrayList<Review> reviews;

    public Topic() {
        reviews = new ArrayList<Review>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink_ref() {
        return link_ref;
    }

    public void setLink_ref(String link_ref) {
        this.link_ref = link_ref;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public String getReviews(int numPage) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        final RequestConfig httpGetConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD)
                                            .setCircularRedirectsAllowed(true).build();
        Set<Review> finalReviewsSet = new HashSet<>();

        String topicNameFinal = this.getTitle();
        String topicUrlFinal = this.getLink_ref();

        HttpGet reviewTopicRequest = null;
        String nextUrl = topicUrlFinal;
        int page = 0;
        // Loop through each results page and add to the set with review entry objects
        while (nextUrl != null && page < numPage) {
            page++;

            try {
                reviewTopicRequest = new HttpGet(nextUrl);
                // add request headers
                reviewTopicRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
                reviewTopicRequest.setConfig(httpGetConfig);
                CloseableHttpResponse reviewTopicResponse = httpClient.execute(reviewTopicRequest);

                try {

                    HttpEntity ent = reviewTopicResponse.getEntity();
                    String responseStr = EntityUtils.toString(ent, "UTF-8");

                    // Scrape the review entries for titles and urls
                    Document parsedReviewResults = Jsoup.parse(responseStr, "UTF-8");
                    Elements reviewLinks = parsedReviewResults.select("div.search-results-item-body > h3 > a");
                    String[] reviewTitles = reviewLinks.eachText().toArray(new String[0]);
                    String[] reviewUrls = reviewLinks.eachAttr("href").toArray(new String[0]);

                    // Scrape the Authors
                    Elements authors = parsedReviewResults.select("div.search-result-authors");
                    String[] reviewAuthors = authors.eachText().toArray(new String[0]);

                    // Scrape the Date
                    Elements publicationDates = parsedReviewResults.select("div.search-result-date");
                    String[] reviewDates = publicationDates.eachText().toArray(new String[0]);

                    // Create Review objects using the scraped info and add them to the data structure
                    Review review_temp = null;
                    String URL_APPEND = "https://www.cochranelibrary.com";
                    for (int i = 0; i < reviewTitles.length; i++) {
                        review_temp = new Review(URL_APPEND + reviewUrls[i],
                                topicNameFinal,
                                reviewTitles[i],
                                reviewAuthors[i],
                                reviewDates[i]);
                        finalReviewsSet.add(review_temp);
                    }
                    // Find the url for the next page of review entries
                    nextUrl = null;
                    Elements nextUrlElements = parsedReviewResults.select("div.pagination-next-link > a");
                    if (!nextUrlElements.isEmpty()) {
                        nextUrl = nextUrlElements.eachAttr("href").toArray(new String[0])[0];
                    }
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reviewTopicResponse.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println(topicNameFinal + ":" + finalReviewsSet.size());
        return toOutputString(finalReviewsSet);
    }

    private static String toOutputString(Set<Review> set) {
        StringBuilder formatString = new StringBuilder();
        Review[] reviews = set.toArray(new Review[0]);
        for (int i = 0; i < reviews.length - 1; i++) {
            formatString.append(reviews[i].toString());
            formatString.append("\n");
        }
        formatString.append(reviews[reviews.length - 1].toString());
        return formatString.toString();
    }


}
