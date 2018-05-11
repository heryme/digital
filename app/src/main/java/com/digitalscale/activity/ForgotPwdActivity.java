package com.digitalscale.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.services.UserService;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.utility.ValidationUtils;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Rahul Padaliya on 4/7/2017.
 */
@Fullscreen
@EActivity(R.layout.activity_forgot_pwd)
public class ForgotPwdActivity  extends MasterActivity {

    private final String TAG = ForgotPwdActivity.class.getName();

    @ViewById(R.id.edt_forgot_pwd_email)
    EditText edt_forgot_pwd_email;

    @ViewById(R.id.btn_forgot_pwd_re_send_otp)
    Button btn_forgot_pwd_re_send_otp;

    @ViewById(R.id.btn_forgot_pwd_send_otp)
    Button btn_forgot_pwd_send_otp;

    @ViewById(R.id.tv_email_verification)
    TextView tv_email_verification;

    ValidationUtils validationUtils;

    Session session;

    @AfterViews
    public void init() {
        validationUtils = new com.digitalscale.utility.ValidationUtils(ForgotPwdActivity.this);
        session = new Session(ForgotPwdActivity.this);
        setFontStyle();
    }

    @Click
    public void btn_forgot_pwd_send_otp() {

        if (NetworkUtility.checkIsInternetConnectionAvailable(ForgotPwdActivity.this) && isValidInputForLogin()) {
            session.setEmailIDForgotPwd(edt_forgot_pwd_email.getText().toString());
            //Call Forgot Password API
            callForgotPasswordAPI();
        }
    }

    @Click
    public void btn_forgot_pwd_re_send_otp() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(ForgotPwdActivity.this) && isValidInputForLogin()) {
            session.setEmailIDForgotPwd(edt_forgot_pwd_email.getText().toString());
            //Call Forgot Password API
            callForgotPasswordAPI();
        }
    }

    /**
     * check login validation
     * @return
     */
    public boolean isValidInputForLogin() {
        if (validationUtils.isValidEditText(edt_forgot_pwd_email)
                && validationUtils.isValidEditTextEmail(edt_forgot_pwd_email)) {
            return true;
        }
        return false;
    }


    /**
     * Call Forgot Password API
     */
    private void callForgotPasswordAPI() {

        HashMap<String, String> param = new HashMap<>();
        param.put("email", edt_forgot_pwd_email.getText().toString().trim());

        UserService.forgotPassword(ForgotPwdActivity.this, param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                String status = response.optString("status");
                String message = response.optString("message");

                if (status.equals("1")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPwdActivity.this);
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    String positiveText = "OK";/*getString(android.R.string.yes);*/
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                    if (dialog != null)
                                        dialog.cancel();

                                    Intent intent = new Intent(ForgotPwdActivity.this, VerifyCodeForgotPwdActivity_.class);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();

                } else {
                    DialogUtility.dialogWithPositiveButton(message,ForgotPwdActivity.this);
                }
            }
        });
    }

    /**
     * Set Font Style Text view, Edit text And Button
     */
    private void setFontStyle(){
        FontUtility.condLight(tv_email_verification,ForgotPwdActivity.this);
        FontUtility.condLight(edt_forgot_pwd_email,ForgotPwdActivity.this);
        FontUtility.condLight(btn_forgot_pwd_send_otp,ForgotPwdActivity.this);
        FontUtility.condLight(btn_forgot_pwd_re_send_otp,ForgotPwdActivity.this);
    }
}

