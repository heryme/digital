package com.digitalscale.fragments;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.activity.BluetoothDevicesActivity_;
import com.digitalscale.services.GoalInfoService;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.BluetoothUtility;
import com.digitalscale.utility.DateUtility;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import static com.digitalscale.activity.SplashActivity.selectedDate;

/**
 * Created by Vishal Gadhiya on 3/28/2017.
 */

@EFragment(R.layout.fragment_diary)
public class DiaryFragment extends MasterFragment {

    public static final String TAG = DiaryFragment.class.getSimpleName();

    @ViewById(R.id.tab_diary)
    TabLayout tabLayoutDiary;

    @ViewById(R.id.viewpager_diary)
    ViewPager viewPagerDiary;

    @ViewById(R.id.tv_diary_frg_goal)
    public static TextView tv_diary_frg_goal;

    @ViewById(R.id.tv_diary_frg_food)
    public static TextView tv_diary_frg_food;

    @ViewById(R.id.tv_tv_diary_frg_remaining)
    public static TextView tv_tv_diary_frg_remaining;

    @ViewById(R.id.tv_frg_diary_goal)
    TextView tv_frg_diary_goal;

    @ViewById(R.id.tv_frg_diary_food)
    TextView tv_frg_diary_food;

    @ViewById(R.id.tv_frg_diary_remaining)
    TextView tv_frg_diary_remaining;

    @ViewById(R.id.iv_diary_previous_date)
    ImageView iv_diary_previous_date;

    @ViewById(R.id.iv_diary_next_date)
    ImageView iv_diary_next_date;

    @ViewById(R.id.tv_diary_selectedDate)
    TextView tv_diary_selectedDate;


    static DiaryFragment diaryFragment = null;

    DiaryTabsAdapter diaryTabsAdapter;

    public static int selectedTabPosition = 0; // Breakfast

    @AfterViews
    public void init() {

        session = new Session(getActivity());

        setHasOptionsMenu(true);

        setRetainInstance(true);

        showHideNextPrevious();

        setUpTabs();

        clearList();

        // Set selected date of food data
        if (selectedDate.equals(DateUtility.getCurrentDateIn_yyyy_mm_dd()))
            tv_diary_selectedDate.setText(getString(R.string.lbl_today));
        else
            tv_diary_selectedDate.setText(selectedDate);

        setFontStyle();

        //Call Goal Info Service
        callGoalInfoService();
        //setRetainInstance(true);
    }

    /**
     * Clear List First Time In Initialization
     */
    private void clearList() {
        if (BreakfastFoodHistoryFragment.breakfastList != null)
            BreakfastFoodHistoryFragment.breakfastList.clear();

        if (LunchFoodHistoryFragment.lunchList != null)
            LunchFoodHistoryFragment.lunchList.clear();

        if (DinnerFoodHistoryFragment.dinnerList != null)
            DinnerFoodHistoryFragment.dinnerList.clear();

        if (SnacksFoodHistoryFragment.snacksList != null)
            SnacksFoodHistoryFragment.snacksList.clear();
    }

