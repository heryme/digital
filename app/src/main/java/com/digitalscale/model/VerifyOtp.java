package com.digitalscale.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vishal Gadhiya on 3/11/2017.
 */

public class VerifyOtp {

    @SerializedName("status")
    String status;

    @SerializedName("message")
    String message;

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
}
