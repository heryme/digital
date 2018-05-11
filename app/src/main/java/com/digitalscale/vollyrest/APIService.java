package com.digitalscale.vollyrest;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Ankur on 24-11-2015.
 */
public abstract class APIService {

    private static final String TAG = APIService.class.getSimpleName();

    private static final int REQUEST_TIME = 5000; // time in millisecond

    private static final int NO_OF_RETRIES = 0;

    //public static final String BASE_URL = "http://local.websoptimization.com:86/digital-scale-web/public/api/";// live

    // Port : 83 For Local
    //public static final String BASE_URL = "http://local.websoptimization.com:83/digital-scale/public/api/";// local

    //Port :89 For Demo
    public static final String BASE_URL = "http://local.websoptimization.com:89/digital-scale-web/public/api/";// demo

    //public static final String BASE_URL = "http://192.168.1.26/digital-scale-web/public/api/";// local


    private static final String UN_AUTHORIZED_MESSAGE = "Your session is expired,Please login again";


    public interface Success<T> {
        public void onSuccess(T response);
    }

    public interface Error<T>
    {
        public void onError(T error);
    }

    /*protected static void handleError(final Context context, VolleyError error) {
        Log.d(TAG, "Error :: " + error);

        if (error instanceof NoConnectionError) {

            Toast.makeText(context, "Please check internet connection !", Toast.LENGTH_LONG).show();

        } else if (error.networkResponse != null) {

            Log.d(TAG, "CODE =" + error.networkResponse.statusCode);
            Log.d(TAG, new String(error.networkResponse.data));

            if (error.networkResponse.statusCode == 401) {
                //Not Authorized Error Display and Redirect to Login Screen
                //DialogUtility.alertDialogUnauthorized(context, UN_AUTHORIZED_MESSAGE);
                Log.d(TAG, "Session expired(Unauthorized error)");
                if (MainActivity.unAuthorizeLoginCount < 1) {
                    MainActivity.unAuthorizeLoginCount++;
                    // Automatically user re-login
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("relogin", true);
                    //UserSession.getInstance(context).setIsDeviceRegister(true);
                    context.startActivity(intent);
                } else {
                    // Manually user re-login
                    UserSession.getInstance(context).clearUserSession(context);
                    Intent intent = new Intent(context, SplashActivity.class);
                    context.startActivity(intent);
                }


            } else {
                //Read Error Response and Display Error Dialog
                DialogUtility.alertErrorMessage(context, getErrorMessage(error.networkResponse.data));
            }
        }
    }*/

    private static String getErrorMessage(byte[] responseData) {
        String message = null;
        String code = null;
        try {
            JSONObject jsonObject = new JSONObject(new String(responseData));
            JSONObject subJsonObj = jsonObject.getJSONObject("error");
            code = subJsonObj.getString("code");
            message = subJsonObj.getString("message");
        } catch (Exception e) {
            Log.d(TAG, "getErrorMessage" + e.getMessage());
            message = "Something is wrong,Please try again";
        }
        return message;
    }

    /* Set request policy : request must be trigger one time only for 5000 millisecond*/
    public static void setRequestPolicy(StringRequest stringRequest) {
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                NO_OF_RETRIES,
                REQUEST_TIME,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    //Put Language Parameter According Selection Of The Device Language
    public static HashMap<String,String> getLanguageType(HashMap<String,String> param){

        if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Svenska")){
            param.put("language","sv");
        }else {
            param.put("language","en");
        }
        Log.d(TAG,"Check Language--->" + param);
        return param;
    }

}
