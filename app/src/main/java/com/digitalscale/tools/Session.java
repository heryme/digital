package com.digitalscale.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Vishal Gadhiya on 3/2/2017.
 */

public class Session {

    private static final String USER_ID = "user_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String PROFILE_PIC_URL = "profile_pic_url";
    private static final String USER_ASK_TO_BASIC_QUESTION = "user_ask_to_basic_question";
    private static final String QUESTION2_ACT_UNIT = "unit";

    private static final String QUESTION_ONE_SELECTION_ID = "qone_selection_id";
    private static final String QUESTION_TWO_SELECTION = "qtwo";
    private static final String QUESTION_TWO_LOSE_WEIGHT = "lose_weight";
    private static final String QUESTION_TWO_GAIN_WEIGHT = "gain weight";
    private static final String BASIC_DETAIL_HEIGHT = "basic_height";
    private static final String BASIC_DETAIL_WEIGHT = "basic_weight";
    private static final String BASIC_DETAIL_HEIGHT_UNIT = "basic_height_unit";
    private static final String BASIC_DETAIL_WEIGHT_UNIT = "basic_weight_unit";
    private static final String BASIC_DETAIL_BIRTH_DATE = "birth_date";
    private static final String BASIC_DETAIL_GENDER = "gender";

    private static final String BASIC_DETAIL_HEIGHT_FT = "height_ft";
    private static final String BASIC_DETAIL_HEIGHT_INCH = "height_inch";

    private static final String FORGOT_PASSWORD_EMAIL_ID = "email_id";

    private static final String STEPS = "steps";
    private  static final String CALORIES = "calories";
    private  static final String TOTAL_DISTANCE = "total_dis";

    private static final String EMAIL_VERIFICATION_SCREEN = "email_verification";

    SharedPreferences sharedPreferences;

    public Session(Context context) {
        sharedPreferences = context.getSharedPreferences("sp_digital_scale", Context.MODE_PRIVATE);
    }

