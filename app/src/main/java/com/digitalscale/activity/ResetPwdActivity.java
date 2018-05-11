package com.digitalscale.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.services.UserService;
import com.digitalscale.tools.Session;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.ValidationUtils;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONException;
import org.json.JSONObject;

@EActivity(R.layout.activity_reset_pwd)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)

public class ResetPwdActivity extends MasterActivity {

    @ViewById(R.id.edt_rest_pwd_new_pwd)
    EditText edt_rest_pwd_new_pwd;

    @ViewById(R.id.edt_reset_conform_pwd)
    EditText edt_reset_conform_pwd;

    @ViewById(R.id.btn_reset_pwd)
    Button btn_reset_pwd;

    @ViewById(R.id.tv_email_verification)
    TextView tv_email_verification;

    ValidationUtils validationUtils;

    Session session;

    String token;

    @AfterViews
    public void init() {
        session = new Session(this);
        setFontStyle();
        validationUtils = new com.digitalscale.utility.ValidationUtils(ResetPwdActivity.this);
        Intent intent = getIntent();
        token =  intent.getStringExtra("token");
        Log.d("Tag","Token--->" + token);
    }

    @Click
    public void btn_reset_pwd() {

        if(validationUtils.isValidEditText(edt_rest_pwd_new_pwd) &&
                validationUtils.isValidEditTextPassword(edt_rest_pwd_new_pwd) &&
                validationUtils.isValidEditText(edt_reset_conform_pwd)) {
            if(edt_rest_pwd_new_pwd.getText().toString().equals(edt_reset_conform_pwd.getText().toString())) {
                //Reset Password API
                callResetPwdAPI();
            }else {
                DialogUtility.dialogWithPositiveButton(getString(R.string.msg_confirm_password_must_be_same_as_new_password),ResetPwdActivity.this);
            }
        }
    }

    /**
     * Reset Password API
     */
    private void callResetPwdAPI() {
        UserService.resetPassword(this, session.getEmailIDForgotPwd(),
                token,
                edt_rest_pwd_new_pwd.getText().toString(),
                edt_reset_conform_pwd.getText().toString(),
                new APIService.Success<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d(TAG,response.toString());
                        try {
                                String status = response.getString("status");
                                String message = response.getString("message");

                                if(status.equals("1")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwdActivity.this);
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

                                                    Intent intent = new Intent(ResetPwdActivity.this,LoginActivity_.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.putExtra("EXIT", true);
                                                    startActivity(intent);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            });

                                    AlertDialog dialog = builder.create();
                                    // display dialog
                                    dialog.show();
                                }else {
                                    DialogUtility.dialogWithPositiveButton(message,ResetPwdActivity.this);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Set Font Style On The Textview,EditText
     */
    private void setFontStyle(){
        FontUtility.condLight(tv_email_verification,ResetPwdActivity.this);
        FontUtility.condLight(edt_rest_pwd_new_pwd,ResetPwdActivity.this);
        FontUtility.condLight(edt_reset_conform_pwd,ResetPwdActivity.this);
        FontUtility.condLight(btn_reset_pwd,ResetPwdActivity.this);
    }
}
