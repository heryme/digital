package com.digitalscale.parser;

import android.nfc.Tag;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahul Padaliya on 5/13/2017.
 */
public class GetProfileParser {
    String firstName;
    String lastName;
    String profilePic;
    String gender;
    String dob;
    String height;
    String height_unit;
    String weight;
    String weight_unit;
    String city;
    String state;
    String country;
    String location;
    String zipcode;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getHeight_unit() {
        return height_unit;
    }

    public void setHeight_unit(String height_unit) {
        this.height_unit = height_unit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    public static GetProfileParser parseGetProfileData(JSONObject jsonObject) throws JSONException {
        if(jsonObject != null && jsonObject.length()> 0) {
            if(jsonObject.has("data")){
                GetProfileParser getProfileParser = new GetProfileParser();
                String data = jsonObject.getString("data");
                JSONObject firstName = new JSONObject(data);
                JSONObject lastName = new JSONObject(data);
                JSONObject profilePic = new JSONObject(data);
                JSONObject gender = new JSONObject(data);
                JSONObject dob = new JSONObject(data);
                JSONObject height = new JSONObject(data);
                JSONObject height_unit = new JSONObject(data);
                JSONObject weight = new JSONObject(data);
                JSONObject weight_unit = new JSONObject(data);
                JSONObject city = new JSONObject(data);
                JSONObject state = new JSONObject(data);
                JSONObject country = new JSONObject(data);
                JSONObject location = new JSONObject(data);
                JSONObject zipcode = new JSONObject(data);

                getProfileParser.setFirstName(firstName.getString("firstName"));
                Log.d("TAG","First Name-->" + firstName.getString("firstName"));
                Log.d("TAG","Last Name-->" + lastName.getString("lastName"));
                Log.d("TAG","Profile Pic--> " + profilePic.getString("profilePic"));
                Log.d("TAG","zipcode Pic--> " + zipcode.getString("zipcode"));

                getProfileParser.setFirstName(firstName.getString("firstName"));
                getProfileParser.setLastName(lastName.getString("lastName"));
                getProfileParser.setProfilePic(profilePic.getString("profilePic"));
                getProfileParser.setGender(gender.getString("gender"));
                getProfileParser.setDob(dob.getString("dob"));
                getProfileParser.setHeight(height.getString("height"));
                getProfileParser.setHeight_unit(height_unit.getString("height_unit"));
                getProfileParser.setWeight(weight.getString("weight"));
                getProfileParser.setWeight_unit(weight_unit.getString("weight_unit"));
                getProfileParser.setCity(city.getString("city"));
                getProfileParser.setState(state.getString("state"));
                getProfileParser.setCountry(country.getString("country"));
                getProfileParser.setLocation(location.getString("location"));
                getProfileParser.setZipcode(zipcode.getString("zipcode"));

                return  getProfileParser;
            }
        }
        return null;
    }

}
