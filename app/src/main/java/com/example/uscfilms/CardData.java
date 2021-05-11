package com.example.uscfilms;

public class CardData {
    private String imgUrl;
    private Integer id;
    private String type;
    private String title;

    public CardData(String imgUrl, Integer id, String type, String title) {
        this.imgUrl = imgUrl;
        this.id = id;
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

