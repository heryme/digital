package com.digitalscale.services;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.vollyrest.APIController;
import com.digitalscale.vollyrest.APIService;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Rahul Padaliya on 3/11/2017.
 */
public class SendImageService extends APIService {
    public static String TAG = SendImageService.class.getName();
    private static final String IMAGE_ZIP_URL = BASE_URL + "profile_picture";

    public static void sendImage(final Context context, HashMap<String,Object> params, final Success<JSONObject> successListener) {

        final Dialog dialog = DialogUtility.showProgress(context);
       /* Map<String, Object> params = new HashMap<>();
        params.put("step", "2");*/

        //Put Language Parameter According Selection Of The Device Language
        if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Svenska")){
            params.put("language","sv");
        }else {
            params.put("language","en");
        }

        logDebug("url >> " + IMAGE_ZIP_URL);
        MultipartRequest sendImage = new MultipartRequest(IMAGE_ZIP_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.hide();
                        Log.d(TAG, response.toString());
                        successListener.onSuccess(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.hide();

                    }
                });

        sendImage.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        APIController.getInstance(context).addRequest(sendImage, TAG);

    }

    private static void logDebug(String message) {
        Log.d(TAG, message);
    }
}