    /**
     * Set Up Tabs
     */
    private void setUpTabs() {

        tabLayoutDiary.addTab(tabLayoutDiary.newTab().setText(getString(R.string.lbl_breakfast)));
        tabLayoutDiary.addTab(tabLayoutDiary.newTab().setText(getString(R.string.lbl_lunch)));
        tabLayoutDiary.addTab(tabLayoutDiary.newTab().setText(getString(R.string.lbl_Snacks)));
        tabLayoutDiary.addTab(tabLayoutDiary.newTab().setText(getString(R.string.lbl_dinner)));
        tabLayoutDiary.setTabGravity(TabLayout.GRAVITY_FILL);

        //Set Font Style
        FontUtility.condLight(tv_frg_diary_goal,getContext());
        FontUtility.condLight(tv_frg_diary_food,getContext());
        FontUtility.condLight(tv_frg_diary_remaining,getContext());

        //tabLayoutDiary.setupWithViewPager(viewPagerDiary);
        diaryTabsAdapter = new DiaryTabsAdapter(getChildFragmentManager(), 4);
        viewPagerDiary.setAdapter(diaryTabsAdapter);
        viewPagerDiary.setCurrentItem(DiaryFragment.selectedTabPosition);
        viewPagerDiary.setOffscreenPageLimit(4);

        viewPagerDiary.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutDiary));
        tabLayoutDiary.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                debugLog("Tab select >> " + tab.getText());
                viewPagerDiary.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static DiaryFragment newInstance() {
        if (diaryFragment == null)
            return new DiaryFragment();
        else return diaryFragment;
    }

    /**
     * Diary view pager adapter class
     */
    class DiaryTabsAdapter extends FragmentStatePagerAdapter {
        int tabCount;

        public DiaryTabsAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BreakfastFoodHistoryFragment_();
                case 1:
                    return new LunchFoodHistoryFragment_();
                case 2:
                    return new SnacksFoodHistoryFragment_();
                case 3:
                    return new DinnerFoodHistoryFragment_();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    /**
     * Call Goal Info Service
     */
    private void callGoalInfoService() {
        GoalInfoService.getGoalInfo(getActivity(), session.getUserId(), DateUtility.getCurrentDateIn_yyyy_mm_dd(), new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG,"Get Goal Info Response---> " + response.toString());
                parseResponseGoalInfo(response);
            }
        });
    }

    /**
     * Response Parsing Goal Info
     * @param jsonObject
     */
    public static void parseResponseGoalInfo(JSONObject jsonObject) {
        try {
            if(jsonObject != null && jsonObject.length() > 0) {

                String status = jsonObject.getString("status");
                //debugLog(TAG,"status-->" + status);
                String message = jsonObject.getString("message");
                //debugLog(TAG,"message-->" + message);

                if(jsonObject.has("data")){
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    tv_diary_frg_goal.setText(jsonObject1.getString("required_kcal") + "\nCAL");
                    tv_diary_frg_food.setText(jsonObject1.getString("consumed_kcal") + "\nCAL");

                    float startingCalories = Float.parseFloat(jsonObject1.getString("consumed_kcal"));
                    float burningCalories =  Float.parseFloat(jsonObject1.getString("required_kcal"));

                    tv_tv_diary_frg_remaining.setText(String.valueOf(Math.round(burningCalories - startingCalories)) + "\nCAL");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     *  //Set Font Style TextView
     */
    private void setFontStyle(){
        FontUtility.condBold(tv_diary_frg_goal,getContext());
        FontUtility.condBold(tv_diary_frg_food,getContext());
        FontUtility.condBold(tv_tv_diary_frg_remaining,getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_diary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bluetooth_diary:
                if (BluetoothUtility.checkBluetoothCompatibility())
                    startActivity(new Intent(getContext(), BluetoothDevicesActivity_.class));
                else
                    toastMessage("Sorry !!! Your phone does not support bluetooth");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Next Date : get next date food data */
    @Click
    public void iv_diary_next_date() {
        // For next date : 1
        selectedDate = DateUtility.getNextOrPreviousDateOf(selectedDate, 1);

        if (diaryTabsAdapter != null)
            diaryTabsAdapter = null;

        diaryTabsAdapter = new DiaryTabsAdapter(getChildFragmentManager(), 4);
        viewPagerDiary.setAdapter(diaryTabsAdapter);
        viewPagerDiary.setCurrentItem(DiaryFragment.selectedTabPosition);
        viewPagerDiary.setOffscreenPageLimit(4);

        tv_diary_selectedDate.setText(selectedDate);

        showHideNextPrevious();
    }

    /* Previous Date : get next date food data */
    @Click
    public void iv_diary_previous_date() {
        // For previous date : 0
        selectedDate = DateUtility.getNextOrPreviousDateOf(selectedDate, 0);

        showHideNextPrevious();

        if (diaryTabsAdapter != null)
            diaryTabsAdapter = null;

        if (!DateUtility.isDate15DaysPrevious(selectedDate)) {
            diaryTabsAdapter = new DiaryTabsAdapter(getChildFragmentManager(), 4);
            viewPagerDiary.setAdapter(diaryTabsAdapter);
            viewPagerDiary.setCurrentItem(DiaryFragment.selectedTabPosition);
            viewPagerDiary.setOffscreenPageLimit(4);
            tv_diary_selectedDate.setText(selectedDate);
            iv_diary_next_date.setVisibility(View.VISIBLE);

            callGoalInfoService();
        } else {
            debugLog(TAG, "selectedDate before >> " + selectedDate);
            selectedDate = DateUtility.getNextOrPreviousDateOf(selectedDate, 1);
            debugLog(TAG, "selectedDate after >> " + selectedDate);
            String msg = getString(R.string.msg_last_15days_food_show);/*"Sorry !!! You can see only last 15 day's food history data from today.";*/
            DialogUtility.dialogWithPositiveButton(msg, getContext());
            iv_diary_previous_date.setVisibility(View.INVISIBLE);
            iv_diary_next_date.setVisibility(View.VISIBLE);
        }
    }

    private void showHideNextPrevious() {
        try {
            if (selectedDate.equals(DateUtility.getCurrentDateIn_yyyy_mm_dd())) {
                debugLog("Today");
                tv_diary_selectedDate.setText(getString(R.string.lbl_today));
                iv_diary_next_date.setVisibility(View.INVISIBLE);
            } else {
                iv_diary_previous_date.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
