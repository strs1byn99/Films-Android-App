package com.example.uscfilms;

public class SliderData {
    // image url is used to
    // store the url of image
    private String imgUrl;
    private Integer id;
    private String type;

    public String getType() {
        return type;
    }

    public SliderData(String imgUrl, Integer id, String type) {
        this.imgUrl = imgUrl;
        this.id = id;
        this.type = type;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }
    public Integer getId() { return id; }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setId(Integer id) { this.id = id; }
}
