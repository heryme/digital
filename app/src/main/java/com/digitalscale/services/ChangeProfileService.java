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
public class ChangeProfileService extends APIService {

    private static final String TAG = ChangeProfileService.class.getSimpleName();
    private static final String URL_CHANGE_PROFILE = APIService.BASE_URL + "update_profile";

    //Change Password
    public static void changeProfile(final Context context,
                               final HashMap<String, String> params,
                               final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "GET PROFILE >> " + URL_CHANGE_PROFILE);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest changeProfile = new StringRequest(Request.Method.POST, URL_CHANGE_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Change profile response >> " + response);
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
                Log.d(TAG, "Change profile response error");
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(changeProfile, TAG);
    }

}
