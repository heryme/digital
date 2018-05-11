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
public class ChangePasswordService extends APIService {

    private static final String TAG = ChangePasswordService.class.getSimpleName();
    private static final String URL_CHANGE_PASSWORD = APIService.BASE_URL + "change_password";

    //Change Password
    public static void changePassword(final Context context,
                               final HashMap<String, String> params,
                               final Success<JSONObject> successListener) {

        Log.d(TAG, "CHANGE PASSWORD >> " + URL_CHANGE_PASSWORD);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest changePwd = new StringRequest(Request.Method.POST, URL_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Change password response >> " + response);
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
                Log.d(TAG, "Change password response error");
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(changePwd, TAG);
    }

}
