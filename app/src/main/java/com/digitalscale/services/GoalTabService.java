package com.digitalscale.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.vollyrest.APIController;
import com.digitalscale.vollyrest.APIService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul Padaliya on 5/12/2017.
 */
public class GoalTabService extends APIService {

    private static final String TAG = GoalTabService.class.getSimpleName();
    private static final String UPDATE_INITIAL_SETTING = BASE_URL + "update_initial_setting";
    private static final String GET_INITIAL_SETTING = BASE_URL + "get_initial_setting";

    /* User initial setting  */
    public static void updateInitialSetting(final Context context,
                                            final HashMap<String, String> params,
                                            final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "UPDATE_INITIAL_SETTING >> " + UPDATE_INITIAL_SETTING);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest updateInitialSettingRequest = new StringRequest(Request.Method.POST, UPDATE_INITIAL_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Update initial setting response >> " + response);
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
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(updateInitialSettingRequest, TAG);
    }


    /* User initial setting  */
    public static void getInitialSettings(final Context context,
                                            final HashMap<String, String> params,
                                            final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "UPDATE_INITIAL_SETTING >> " + GET_INITIAL_SETTING);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest getInitialSettingRequest = new StringRequest(Request.Method.POST, GET_INITIAL_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "get initial setting response >> " + response);
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
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(getInitialSettingRequest, TAG);
    }
}
