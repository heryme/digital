package com.digitalscale.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.activity.AddFoodActivity_;
import com.digitalscale.adapter.DinnerFoodAdapter;
import com.digitalscale.model.FoodHistory;
import com.digitalscale.parser.FoodParser;
import com.digitalscale.services.FoodService;
import com.digitalscale.tools.Constant;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.digitalscale.activity.SplashActivity.selectedDate;


/**
 * Created by Vishal Gadhiya on 4/7/2017.
 */

@EFragment(R.layout.tab_diary_food_list)
public class DinnerFoodHistoryFragment extends MasterFragment {

    public static final String TAG = DinnerFoodHistoryFragment.class.getSimpleName();

    @ViewById(R.id.rv_diary_food_list)
    ListView rv_diary_food_list;

    @ViewById(R.id.rv_diary_food_list_lunch)
    ListView rv_diary_food_list_lunch;

    @ViewById(R.id.rv_diary_food_list_dinner)
    ListView rv_diary_food_list_dinner;

    @ViewById(R.id.rv_diary_food_list_snackes)
    ListView rv_diary_food_list_snackes;

    @ViewById(R.id.iv_diary_food_list_add_food)
    ImageView iv_diary_food_list_add_food;

    @ViewById(R.id.iv_diary_food_list_edit_food)
    ImageView iv_diary_food_list_edit_food;

    @ViewById(R.id.tv_diary_food_list_cal_total)
    TextView tv_diary_food_list_cal_total;

    @ViewById(R.id.pb_process)
    ProgressBar pb_process;

    /**
     * TestAdapterDinner
     */
    DinnerFoodAdapter testAdapter;

    /**
     * Dinner ArrayList
     */
    public static ArrayList<FoodHistory> dinnerList;

    @AfterViews
    public void init() {
        debugLog(TAG,"Load Dinner Fragment");
        debugLog("Dinner init");
        dinnerList = new ArrayList<FoodHistory>();
        //Call API
        getDinnerHistory();
        testAdapter = new DinnerFoodAdapter(getActivity(), dinnerList);
        rv_diary_food_list_dinner.setAdapter(testAdapter);
    }

    /**
     * Call API OF The Dinner History
     */
    private void getDinnerHistory() {
        final HashMap<String, String> param = new HashMap();
        param.put("user_id", getSession().getUserId());
        param.put("type", Constant.DINNER);
        param.put("food_date", selectedDate/*DateUtility.getCurrentDateIn_yyyy_mm_dd()*/);

        FoodService.getFoodHistory(getContext(), param,pb_process,new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog("Dinner param >>" + param);
                debugLog("response >> " + response);
                //dinnerList.clear();
                ArrayList<FoodHistory> list = FoodParser.FoodHistoryParser.parseFoodHistoryResponse(getContext(), response);
                if (list != null && list.size() > 0)
                    dinnerList.addAll(list);

                if (dinnerList != null && dinnerList.size() > 0) {
                    iv_diary_food_list_edit_food.setVisibility(View.VISIBLE);
                    debugLog("dinnerList.size() >> " + dinnerList.size());
                    goneListView();
                   // FoodHistoryFragment.setupList(getContext(), dinnerList, rv_diary_food_list_breakfast);
                    if (dinnerList.size() > 0) {
                        //Collections.reverse(dinnerList);
                        testAdapter.updateList(dinnerList);
                        rv_diary_food_list_dinner.setVisibility(View.VISIBLE);
                    }

                    tv_diary_food_list_cal_total.setText(FoodHistoryFragment.getCaloriesTotal(dinnerList)+" kcal");
                } else {
                    iv_diary_food_list_edit_food.setVisibility(View.GONE);
                }

            }
        });
    }

    /* Add food */
    @Click
    public void iv_diary_food_list_add_food() {
        Intent intent = new Intent(getContext(), AddFoodActivity_.class);
        intent.putExtra("food_type", Constant.DINNER);
        startActivity(intent);
    }

    /* Edit food */
    @Click
    public void iv_diary_food_list_edit_food() {
        Intent intent = new Intent(getContext(), AddFoodActivity_.class);
        intent.putExtra("food_type", Constant.DINNER);
        intent.putExtra("food_mode_edit", true);
        startActivity(intent);
    }

    /**
     * Show Hide ListView
     */
    private void goneListView() {
        rv_diary_food_list.setVisibility(View.GONE);
        rv_diary_food_list_dinner.setVisibility(View.GONE);
        rv_diary_food_list_lunch.setVisibility(View.GONE);
        rv_diary_food_list_snackes.setVisibility(View.GONE);
    }
}
