package com.digitalscale.parser;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Vishal Gadhiya on 4/10/2017.
 */

public class UserParser {

    private static final String TAG = UserParser.class.getSimpleName();

    public static class SignUp {
        String status;
        String message;
        String userId;
        String email;
        String firstName;
        String lastName;
        String profilePic;
        String initialSetting;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public static SignUp parseSignUpResponse(JSONObject jsonObject) {
            SignUp signUp = new SignUp();

            try {
                if (jsonObject != null && jsonObject.length() > 0) {
                    signUp.setStatus(jsonObject.optString("status"));
                    signUp.setMessage(jsonObject.optString("message"));

                    if (jsonObject.has("data")) {
                        JSONObject jo = new JSONObject(jsonObject.optString("data"));

                        signUp.setUserId(jo.optString("userId"));
                        signUp.setEmail(jo.optString("email"));
                        signUp.setFirstName(jo.optString("firstName"));
                        signUp.setLastName(jo.optString("lastName"));
                        signUp.setProfilePic(jo.optString("profilePic"));
                        signUp.setInitialSetting(jo.optString("initialSetting"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return signUp;
        }
    }

    public static class Login {
        String status;
        String message;
        String userId;
        String email;
        String firstName;
        String lastName;
        String profilePic;
        String initialSetting;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public static Login parseLoginResponse(JSONObject jsonObject) {
            Login login = new Login();

            if (jsonObject != null) {
                try {
                    login.setStatus(jsonObject.optString("status"));
                    login.setMessage(jsonObject.optString("message"));

                    String data = jsonObject.optString("data");

                    if (data != null && data.length() > 0) {
                        JSONObject jo = new JSONObject(data);
                        login.setUserId(jo.optString("userId"));
                        login.setFirstName(jo.optString("firstName"));
                        login.setLastName(jo.optString("lastName"));
                        login.setEmail(jo.optString("email"));
                        login.setProfilePic(jo.optString("profilePic"));
                        login.setInitialSetting(jo.optString("initialSetting"));
                    }
                    else {
                        Log.d(TAG,"Login response \"data\" not available");
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Login response parsing exception");
                    e.printStackTrace();
                    return null;
                }
            }


            return login;
        }
    }
}
