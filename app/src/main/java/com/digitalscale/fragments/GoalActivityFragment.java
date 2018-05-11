package com.digitalscale.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalscale.R;
import com.digitalscale.services.GoalInfoService;
import com.digitalscale.utility.DateUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.FormulaUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.vollyrest.APIService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

/**
 * Created by Vishal Gadhiya on 2/22/2017.
 */

@EFragment(R.layout.tab_change_fragment_goal_activity)
public class GoalActivityFragment extends MasterFragment {

    @ViewById(R.id.circularProgressBar)
    ProgressBar circularProgressBar;
    @ViewById(R.id.tvProgress)
    TextView tvProgress;

    @ViewById(R.id.tv_goal_activity_cal)
    TextView tv_goal_activity_cal;

    @ViewById(R.id.tv_goal_activity_cal_unit)
    TextView tv_goal_activity_cal_unit;

    @ViewById(R.id.tv_goal_activity_distance1)
    TextView tv_goal_activity_distance;

    @ViewById(R.id.tv_goal_activity_distance_unit)
    TextView tv_goal_activity_distance_unit;

    @ViewById(R.id.tv_goal_activity_no_of_steps1)
    TextView tv_goal_activity_no_of_steps;

    @ViewById(R.id.tv_goal_frg_act_starting_cal1)
    TextView tv_goal_frg_act_starting_cal1;

    @ViewById(R.id.tv_goal_frg_act_starting_cal2)
    TextView tv_goal_frg_act_starting_cal2;

    @ViewById(R.id.tv_goal_frg_act_starting_cal3)
    TextView tv_goal_frg_act_starting_cal3;

    @ViewById(R.id.tv_goal_activity_distance_km)
    TextView tv_goal_activity_distance_km;

    @ViewById(R.id.iv_goal_activity_refresh_steps)
    ImageView iv_goal_activity_refresh_steps;

    @ViewById(R.id.pb_goal_activity_refresh_steps)
    ProgressBar pb_goal_activity_refresh_steps;

    @ViewById(R.id.tv_goal_frg_act_starting_cal_label)
    TextView tv_goal_frg_act_starting_cal_label;

    @ViewById(R.id.tv_goal_frg_act_burning_cal_label)
    TextView tv_goal_frg_act_burning_cal_label;

    @ViewById(R.id.tv_goal_frg_act_current_cal_label)
    TextView tv_goal_frg_act_current_cal_label;

    @ViewById(R.id.tv_goal_frg_act_activity)
    TextView tv_goal_frg_act_activity;

    @ViewById(R.id.ll_tab_change_frg_goal_label)
    LinearLayout ll_tab_change_frg_goal_label;

    @ViewById(R.id.ll_tab_change_frg_food_label)
    LinearLayout ll_tab_change_frg_food_label;

    @ViewById(R.id.ll_tab_change_frg_remaining_label)
    LinearLayout ll_tab_change_frg_remaining_label;

    //New View Like Dairy

    @ViewById(R.id.tv_frg_goal_goal_label)
    TextView tv_frg_goal_goal_label;

    @ViewById(R.id.tv_frg_goal_food_label)
    TextView tv_frg_goal_food_label;

    @ViewById(R.id.tv_frg_goal_remaining_label)
    TextView tv_frg_goal_remaining_label;

    @ViewById(R.id.tv_goal_frg_goal)
    TextView tv_goal_frg_goal;

    @ViewById(R.id.tv_goal_frg_food)
    TextView tv_goal_frg_food;

    @ViewById(R.id.tv_goal_frg_remaining)
    TextView tv_goal_frg_remaining;


    public static final String TAG = GoalActivityFragment.class.getSimpleName();

    public static final int GOOGLE_FIT_API_UPDATE_DATA_INTERVAL = 30; // seconds
    private GoogleApiClient mClient = null;

    private long totalNoOfSteps = 0;
    private float totalTimeDuration = 0f;
    private float totalDistance = 0;
    private float totalCal = 0f;

    Dialog dlgAddFood = null;
    public static Fragment newInstance() {
        return new GoalActivityFragment_();
    }

    @AfterViews
    public void init() {
        buildFitnessClient();

        // Read google fit data first time
        readData();

        if (NetworkUtility.checkIsInternetConnectionAvailable(getActivity())){
            callGoalInfoService();
        }

        setFontStyle();
    }

