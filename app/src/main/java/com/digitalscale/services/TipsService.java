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
 * Created by Vishal Gadhiya on 4/19/2017.
 */

public class TipsService extends APIService {

    private static final String TAG = TipsService.class.getSimpleName();
    private static final String URL_GET_TIPS = APIService.BASE_URL + "get_tips";
    private static final String URL_GET_TIPS_DETAIL = APIService.BASE_URL + "get_tip_detail";


    /* Get tips */
    public static void getTips(final Context context,
                               final HashMap<String, String> params,
                               final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "GET TIPS >> " + URL_GET_TIPS);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest getTipsRequest = new StringRequest(Request.Method.POST, URL_GET_TIPS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Get tips response >> " + response);
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
                Log.d(TAG, "Get Tips response error");
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(getTipsRequest, TAG);
    }

    /* Get tips detail */
    public static void getTipsDetail(final Context context,
                               final HashMap<String, String> params,
                               final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "GET TIPS DETAIL >> " + URL_GET_TIPS_DETAIL);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest getTipDetailRequest = new StringRequest(Request.Method.POST, URL_GET_TIPS_DETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Get tips details response >> " + response);
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
                Log.d(TAG, "Get Tips detail response error");
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(getTipDetailRequest, TAG);
    }
}
