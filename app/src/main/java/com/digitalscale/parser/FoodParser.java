package com.digitalscale.parser;

import android.content.Context;
import android.util.Log;

import com.digitalscale.model.Food;
import com.digitalscale.model.FoodHistory;
import com.digitalscale.tools.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.digitalscale.fragments.DiaryFragment.tv_diary_frg_food;
import static com.digitalscale.fragments.DiaryFragment.tv_diary_frg_goal;
import static com.digitalscale.fragments.DiaryFragment.tv_tv_diary_frg_remaining;

/**
 * Created by Vishal Gadhiya on 4/4/2017.
 */

public class FoodParser {

    /* Get food list from parsing search food api response */
    public static ArrayList<Food> getFoodList(JSONArray jsonArray) {
        ArrayList<Food> foodList = new ArrayList<>();
        try {
            Food food;
            //JSONArray jsonArray = response.getJSONArray("message");
            for (int i = 0; i < jsonArray.length(); i++) {
                food = new Food();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                food.setId(jsonObject.optString("foodId"));
                food.setFoodName(jsonObject.optString("foodName"));
                food.setDescription(jsonObject.optString("description"));
                food.setQuantity(jsonObject.optString("quantity"));
                food.setCalorie(jsonObject.optString("calorie"));
                food.setFat(jsonObject.optString("fat"));
                food.setAlcohol(jsonObject.optString("alcohol"));
                food.setFiber(jsonObject.optString("fiber"));
                food.setAsh(jsonObject.optString("ash"));
                food.setWater(jsonObject.optString("water"));
                food.setProtein(jsonObject.optString("protein"));
                food.setCarbohydrate(jsonObject.optString("carbohydrate"));
                food.setFoodKcal(jsonObject.optString("foodKcal"));
                food.setFoodQty(jsonObject.optString("foodQty"));
                food.setFoodUnit(jsonObject.optString("foodUnit"));

                foodList.add(food);
            }

        } catch (Exception e) {
            e.printStackTrace();
            foodList = null;
        }
        return foodList;
    }

    /* Get food history list form parsing food history api response  */
    public static class FoodHistoryParser {

        public static String status;
        public static String message;
        public static ArrayList<FoodHistory> foodHistoryList = null;

        public static String getStatus() {
            return status;
        }

        public static void setStatus(String status) {
            FoodHistoryParser.status = status;
        }

        public static String getMessage() {
            return message;
        }

        public static void setMessage(String message) {
            FoodHistoryParser.message = message;
        }

        public static void setFoodHistoryList(ArrayList<FoodHistory> foodHistoryList) {
            FoodHistoryParser.foodHistoryList = foodHistoryList;
        }

        public static ArrayList<FoodHistory> getFoodHistoryList() {
            return foodHistoryList;
        }

        /* Parse food history response and return list */
        public static ArrayList<FoodHistory> parseFoodHistoryResponse(Context context, JSONObject response) {
            FoodHistoryParser foodHistoryParser = new FoodHistoryParser();

            if (response != null && response.length() > 0) {
                JSONObject joData;
                try {
                    setStatus(response.optString("status"));
                    setMessage(response.optString("message"));

                    if (getStatus().equalsIgnoreCase("1")) {
                        JSONArray jsonArray = null;
                        foodHistoryList = new ArrayList<>();
                        joData = response.getJSONObject("data");
                        if (joData.has(Constant.LUNCH)) {
                            jsonArray = new JSONArray(joData.getString(Constant.LUNCH));
                        } else if (joData.has(Constant.BREAKFAST)) {
                            jsonArray = new JSONArray(joData.getString(Constant.BREAKFAST));
                        } else if (joData.has(Constant.SNACKS)) {
                            jsonArray = new JSONArray(joData.getString(Constant.SNACKS));
                        } else if (joData.has(Constant.DINNER)) {
                            jsonArray = new JSONArray(joData.getString(Constant.DINNER));
                        }

                        if (jsonArray != null && jsonArray.length() > 0) {

                            FoodHistory foodHistory = null;

                            Log.d("TAG", "jsonArray.length() >> " + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                foodHistory = new FoodHistory();

                                if (jo.has("foodId"))
                                    foodHistory.setFoodId(jo.getString("foodId"));

                                if (jo.has("foodHistoryId"))
                                    foodHistory.setFoodHistoryId(jo.getString("foodHistoryId"));

                                if (jo.has("foodName")) {
                                    foodHistory.setFoodName(jo.getString("foodName"));
                                }

                                if (jo.has("description"))
                                    foodHistory.setDescription(jo.getString("description"));

                                if (jo.has("qty"))
                                    foodHistory.setQty(jo.getString("qty"));

                                if (jo.has("qtyUnit")) {
                                    foodHistory.setQtyUnit(jo.getString("qtyUnit"));
                                }

                                if (jo.has("kcal"))
                                    foodHistory.setKcal(jo.getString("kcal"));

                                if(jo.has("foodKcal"))
                                    foodHistory.setFoodKcal(jo.getString("foodKcal"));

                                if(jo.has("foodQty"))
                                    foodHistory.setFoodQty(jo.getString("foodQty"));

                                if(jo.has("foodUnit"))
                                    foodHistory.setFoodUnit(jo.getString("foodUnit"));

                                Log.d("Food Parser", "foodHistory.getFoodName() >> " + foodHistory.getFoodName());
                                foodHistoryList.add(foodHistory);
                            }
                            //foodHistoryParser.setFoodHistoryList(foodHistoryList);
                            if (joData.has("calories")) {
                                JSONObject jsonObject1 = joData.getJSONObject("calories");
                                tv_diary_frg_goal.setText(jsonObject1.getString("required_kcal") + "\nCAL");
                                tv_diary_frg_food.setText(jsonObject1.getString("consumed_kcal") + "\nCAL");

                                float startingCalories = Float.parseFloat(jsonObject1.getString("consumed_kcal"));
                                float burningCalories = Float.parseFloat(jsonObject1.getString("required_kcal"));

                                tv_tv_diary_frg_remaining.setText(String.valueOf (Math.round(burningCalories - startingCalories)) + "\nCAL");
                            }

                        } else {
                            foodHistoryList.clear();
                        }

                    } else {
                        if (foodHistoryList != null)
                            foodHistoryList.clear();

                        // When food history data not available at that time calorie is there
                        joData = response.getJSONObject("data");
                        if (joData.has("calories")) {
                            JSONObject jsonObject1 = joData.getJSONObject("calories");
                            tv_diary_frg_goal.setText(jsonObject1.getString("required_kcal") + "\nCAL");
                            tv_diary_frg_food.setText(jsonObject1.getString("consumed_kcal") + "\nCAL");

                            float startingCalories = Float.parseFloat(jsonObject1.getString("consumed_kcal"));
                            float burningCalories = Float.parseFloat(jsonObject1.getString("required_kcal"));

                            tv_tv_diary_frg_remaining.setText(String.valueOf (Math.round(burningCalories - startingCalories)) + "\nCAL");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    foodHistoryList = null;
                }
            }
            return foodHistoryList;
        }
    }

}
