package com.digitalscale.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalscale.vollyrest.APIController;
import com.digitalscale.vollyrest.APIService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul Padaliya on 4/8/2017.
 */
public class GoalInfoService extends APIService {
    final static  String TAG = GoalInfoService.class.getName();

    private static final String URL_GET_GOAL_INFO = APIService.BASE_URL + "get_goal_info";

    private static final String USER_ID = "user_id";
    private static final String DATE = "date";

    /**
     * Call Goal Info Service
     * @param context
     * @param user_id
     * @param date
     * @param successListener
     */

    public static void getGoalInfo(final Context context,
                                   final String user_id,
                                   final String date,
                                   final APIService.Success<JSONObject> successListener) {

         //final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest goalInfoRequest = new StringRequest(Request.Method.POST, URL_GET_GOAL_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
           //         dialog.hide();
                    successListener.onSuccess(new JSONObject(response));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*dialog.hide();
                handleError(context, error);*/
                //dialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "param user_id >> " + user_id);
                Log.d(TAG, "param date >> " + date);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(USER_ID, user_id);
                params.put(DATE, date);
                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(goalInfoRequest, TAG);

    }
}
