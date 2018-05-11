package com.digitalscale.google_place_api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.digitalscale.vollyrest.APIController;
import com.digitalscale.vollyrest.APIService;

import org.json.JSONObject;

/**
 * Created by Rahul Padaliya on 5/17/2017.
 */
public class AddressService extends APIService {

    final static String TAG = APIService.class.getName();

    private static final String USER_ID = "user_id";
    private static final String DATE = "date";

    /**
     * Call Goal Info Service
     * @param context
     *@param successListener
     */

    public static void getAddress(final Context context,
                                  String url,
                                  final APIService.Success<JSONObject> successListener) {

        //final ProgressDialog dialog = DialogUtility.showProgress(context);
        Log.d(TAG,"Urlll-->" + url);

        StringRequest address = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
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
        }); /*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return param;
            }
        };*/

        APIController.getInstance(context).addRequest(address, TAG);
    }
}
