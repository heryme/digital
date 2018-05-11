package com.digitalscale.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.digitalscale.R;
import com.digitalscale.adapter.BreackFastFoodAdapter;
import com.digitalscale.model.FoodHistory;
import com.digitalscale.tools.Constant;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vishal Gadhiya on 3/29/2017.
 */

@EFragment(R.layout.tab_diary_food_list)
public class FoodHistoryFragment extends MasterFragment {

    @ViewById(R.id.rv_diary_food_list)
    RecyclerView rv_diary_food_list;

    private static String foodType = null;
    private static String date = null;

    private ArrayList<FoodHistory> lunchList = new ArrayList<>();
    private ArrayList<FoodHistory> breakFastList = new ArrayList<>();
    private ArrayList<FoodHistory> dinnerList = new ArrayList<>();
    private ArrayList<FoodHistory> snacksList = new ArrayList<>();

    static FoodHistoryFragment foodHistoryFragment = null;

    //static FoodHistoryAdapter foodHistoryAdapter;
    static BreackFastFoodAdapter testAdapter;

    public static FoodHistoryFragment newInstance(String food_type, String date) {
        foodType = food_type;
        Log.d("TAG", "food_type >> " + foodType);
        date = date;
        return new FoodHistoryFragment_();
    }

    @AfterViews
    public void init() {
        debugLog("food init");
        lunchList = new ArrayList<>();
        breakFastList = new ArrayList<>();
        dinnerList = new ArrayList<>();
        snacksList = new ArrayList<>();
    }

    /**
     * Calculate Calories Total
     * @param list
     * @return
     */
    public static String getCaloriesTotal(ArrayList<FoodHistory> list) {
        float calTotal = 0f;
        for (int i = 0; i < list.size(); i++) {
            calTotal = calTotal + Float.valueOf(list.get(i).getKcal());
        }
        //return Math.round(calTotal);
        return new DecimalFormat("##.##").format(calTotal);
    }

    /* parse food history response */
    private HashMap<String, Object> foodHistoryParser(JSONObject response) {
        HashMap<String, Object> map = null;
        ArrayList<FoodHistory> foodHistoryList = null;
        try {
            map = new HashMap<>();

            debugLog("jo1 >> " + response.getString(Constant.LUNCH));

            JSONArray jsonArray = new JSONArray(response.getString(Constant.LUNCH));
            debugLog("array >> " + jsonArray.toString());

            if (jsonArray != null && jsonArray.length() > 0) {
                foodHistoryList = new ArrayList<>();
                FoodHistory foodHistory = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    foodHistory = new FoodHistory();

                    if (jo.has("foodId"))
                        foodHistory.setFoodId(jo.getString("foodId"));

                    if (jo.has("foodHistoryId"))
                        foodHistory.setFoodHistoryId(jo.getString("foodHistoryId"));

                    if (jo.has("foodName")) {
                        debugLog(jo.getString("foodName"));
                        foodHistory.setFoodName(jo.getString("foodName"));
                    }

                    if (jo.has("description"))
                        foodHistory.setDescription(jo.getString("description"));

                    if (jo.has("qty"))
                        foodHistory.setQty(jo.getString("qty"));

                    if (jo.has("qtyUnit"))
                        foodHistory.setQtyUnit(jo.getString("qtyUnit"));

                    if (jo.has("kcal"))
                        foodHistory.setKcal(jo.getString("kcal"));

                    foodHistoryList.add(foodHistory);
                }

            }
            debugLog("foodHistoryList.size() >> " + foodHistoryList.size());
            map.put("HISTORY_LIST", foodHistoryList);

        } catch (Exception e) {
            e.printStackTrace();
            map = null;

        }
        return map;

    }
}
