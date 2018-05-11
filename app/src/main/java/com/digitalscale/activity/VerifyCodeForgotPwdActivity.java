package com.digitalscale.activity;

import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.services.UserService;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONException;
import org.json.JSONObject;

@EActivity(R.layout.activity_verify_code_forgot_pwd)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class VerifyCodeForgotPwdActivity extends MasterActivity {

    private final String TAG = VerifyCodeForgotPwdActivity.class.getName();

    @ViewById(R.id.edt_verify_code_forgot_pwd_otp)
    EditText edt_verify_code_forgot_pwd_otp;

    @ViewById(R.id.btn_verify_code_forgot_pwd_otp)
    Button btn_verify_code_forgot_pwd_otp;

    @ViewById(R.id.tv_email_verification)
    TextView tv_email_verification;

    Session session;

    @AfterViews
    public void init() {
        session = new Session(VerifyCodeForgotPwdActivity.this);
        session.getEmailIDForgotPwd();
        setFontStyle();
    }

    @Click
    public void btn_verify_code_forgot_pwd_otp() {

        debugLog(TAG,"Email id-->" + session.getEmailIDForgotPwd());
        //Call API Verify OTP
        if (validationUtils.isValidEditText(edt_verify_code_forgot_pwd_otp))
        callVerifyOtpAPI();
    }

    /**
     * Call Verify OTP API
     */
    private void callVerifyOtpAPI(){
        UserService.verifyForgotPwdOtp(this,
                session.getEmailIDForgotPwd(),
                edt_verify_code_forgot_pwd_otp.getText().toString(),
                new APIService.Success<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        debugLog(TAG,"Call Forgot Password Response--->" + response.toString());
                        parseResponse(response);

            }
        });

    }

    /**
     * Parse Json
     * @param jsonObject
     */
    private void parseResponse(JSONObject jsonObject){
        try {
            String status = jsonObject.getString("status");
            debugLog(TAG,"status-->" + status);
            String message = jsonObject.getString("message");
            debugLog(TAG,"message-->" + message);
            if(jsonObject.has("data")){
                String data = jsonObject.getString("data");
                JSONObject token = new JSONObject(data);
                token.getString("token");
                debugLog(TAG,"token-->" + token.getString("token"));
                Intent intent = new Intent(this,ResetPwdActivity_.class);
                intent.putExtra("token", token.getString("token"));
                startActivity(intent);
            }else {
                DialogUtility.dialogWithPositiveButton(message,VerifyCodeForgotPwdActivity.this);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set Font Style Of The Textview,Edittext And Button
     */
    private void setFontStyle(){
        FontUtility.condLight(tv_email_verification,VerifyCodeForgotPwdActivity.this);
        FontUtility.condLight(edt_verify_code_forgot_pwd_otp,VerifyCodeForgotPwdActivity.this);
        FontUtility.condLight(btn_verify_code_forgot_pwd_otp,VerifyCodeForgotPwdActivity.this);
    }
}
