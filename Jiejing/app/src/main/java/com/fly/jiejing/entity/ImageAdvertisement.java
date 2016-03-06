package com.fly.jiejing.entity;

/**
 * Created by Administrator on 2015/10/1.
 */
public class ImageAdvertisement {
    private int image;
    private String title;
    private String content;
    private String img_path;
    private String link;
    private String uri_title;
    private int id;

    public ImageAdvertisement(int image) {
        this.image = image;
    }

    public ImageAdvertisement(int image, String link) {
        this.image = image;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUri_title() {
        return uri_title;
    }

    public void setUri_title(String uri_title) {
        this.uri_title = uri_title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
