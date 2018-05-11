package com.digitalscale.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.activity.AddFoodActivity_;
import com.digitalscale.adapter.SnacksFoodAdapter;
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
public class SnacksFoodHistoryFragment extends MasterFragment {

    public static final String TAG = SnacksFoodHistoryFragment.class.getSimpleName();

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

    public static ArrayList<FoodHistory> snacksList;
    SnacksFoodAdapter testAdapter;

    @AfterViews
    public void init() {
        debugLog("snacks init");
        debugLog(TAG,"Load Snacks food Fragment");
        snacksList = new ArrayList<FoodHistory>();
        //Call API Of The Snacks History
        getSnacksHistory();
        testAdapter = new SnacksFoodAdapter(getContext(), snacksList);
        rv_diary_food_list_snackes.setAdapter(testAdapter);
    }
     /**
     * Call API Snacks History
     */
    private void getSnacksHistory() {
        final HashMap<String, String> param = new HashMap();
        param.put("user_id", getSession().getUserId());
        param.put("type", Constant.SNACKS);
        param.put("food_date", selectedDate/*DateUtility.getCurrentDateIn_yyyy_mm_dd()*/);

        FoodService.getFoodHistory(getContext(), param, pb_process, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                debugLog("Snacks param >>" + param);
                debugLog("response >> " + response);

                //Get snacksList after the response parsing
                ArrayList<FoodHistory> tempList = FoodParser.FoodHistoryParser.parseFoodHistoryResponse(getContext(), response);
                if (tempList != null && tempList.size() > 0)
                    snacksList.addAll(tempList);

                if (snacksList != null && snacksList.size() > 0) {
                    iv_diary_food_list_edit_food.setVisibility(View.VISIBLE);
                    debugLog("snacksList.size() >> " + snacksList.size());
                    goneListView();

                    if (snacksList.size() > 0) {
                        //Collections.reverse(snacksList);
                        testAdapter.updateList(snacksList);
                        rv_diary_food_list_snackes.setVisibility(View.VISIBLE);
                    }
                    tv_diary_food_list_cal_total.setText(FoodHistoryFragment.getCaloriesTotal(snacksList) + " kcal");
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
        intent.putExtra("food_type", Constant.SNACKS);
        startActivity(intent);
    }

    /* Edit food */
    @Click
    public void iv_diary_food_list_edit_food() {
        Intent intent = new Intent(getContext(), AddFoodActivity_.class);
        intent.putExtra("food_type", Constant.SNACKS);
        intent.putExtra("food_mode_edit", true);
        startActivity(intent);
    }

    /**
     * Show Or Hide Listview
     */
    private void goneListView() {
        rv_diary_food_list.setVisibility(View.GONE);
        rv_diary_food_list_dinner.setVisibility(View.GONE);
        rv_diary_food_list_lunch.setVisibility(View.GONE);
        rv_diary_food_list_snackes.setVisibility(View.GONE);
    }
}
