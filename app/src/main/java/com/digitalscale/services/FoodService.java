package com.digitalscale.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalscale.fragments.DiaryFragment;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.vollyrest.APIController;
import com.digitalscale.vollyrest.APIService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vishal Gadhiya on 3/30/2017.
 */

public class FoodService extends APIService{

    private static final String TAG = FoodService.class.getSimpleName();

    private static final String URL_GET_FOOD = APIService.BASE_URL + "get_foods";

    private static final String URL_GET_FOOD_HISTORY = APIService.BASE_URL + "get_food_history";

    private static final String URL_ADD_FOOD = APIService.BASE_URL + "add_food";

    public static void searchFood(final Context context, final HashMap<String, String> param, String requestTag, final APIService.Success<JSONObject> successListener, final APIService.Error<VolleyError> errorListener) {

        //final ProgressDialog dialog = DialogUtility.processDialog(context, "Getting Overview....", false);

        StringRequest searchFoodRequest = new StringRequest(Request.Method.POST, URL_GET_FOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG,"Search food response \n"+response);
                    successListener.onSuccess(new JSONObject(response));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"error >> "+ error);
                errorListener.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "param >> " + param);
                return getLanguageType(param);
            }
        };

        APIController.getInstance(context).addRequest(searchFoodRequest, requestTag);

    }

    /* Food history api */

    public static void getFoodHistory(final Context context, final HashMap<String, String> param, final ProgressBar progressBar, final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "URL_GET_FOOD_HISTORY >> " + URL_GET_FOOD_HISTORY);
        Log.d(TAG, "param >> " + param);
        //final ProgressDialog dialog = DialogUtility.showProgress(context);
        progressBar.setVisibility(View.VISIBLE);
        StringRequest getFoodHistoryRequest = new StringRequest(Request.Method.POST, URL_GET_FOOD_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressBar.setVisibility(View.GONE);
                    successListener.onSuccess(new JSONObject(response));
                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    DiaryFragment.tv_diary_frg_food.setText("--");
                    DiaryFragment.tv_diary_frg_goal.setText("--");
                    DiaryFragment.tv_tv_diary_frg_remaining.setText("--");
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getLanguageType(param);
            }
        };

        APIController.getInstance(context).cancelPendingRequests("Cancel food history request");
        APIController.getInstance(context).addRequest(getFoodHistoryRequest, "getFoodHistoryRequest");

    }

    /* Add food api */

    public static void addFood(final Context context, final HashMap<String, String> param, final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "URL_ADD_FOOD >> " + URL_ADD_FOOD);
        Log.d(TAG, "param >> " + param);
        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest addFoodRequest = new StringRequest(Request.Method.POST, URL_ADD_FOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    successListener.onSuccess(new JSONObject(response));
                } catch (Exception e) {
                    DialogUtility.hideProgress(dialog);
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtility.hideProgress(dialog);
                if (error != null) {
                    Log.d(TAG, "Error >> " + error);
                }
                /*dialog.hide();
                handleError(context, error);*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getLanguageType(param);
            }
        };

        APIService.setRequestPolicy(addFoodRequest);

        APIController.getInstance(context).addRequest(addFoodRequest, "addFoodRequest");

    }

    private void debugLog(String message) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, message);
    }
}
