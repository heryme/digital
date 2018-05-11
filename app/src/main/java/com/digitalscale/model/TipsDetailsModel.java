
package com.digitalscale.model;

import com.google.gson.annotations.SerializedName;

public class TipsDetailsModel {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Data data;


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public Data getData() {
        return data;
    }


    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("tipId")
        private String tipId;
        @SerializedName("title")
        private String title;
        @SerializedName("image")
        private String image;
        @SerializedName("content")
        private String content;

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


        public String getContent() {
            return content;
        }


        public void setContent(String content) {
            this.content = content;
        }

    }


}
