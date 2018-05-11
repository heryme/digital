package com.digitalscale.model;

/**
 * Created by Vishal Gadhiya on 4/19/2017.
 */

public class TipsModel {

    private String tipId;
    private String title;
    private String image;
    private String description;
    private String contentPageUrl;

    public String getTipId() {
        return tipId;
    }

    public void setTipId(String tipId) {
        this.tipId = tipId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentPageUrl() {
        return contentPageUrl;
    }

    public void setContentPageUrl(String contentPageUrl) {
        this.contentPageUrl = contentPageUrl;
    }
}