    private void saveDataInSharPref(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void saveDataInSharPref(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setUserId(String id) {
        saveDataInSharPref(Session.USER_ID, id);
    }

    public String getUserId() {
        return sharedPreferences.getString(Session.USER_ID, "");
    }

    public void setEmail(String email) {
        saveDataInSharPref(Session.EMAIL, email);
    }

    public String getEmail() {
        return sharedPreferences.getString(Session.EMAIL, "");
    }

    public void setFirstName(String firstName) {
        saveDataInSharPref(Session.FIRST_NAME, firstName);
    }

    public String getFirstName() {
        return sharedPreferences.getString(Session.FIRST_NAME, "");
    }

    public void setLastName(String lastName) {
        saveDataInSharPref(Session.LAST_NAME, lastName);
    }

    public String getLastName() {
        return sharedPreferences.getString(Session.LAST_NAME, "");
    }

    public void setProfilePic(String profilePicUrl) {
        saveDataInSharPref(Session.PROFILE_PIC_URL, profilePicUrl);
    }

    public String getProfilePic() {
        return sharedPreferences.getString(Session.PROFILE_PIC_URL, "");
    }

    /*public boolean getIsAskBasicQuestionToUser() {
        return sharedPreferences.getBoolean(USER_ASK_TO_BASIC_QUESTION, true);
    }

    public void setIsAskBasicQuestionToUser(boolean flag) {
        saveDataInSharPref(USER_ASK_TO_BASIC_QUESTION, flag);
    }*/

    // Clear session
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Set Unit For Question2Activity
     * @param unit
     */
    public void setUnit(String unit) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(QUESTION2_ACT_UNIT);
        editor.apply();
        saveDataInSharPref(Session.QUESTION2_ACT_UNIT, unit);
    }

    public String getUnit() {
        return sharedPreferences.getString(Session.QUESTION2_ACT_UNIT, "kg");
    }

    /* Set Question 1 selection id */
    public void setSelectionQuestion1(String q1) {
        saveDataInSharPref(Session.QUESTION_ONE_SELECTION_ID, q1);
    }

    public String getSelectionQuestion1() {
        return sharedPreferences.getString(Session.QUESTION_ONE_SELECTION_ID, "");
    }

    /**
     * Set Question2 Selected State Of The Option
     * @param q2
     */
    public void setSelectionQuestion2(String q2) {
        saveDataInSharPref(Session.QUESTION_TWO_SELECTION, q2);
    }

    public String getSelectionQuestion2() {
        return sharedPreferences.getString(Session.QUESTION_TWO_SELECTION, "");
    }

    /**
     * Set Question2 Lose Weight
     * @param loseWight
     */

    public void setQuestion2LoseWeight(String loseWight){
        saveDataInSharPref(Session.QUESTION_TWO_LOSE_WEIGHT, loseWight);
    }

    public String getQuestion2LoseWeight() {
        return sharedPreferences.getString(Session.QUESTION_TWO_LOSE_WEIGHT, "");
    }

    /**
     * Set Question2 Gain Weight
     * @param gainWight
     */

    public void setQuestion2gainWeight(String gainWight){
        saveDataInSharPref(Session.QUESTION_TWO_GAIN_WEIGHT, gainWight);
    }

    public String getQuestion2gainWeight() {
        return sharedPreferences.getString(Session.QUESTION_TWO_GAIN_WEIGHT, "");
    }

    /**
     * Set BasicDetail Height
     * @param height
     */
    public void setBasicDetailHeight(String height){
        saveDataInSharPref(Session.BASIC_DETAIL_HEIGHT, height);
    }

    public String getBasicDetailHeight() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_HEIGHT, "");
    }

    /**
     * Set BasicDetail Height
     * @param weight
     */
    public void setBasicDetailWeight(String weight){
        saveDataInSharPref(Session.BASIC_DETAIL_WEIGHT, weight);
    }

    public String getBasicDetailWeight() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_WEIGHT, "");
    }

    /**
     * Set basic detail height unit
     * @param heightUnit
     */

    public void setBasicDetailHeightUnit(String heightUnit){
        saveDataInSharPref(Session.BASIC_DETAIL_HEIGHT_UNIT, heightUnit);
    }

    public String getBasicDetailHeightUnit() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_HEIGHT_UNIT, "cm");
    }


    /**
     * Set basic detail weight unit
     * @param weightUnit
     */

    public void setBasicDetailWeightUnit(String weightUnit){
        saveDataInSharPref(Session.BASIC_DETAIL_WEIGHT_UNIT, weightUnit);
    }

    public String getBasicDetailWeightUnit() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_WEIGHT_UNIT, "kg");
    }

    /**
     * Set basic detail weight unit
     * @param birthDate
     */

    public void setBasicDetailBirthDate(String birthDate){
        saveDataInSharPref(Session.BASIC_DETAIL_BIRTH_DATE, birthDate);
    }

    public String getBasicDetailBirthDate() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_BIRTH_DATE, "<select date>");
    }

    /**
     * Set basic detail Gender
     * @param gender
     */

    public void setBasicDetailGender(String gender){
        saveDataInSharPref(Session.BASIC_DETAIL_GENDER, gender);
    }

    public String getBasicDetailGender() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_GENDER, "");
    }


    /**
     * Set basic detail For Spinner Height
     * @param ft
     */

    public void setBasicDetailSpinnerHeightFt(String ft){
        saveDataInSharPref(Session.BASIC_DETAIL_HEIGHT_FT, ft);
    }

    public String getBasicDetailSpinnerHeightFt() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_HEIGHT_FT, "5");
    }

    /**
     * Set basic detail For Spinner Height
     * @param inch
     */

    public void setBasicDetailSpinnerHeightInch(String inch){
        saveDataInSharPref(Session.BASIC_DETAIL_HEIGHT_INCH, inch);
    }

    public String getBasicDetailSpinnerHeightInch() {
        return sharedPreferences.getString(Session.BASIC_DETAIL_HEIGHT_INCH, "0");
    }


    /**
     * Set Email Id When User Make Request For Forgot Password
     * @param gender
     */

    public void setEmailIDForgotPwd(String gender){
        saveDataInSharPref(Session.FORGOT_PASSWORD_EMAIL_ID, gender);
    }

    public String getEmailIDForgotPwd() {
        return sharedPreferences.getString(Session.FORGOT_PASSWORD_EMAIL_ID, "");
    }

    /**
     * Set Step Come From The Fitness API
     * @param steps
     */
    public void setSteps(String steps){
        saveDataInSharPref(Session.STEPS, steps);
    }

    public String getSteps() {
        return sharedPreferences.getString(Session.STEPS, "0");
    }

    /**
     * Set Calories Come From The Fitness API
     * @param calories
     */
    public void setCalories(String calories){
        saveDataInSharPref(Session.CALORIES, calories);
    }

    public String getCalories() {
        return sharedPreferences.getString(Session.CALORIES, "0");
    }

    /**
     * Set Total Distance Come From The Fitness API
     * @param  totalDistance
     */
    public void setTotalDistance(String totalDistance){
        saveDataInSharPref(Session.TOTAL_DISTANCE, totalDistance);
    }

    public String getTotalDistance() {
        return sharedPreferences.getString(Session.TOTAL_DISTANCE, "0.0");
    }

    public void setIsUserFillBasicDetails(boolean flag) {
        saveDataInSharPref(Session.USER_ASK_TO_BASIC_QUESTION, flag);
    }

    public boolean getIsUserFillBasicDetails() {
        return sharedPreferences.getBoolean(Session.USER_ASK_TO_BASIC_QUESTION, false);
    }

    /**
     * For Email Verification Activity Directly Go YT
     * @param isShow
     */
    public void setIsUserVerifyEmailId(boolean isShow) {
        saveDataInSharPref(Session.EMAIL_VERIFICATION_SCREEN, isShow);
    }

    public boolean getIsUserVerifyEmailId() {
        return sharedPreferences.getBoolean(Session.EMAIL_VERIFICATION_SCREEN, false);
    }
}