    @Click
    public void ll_tab_change_frg_goal_label() {
        new SimpleTooltip.Builder(getContext())
                .anchorView(ll_tab_change_frg_goal_label)
                .text("Texto do Tooltipjiosjoa")
                .gravity(Gravity.BOTTOM)
                .animated(true)
                .transparentOverlay(true)
                .backgroundColor(getResources().getColor(R.color.colorOrange))
                .arrowColor(getResources().getColor(R.color.colorOrange))
                .textColor(getResources().getColor(R.color.white))
                .build()
                .show();
    }

    @Click
    public void ll_tab_change_frg_food_label(){
        new SimpleTooltip.Builder(getContext())
                .anchorView(ll_tab_change_frg_food_label)
                .text("Texto do Tooltipjiosjoa")
                .gravity(Gravity.BOTTOM)
                .animated(true)
                .transparentOverlay(true)
                .backgroundColor(getResources().getColor(R.color.colorPink))
                .arrowColor(getResources().getColor(R.color.colorPink))
                .textColor(getResources().getColor(R.color.white))
                .build()
                .show();
    }

    @Click
    public void ll_tab_change_frg_remaining_label(){
        new SimpleTooltip.Builder(getContext())
                .anchorView(ll_tab_change_frg_remaining_label)
                .text("Texto do Tooltipjiosjoa")
                .gravity(Gravity.BOTTOM)
                .animated(true)
                .transparentOverlay(true)
                .backgroundColor(getResources().getColor(R.color.colorOrange))
                .arrowColor(getResources().getColor(R.color.colorOrange))
                .textColor(getResources().getColor(R.color.white))
                .build()
                .show();
    }


    /**
     * Set Font Style of The Textview
     */
    private void setFontStyle(){
        FontUtility.condLight(tv_goal_frg_act_starting_cal_label,getActivity());
        FontUtility.condLight(tv_goal_frg_act_burning_cal_label,getActivity());
        FontUtility.condLight(tv_goal_frg_act_current_cal_label,getActivity());
        FontUtility.condLight(tv_goal_frg_act_activity,getActivity());
        FontUtility.condBold(tv_goal_activity_cal_unit,getActivity());
        FontUtility.condBold(tv_goal_activity_distance_unit,getActivity());
        FontUtility.condBold(tv_goal_activity_distance_km,getActivity());
        FontUtility.condBold(tv_goal_activity_no_of_steps,getActivity());
        FontUtility.condBold(tv_goal_activity_cal,getActivity());
        FontUtility.condBold(tv_goal_activity_distance,getActivity());
        FontUtility.condBold(tv_goal_frg_act_starting_cal1,getContext());
        FontUtility.condBold(tv_goal_frg_act_starting_cal2,getContext());
        FontUtility.condBold(tv_goal_frg_act_starting_cal3,getContext());

        FontUtility.condBold(tv_goal_frg_goal,getActivity());
        FontUtility.condBold(tv_goal_frg_food,getActivity());
        FontUtility.condBold(tv_goal_frg_remaining,getActivity());

        FontUtility.condLight(tv_frg_goal_goal_label,getActivity());
        FontUtility.condLight(tv_frg_goal_food_label,getActivity());
        FontUtility.condLight(tv_frg_goal_remaining_label,getActivity());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        debugLog("Goal frm : " + requestCode);
        if (requestCode != -1 && (requestCode & 0xffff0000) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
        debugLog("Goal act : in Activity result");
    }

    /**
     * Build a {@link GoogleApiClient} to authenticate the user and allow the application
     * to connect to the Fitness APIs. The included scopes should match the scopes needed
     * by your app (see the documentation for details).
     * Use the {@link GoogleApiClient.OnConnectionFailedListener}
     * to resolve authentication failures (for example, the user has not signed in
     * before, or has multiple accounts and must specify which account to use).
     */
    private void buildFitnessClient() {
        // Create the Google API Client

        if (mClient == null) {
            try {
            mClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Fitness.RECORDING_API)
                    .addApi(Fitness.HISTORY_API)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(@Nullable Bundle bundle) {
                                    Log.i(TAG, "Connected!!!");
                                    // Now you can make calls to the Fitness APIs.  What to do?
                                    // Subscribe to some data sources!

                                    subscribe();

                                    //subscribeDis();

                                    //subscribeSpeed();
                                    //dis();
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the sensor gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.w(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.w(TAG, "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(getActivity(), 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.w(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                            String msg = getString(R.string.toast_select_google_acc);/*"You must select at least on google account, Otherwise STEPS,DISTANCE & CALORIES not calculate";*/
                            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    })
                    .build();
            } catch (Exception e) {
                e.printStackTrace();
                mClient = null;
            }

        }

    }

    /**
     * Record step data by requesting a subscription to background step data.
     */
    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("STATUS", "status >> " + status);
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.w(TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }

    public void subscribeDis() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_DISTANCE_DELTA)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("STATUS", "status >> " + status);
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.w(TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }

