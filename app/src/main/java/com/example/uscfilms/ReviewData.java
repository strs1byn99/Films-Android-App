package com.example.uscfilms;

public class ReviewData {
    private String by;
    private String content;
    private String rating;

    public ReviewData(String by, String content, String rating) {
        this.by = by;
        this.content = content;
        this.rating = rating;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
