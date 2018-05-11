
package com.digitalscale.model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

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

        @SerializedName("userId")
        private String userId;
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("email")
        private String email;
        @SerializedName("profilePic")
        private String profilePic;
        @SerializedName("initialSetting")
        private String initialSetting;


        public String getUserId() {
            return userId;
        }


        public void setUserId(String userId) {
            this.userId = userId;
        }


        public String getFirstName() {
            return firstName;
        }


        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }


        public String getLastName() {
            return lastName;
        }


        public void setLastName(String lastName) {
            this.lastName = lastName;
        }


        public String getEmail() {
            return email;
        }


        public void setEmail(String email) {
            this.email = email;
        }


        public String getProfilePic() {
            return profilePic;
        }


        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }


        public String getInitialSetting() {
            return initialSetting;
        }


        public void setInitialSetting(String initialSetting) {
            this.initialSetting = initialSetting;
        }

    }


}
