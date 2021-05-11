package com.example.uscfilms;

public class SearchData {
    private String type;
    private String year;
    private String title;
    private String rating;
    private Integer id;
    private String imgUrl;

    public SearchData(String type, String year, String title, String rating, Integer id, String imgUrl) {
        this.type = type;
        this.year = year;
        this.title = title;
        this.rating = rating;
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }
}
