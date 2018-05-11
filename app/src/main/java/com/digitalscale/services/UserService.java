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
 * Created by Rahul Padaliya on 4/7/2017.
 */
public class UserService extends APIService {

    private static final String TAG = UserService.class.getSimpleName();
    private static final String VERIFY_OTP = APIService.BASE_URL + "verify_otp";
    private static final String RESET_PWD = APIService.BASE_URL + "reset_password";
    private static final String SIGN_UP = APIService.BASE_URL + "register";
    private static final String LOGIN = APIService.BASE_URL + "login";
    private static final String UPDATE_INITIAL_SETTING = APIService.BASE_URL + "update_initial_setting";
    private static final String VERIFY_EMAIL = APIService.BASE_URL + "verify_email";
    private static final String FORGOT_PASSWORD = APIService.BASE_URL + "forgot_password";


    private static final String EMAIL = "email";
    private static final String OTP = "otp";

    private static final String TOKEN = "token";
    private static final String NEW_PASSWORD = "new_password";
    private static final String CONFORM_PASSWORD = "confirm_password";

    /* For sign up & forgot password api, default retries policy change(0),
     so only one request is triggered when server is down
     or any other network problems*/
    private static final int SIGNUP_RETRIES = 0;
    private static final int FORGOT_PASSWORD_RETRIES = 0;

    /**
     * Call Verify Forgot Password API
     * @param context
     * @param email
     * @param otp
     * @param successListener
     */
    public static void verifyForgotPwdOtp(final Context context,
                                          final String email,
                                          final String otp ,
                                          final APIService.Success<JSONObject> successListener) {

        //final ProgressDialog dialog = DialogUtility.processDialog(context, "Getting Overview....", false);

        StringRequest verifyOtpRequest = new StringRequest(Request.Method.POST, VERIFY_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "param email >> " + email);
                Log.d(TAG, "param otp >> " + otp);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(EMAIL, email);
                params.put(OTP, otp);
                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(verifyOtpRequest, TAG);

    }

    /**
     * Call Reset Password API
     * @param context
     * @param email
     * @param token
     * @param new_password
     * @param confirm_password
     * @param successListener
     */

    public static void resetPassword(final Context context,
                                          final String email,
                                          final String token,
                                          final String new_password,
                                          final String confirm_password,
                                          final APIService.Success<JSONObject> successListener) {

        //final ProgressDialog dialog = DialogUtility.processDialog(context, "Getting Overview....", false);

        StringRequest resetPasswordRequest = new StringRequest(Request.Method.POST, RESET_PWD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "param email >> " + email);
                Log.d(TAG, "param token >> " + token);
                Log.d(TAG, "param new_password >> " + new_password);
                Log.d(TAG, "param confirm_password >> " + confirm_password);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(EMAIL, email);
                params.put(TOKEN, token);
                params.put(NEW_PASSWORD, new_password);
                params.put(CONFORM_PASSWORD, confirm_password);
                return getLanguageType(params);
            }
        };

        APIController.getInstance(context).addRequest(resetPasswordRequest, TAG);

    }

    /* sign up user */
    public static void signUp(final Context context,
                              final HashMap<String, String> params,
                              final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "SIGN_UP >> " + SIGN_UP);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest signUpRequest = new StringRequest(Request.Method.POST, SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Sign up response >> " + response);
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
                    error.printStackTrace();
                    Log.d(TAG, "error >> " + error.networkResponse);
                    System.out.print(new String(error.networkResponse.data));
                }
                Toast.makeText(context, "Something is wrong", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getLanguageType(params);
            }
        };

        APIService.setRequestPolicy(signUpRequest);

        APIController.getInstance(context).addRequest(signUpRequest, TAG);
    }

    /* Login  user */
    public static void login(final Context context,
                             final HashMap<String, String> params,
                             final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "LOGIN >> " + LOGIN);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest loginRequest = new StringRequest(Request.Method.POST, LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Login response >> " + response);
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

        APIController.getInstance(context).addRequest(loginRequest, TAG);
    }

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

    /* Verify email with otp  */
    public static void verifyEmail(final Context context,
                                   final HashMap<String, String> params,
                                   final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "VERIFY_EMAIL >> " + VERIFY_EMAIL);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest verifyEmailRequest = new StringRequest(Request.Method.POST, VERIFY_EMAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Verify email response >> " + response);
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

        APIController.getInstance(context).addRequest(verifyEmailRequest, TAG);
    }

    /* Forgot password   */
    public static void forgotPassword(final Context context,
                                      final HashMap<String, String> params,
                                      final APIService.Success<JSONObject> successListener) {

        Log.d(TAG, "FORGOT_PASSWORD >> " + FORGOT_PASSWORD);
        Log.d(TAG, "PARAM >> " + params);

        final ProgressDialog dialog = DialogUtility.showProgress(context);

        StringRequest forgotPasswordRequest = new StringRequest(Request.Method.POST, FORGOT_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DialogUtility.hideProgress(dialog);
                    Log.d(TAG, "Forgot Password response >> " + response);
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

        APIService.setRequestPolicy(forgotPasswordRequest);

        APIController.getInstance(context).addRequest(forgotPasswordRequest, TAG);
    }
}
