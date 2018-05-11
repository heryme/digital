package com.digitalscale.parser;

import android.util.Log;

import com.digitalscale.model.TipsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vishal Gadhiya on 4/19/2017.
 */

public class TipsParser {

    public static String TAG = TipsParser.class.getSimpleName();

    public static class Tips {
        String status;
        String message;
        ArrayList<TipsModel> tipList;

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

        public ArrayList<TipsModel> getTipList() {
            if (tipList == null)
                tipList = new ArrayList<>();
            return tipList;
        }

        public void setTipList(ArrayList<TipsModel> tipList) {
            this.tipList = tipList;
        }

        public static Tips parseGetTipsResponse(JSONObject jsonObject) {
            Tips tips = new Tips();

            if (jsonObject != null) {
                try {
                    tips.setStatus(jsonObject.optString("status"));
                    tips.setMessage(jsonObject.optString("message"));

                    String data = jsonObject.optString("data");
                    Log.d(TAG,"Data >> "+ data);
                    JSONArray jsonArray= new JSONArray(data);

                    if (jsonArray != null && jsonArray.length() > 0) {
                        TipsModel tipsM;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            tipsM = new TipsModel();
                            tipsM.setTipId(jo.optString("tipId"));
                            tipsM.setTitle(jo.optString("title"));
                            tipsM.setImage(jo.optString("image"));
                            tipsM.setDescription(jo.optString("description"));
                            tipsM.setContentPageUrl(jo.getString("contentPageUrl"));

                            tips.getTipList().add(tipsM);
                        }

                    } else {
                        Log.d(TAG, "GetTips api response \"data\" not available");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            return tips;
        }
    }

}
