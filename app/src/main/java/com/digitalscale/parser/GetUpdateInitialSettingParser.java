package com.digitalscale.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahul Padaliya on 6/9/2017.
 */
public class GetUpdateInitialSettingParser {

    public static final String TAG = GetUpdateInitialSettingParser.class.getSimpleName();

    String status;
    String message;
    String questionOne;
    String questionTwo;
    String questionTwoWeight;
    String questionTwoUnit;

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

    public String getQuestionOne() {
        return questionOne;
    }

    public void setQuestionOne(String questionOne) {
        this.questionOne = questionOne;
    }

    public String getQuestionTwo() {
        return questionTwo;
    }

    public void setQuestionTwo(String questionTwo) {
        this.questionTwo = questionTwo;
    }

    public String getQuestionTwoWeight() {
        return questionTwoWeight;
    }

    public void setQuestionTwoWeight(String questionTwoWeight) {
        this.questionTwoWeight = questionTwoWeight;
    }

    public String getQuestionTwoUnit() {
        return questionTwoUnit;
    }

    public void setQuestionTwoUnit(String questionTwoUnit) {
        this.questionTwoUnit = questionTwoUnit;
    }

    public static GetUpdateInitialSettingParser parseGetUpdateInitialSetting(JSONObject jsonObject){

        if(jsonObject != null && jsonObject.length() > 0) {
            GetUpdateInitialSettingParser getUpdateInitialSettingParser = new GetUpdateInitialSettingParser();
            try {
                getUpdateInitialSettingParser.setStatus(jsonObject.getString("status"));
                getUpdateInitialSettingParser.setMessage(jsonObject.getString("message"));

                String data = jsonObject.getString("data");
                JSONObject question_one = new JSONObject(data);
                JSONObject question_two = new JSONObject(data);
                JSONObject question_two_weight = new JSONObject(data);
                JSONObject question_two_unit = new JSONObject(data);

                getUpdateInitialSettingParser.setQuestionOne(question_one.getString("question_one"));
                getUpdateInitialSettingParser.setQuestionTwo(question_two.getString("question_two"));
                getUpdateInitialSettingParser.setQuestionTwoWeight(question_two_weight.getString("question_two_weight"));
                getUpdateInitialSettingParser.setQuestionTwoUnit(question_two_unit.getString("question_two_unit"));

                return getUpdateInitialSettingParser;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
