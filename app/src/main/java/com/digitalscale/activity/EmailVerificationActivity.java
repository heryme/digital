package com.digitalscale.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.parser.UserParser;
import com.digitalscale.services.UserService;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONObject;

import java.util.HashMap;

@EActivity(R.layout.activity_email_verification)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class EmailVerificationActivity extends MasterActivity {

    @ViewById(R.id.edtVerifyCodeVerificationCode)
    EditText edtVerificationCode;

    @ViewById(R.id.btn_email_verification_send_otp)
    Button btn_email_verification_send_otp;

    @ViewById(R.id.btn_email_verification_re_send_otp)
    Button btn_email_verification_re_send_otp;

    @ViewById(R.id.tv_email_verification)
    TextView tv_email_verification;

    @ViewById(R.id.tv_email_verification_check_email)
    TextView tv_email_verification_check_email;

    @ViewById(R.id.tv_act_email_verification)
    TextView tv_act_email_verification;

    @AfterViews
    public void init() {
        setFontStyle();
        tv_email_verification_check_email.setText(getString(R.string.lbl_please_check_your_email) + " : " + getSession().getEmail());
    }

    @Click
    public void btn_email_verification_send_otp() {

        if (validationUtils.isVerificationCodeIsValid(edtVerificationCode)) {
            HashMap<String, String> param = new HashMap<>();
            param.put("email", getSession().getEmail());
            param.put("otp", edtVerificationCode.getText().toString());

            UserService.verifyEmail(EmailVerificationActivity.this, param, new APIService.Success<JSONObject>() {
                @Override
                public void onSuccess(final JSONObject response) {

                    String status = response.optString("status");
                    String message = response.optString("message");

                    if (status.equals("1")) {
                        debugLog("success");

                        AlertDialog.Builder builder = new AlertDialog.Builder(EmailVerificationActivity.this);
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

                                            getSession().setIsUserVerifyEmailId(false);
                                            UserParser.SignUp signUp = UserParser.SignUp.parseSignUpResponse(response);

                                            getSession().setUserId(signUp.getUserId());
                                            getSession().setEmail(signUp.getEmail());
                                            getSession().setFirstName(signUp.getFirstName());
                                            getSession().setLastName(signUp.getLastName());
                                            getSession().setProfilePic(signUp.getProfilePic());
                                            redirectTo(signUp.getInitialSetting());

                                            debugLog(TAG,"Email Vrifity Activity-->" + signUp.getFirstName());
                                            startActivity(new Intent(EmailVerificationActivity.this, Question1Activity_.class));
                                            finish();

                                    }
                                });

                        AlertDialog dialog = builder.create();
                        // display dialog
                        dialog.show();
                    } else if (status.equalsIgnoreCase("2")) {
                        DialogUtility.dialogWithPositiveButton(message,EmailVerificationActivity.this);
                    } else {
                        DialogUtility.dialogWithPositiveButton(message,EmailVerificationActivity.this);
                    }
                }
            });
        }
    }

    @Click
    public void tv_act_email_verification() {
        Intent intent = new Intent(EmailVerificationActivity.this,LoginActivity_.class);
        intent.putExtra("showSighUP",true);
        startActivity(intent);
        finish();
    }

    /**
     * Set Font Style Textview,Edittext And Button
     */
    private void setFontStyle(){
        FontUtility.condLight(tv_email_verification,EmailVerificationActivity.this);
        FontUtility.condLight(tv_email_verification_check_email,EmailVerificationActivity.this);
        FontUtility.condLight(edtVerificationCode,EmailVerificationActivity.this);
        FontUtility.condLight(btn_email_verification_send_otp,EmailVerificationActivity.this);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        tv_act_email_verification.performClick();
    }

    /**
     * Redirect Question1Activity Or MainActivity
     * @param initialSetting
     */
    void redirectTo(String initialSetting) {
        if (initialSetting.equalsIgnoreCase("0")) {
            startActivity(new Intent(EmailVerificationActivity.this, Question1Activity_.class));
        } else {
            getSession().setIsUserFillBasicDetails(true);
            startActivity(new Intent(EmailVerificationActivity.this, MainActivity_.class));
        }
    }
}