    public void subscribeSpeed() {
        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_SPEED)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("STATUS", "status >> " + status);
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.w(TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }

    private void cancelSubscriptions() {
        Fitness.RecordingApi.unsubscribe(mClient, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }


    /**
     * Read the current daily step total, computed from midnight of the current day
     * on the device's current timezone.
     */
    private class VerifyDataTask extends AsyncTask<Void, Void, Void> implements ResultCallback<DailyTotalResult> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            iv_goal_activity_refresh_steps.setVisibility(View.GONE);
            pb_goal_activity_refresh_steps.setVisibility(View.VISIBLE);

        }

        protected Void doInBackground(Void... params) {

            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                total = totalSet.isEmpty()
                        ? 0
                        : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
            } else {
                debugLog(TAG,"There was a problem getting the step count.");
            }
            totalNoOfSteps = total;
            getSession().setSteps(Float.toString(total));
            debugLog(TAG,"Total steps: " + total);

            // For calories
            PendingResult<DailyTotalResult> result1 = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult1 = result1.await(30, TimeUnit.SECONDS);
            showCalories(totalResult1.getTotal());

            // For distance
            if (Build.VERSION.SDK_INT >= 23) {
                    PendingResult<DailyTotalResult> result2 = Fitness.HistoryApi.readDailyTotal(mClient, DataType.AGGREGATE_DISTANCE_DELTA);
                    DailyTotalResult totalResult2 = result2.await(30, TimeUnit.SECONDS);
                    showDistance(totalResult2.getTotal());
            } else {
                PendingResult<DailyTotalResult> result2 = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_DISTANCE_DELTA);
                DailyTotalResult totalResult2 = result2.await(30, TimeUnit.SECONDS);
                showDistance(totalResult2.getTotal());
            }

            // For Time
            // For calories
            PendingResult<DailyTotalResult> result3 = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_SPEED);
            DailyTotalResult totalResult3 = result3.await(30, TimeUnit.SECONDS);
            showTime(totalResult3.getTotal());

            /*PendingResult<DailyTotalResult> distanceResult =
                    Fitness.HistoryApi.readDailyTotal(
                            mClient,
                            DataType.TYPE_DISTANCE_DELTA);*/

            /*PendingResult<DailyTotalResult> speedResult =
                    Fitness.HistoryApi.readDailyTotal(
                            mClient,
                            DataType.TYPE_CYCLING_PEDALING_CADENCE);*/


            //distanceResult.setResultCallback(this);
            //speedResult.setResultCallback(this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            iv_goal_activity_refresh_steps.setVisibility(View.VISIBLE);
            pb_goal_activity_refresh_steps.setVisibility(View.GONE);

            if(totalCal != 0.0) {
                //tv_goal_activity_cal.setText(String.format("%.2f", totalCal));
                tv_goal_activity_cal.setText("" + Math.round((totalCal)));
            }else {
                tv_goal_activity_cal.setText(getSession().getCalories());
            }

            if(totalDistance != 0.0)  {
                tv_goal_activity_distance.setText(String.format("%.2f", totalDistance));
            }else {
                tv_goal_activity_distance.setText(getSession().getTotalDistance());
            }

            if(totalNoOfSteps != 0 ){
                tv_goal_activity_no_of_steps.setText(String.valueOf(totalNoOfSteps));
            }else {
                tv_goal_activity_no_of_steps.setText(getSession().getSteps());
            }
            //tv_goal_activity_time.setText(String.valueOf(totalTimeDuration));
        }

        @Override
        public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
            debugLog(TAG,"mGoogleApiAndFitCallbacks.onResult(): " + dailyTotalResult);

            if (dailyTotalResult.getStatus().isSuccess()) {
                List<DataPoint> points = dailyTotalResult.getTotal().getDataPoints();
                debugLog(TAG,"ponts: " + points);
                /*if (!points.isEmpty()) {
                    float mDistanceTotal = points.get(0).getValue(Field.FIELD_ACTIVITY).asFloat();
                    Log.d(TAG, "distance updated: " + mDistanceTotal);
                }*/
            } else {
                debugLog(TAG,"onResult() failed! " + dailyTotalResult.getStatus().getStatusMessage());
            }
        }
    }

    /**
     * Get Total Calories
     * @param caloriesDataSet
     */
    private void showCalories(DataSet caloriesDataSet) {
        float total_cal = 0;
        total_cal = caloriesDataSet.isEmpty()
                ? 0
                : caloriesDataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();

        totalCal = total_cal;
        getSession().setCalories(String.valueOf(Math.round(total_cal)));
        debugLog(TAG,"Total Calories: " + total_cal);
    }

    /**
     * Get Total Distance
     * @param caloriesDataSet
     */
    private void showDistance(DataSet caloriesDataSet) {
        float total_Dis = 0;
        total_Dis = caloriesDataSet.isEmpty()
                ? 0
                : caloriesDataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
        debugLog(TAG,"Total Distance: " + total_Dis);
        getSession().setTotalDistance(Float.toString(total_Dis));
        totalDistance = (float) (total_Dis * 0.001); // convert in KM

    }

    /**
     * Get Total Duration
     * @param caloriesDataSet
     */
    private void showTime(DataSet caloriesDataSet) {

        float total_Time = 0;
        debugLog("Time caloriesDataSet >> " + caloriesDataSet.getDataPoints());
        /*total_Time = caloriesDataSet.isEmpty()
                ? 0
                : caloriesDataSet.getDataPoints().get(0).getValue(Field.FIELD_SPEED).asFloat();

        totalTimeDuration = total_Time;*/
        debugLog(TAG,"Total Duration: " + total_Time);
    }

    /* Read steps , distance , time and calories data */
    private void readData() {
        new VerifyDataTask().execute();
    }

    /* Update steps in every 1 minutes */
    private void updateDataEveryInterval() {

        final ExecutorService es = Executors.newCachedThreadPool();
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                es.submit(new Runnable() {
                    @Override
                    public void run() {
                        //mCamera.enableShutterSound(false);
                        Log.d("TAG", "--->Called 30 seconds<---");
                        readData();

                    }
                });
            }
        }, 0, GOOGLE_FIT_API_UPDATE_DATA_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClient.stopAutoManage(getActivity());
        mClient.disconnect();
    }

    /**
     * Call Goal Info Service
     */
    private void callGoalInfoService() {
        GoalInfoService.getGoalInfo(getActivity(), getSession().getUserId(),
                DateUtility.getCurrentDateIn_yyyy_mm_dd(),
                new APIService.Success<JSONObject>() {
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
    private void  parseResponseGoalInfo(JSONObject jsonObject) {
        try {
            if(jsonObject != null && jsonObject.length() > 0) {

                String status = jsonObject.getString("status");
                debugLog(TAG,"status-->" + status);
                String message = jsonObject.getString("message");
                debugLog(TAG,"message-->" + message);

                if(jsonObject.has("data")){
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);

                    tv_goal_frg_act_starting_cal1.setText(jsonObject1.getString("required_kcal"));
                    tv_goal_frg_goal.setText(jsonObject1.getString("required_kcal") + "\nCAL");

                    tv_goal_frg_act_starting_cal2.setText(jsonObject1.getString("consumed_kcal"));
                    tv_goal_frg_food.setText(jsonObject1.getString("consumed_kcal") + "\nCAL");

                    float requiredCal =  Float.parseFloat(jsonObject1.getString("required_kcal"));
                    float consumedCal = Float.parseFloat(jsonObject1.getString("consumed_kcal"));

                    tv_goal_frg_act_starting_cal3.setText(String.format("%.02f", (requiredCal - consumedCal)));
                    tv_goal_frg_remaining.setText(String.valueOf (Math.round(requiredCal - consumedCal)) + "\nCAL");

                    setCircleProgress(Math.abs((int) requiredCal), Math.abs((int) consumedCal));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Circle Progress Value
     * @param requiredCal
     * @param consumedCal
     */
    private void setCircleProgress(int requiredCal, int consumedCal) {

        debugLog("requiredCal >> "+ requiredCal);
        debugLog("consumedCal >> "+ consumedCal);

        circularProgressBar.setMax(requiredCal);
        circularProgressBar.setProgress(consumedCal);
        //Set Font Style Of The Text view
        FontUtility.condBold(tvProgress,getActivity());
        tvProgress.setText(String.valueOf(consumedCal) +"\nCAL");
    }

    @Click
    public void iv_goal_activity_refresh_steps() {

        if (NetworkUtility.checkIsInternetConnectionAvailable(getContext()))
            readData();
    }

}
