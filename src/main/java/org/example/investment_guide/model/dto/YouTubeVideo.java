package org.example.investment_guide.model.dto;

public class YouTubeVideo {
    private String title;
    private String url;

    public YouTubeVideo(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
