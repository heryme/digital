package com.digitalscale.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.activity.LoginActivity_;
import com.digitalscale.services.ChangePasswordService;
import com.digitalscale.utility.DialogUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.utility.ValidationUtils;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vishal Gadhiya on 3/15/2017.
 */

@EFragment(R.layout.fragment_change_password)
public class ChangePasswordFragment extends MasterFragment {

    @ViewById(R.id.tv_frg_change_pwd_current_pwd)
    TextView tv_frg_change_pwd_current_pwd;

    @ViewById(R.id.tv_frg_change_pwd_new_pwd)
    TextView tv_frg_change_pwd_new_pwd;

    @ViewById(R.id.tv_frg_change_pwd_conform_pwd)
    TextView tv_frg_change_pwd_conform_pwd;

    @ViewById(R.id.ev_frg_change_pwd_current_pwd)
    EditText ev_frg_change_pwd_current_pwd;

    @ViewById(R.id.ev_frg_change_pwd_new_pwd)
    EditText ev_frg_change_pwd_new_pwd;

    @ViewById(R.id.et_frg_change_pwd_conform_pwd)
    EditText et_frg_change_pwd_conform_pwd;

    @ViewById(R.id.btn_frg_change_pwd_save)
    Button btn_frg_change_pwd_save;

    ValidationUtils validationUtils;

    @AfterViews
    public void init() {
        validationUtils = new ValidationUtils(getContext());
        //Call Change Password API
        changeFontStyle();
    }

    @Click
    public void btn_frg_change_pwd_save(){
        if (checkChangePasswordValidation()) {
            callChangePasswordAPI();
        }
    }

    /* Check change password validation */
    private boolean checkChangePasswordValidation() {
        if (NetworkUtility.checkIsInternetConnectionAvailable(getContext()) &&
                validationUtils.isValidEditText(ev_frg_change_pwd_current_pwd)
                && validationUtils.isValidEditText(ev_frg_change_pwd_new_pwd)
                && validationUtils.isValidEditText(et_frg_change_pwd_conform_pwd)
                && validationUtils.isValidEditTextPassword(ev_frg_change_pwd_current_pwd)
                && validationUtils.isValidEditTextPassword(ev_frg_change_pwd_new_pwd)
                && validationUtils.isValidEditTextPassword(et_frg_change_pwd_conform_pwd)
                && ValidationUtils.isConformPassword(ev_frg_change_pwd_new_pwd, et_frg_change_pwd_conform_pwd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Change Font Style
     */
    private void changeFontStyle(){
        //Text view
        FontUtility.condLight(tv_frg_change_pwd_current_pwd,getContext());
        FontUtility.condLight(tv_frg_change_pwd_new_pwd,getContext());
        FontUtility.condLight(tv_frg_change_pwd_conform_pwd,getContext());

        //Editext
        FontUtility.condLight(ev_frg_change_pwd_current_pwd,getContext());
        FontUtility.condLight(ev_frg_change_pwd_new_pwd,getContext());
        FontUtility.condLight(et_frg_change_pwd_conform_pwd,getContext());

        //Button
        FontUtility.condLight(btn_frg_change_pwd_save,getContext());
    }

    /**
     * Call API Change Password
     */
    private void callChangePasswordAPI(){
        HashMap<String,String> param = new HashMap<>();
        param.put("user_id",getSession().getUserId());
        param.put("old_password",ev_frg_change_pwd_current_pwd.getText().toString());
        param.put("new_password",ev_frg_change_pwd_new_pwd.getText().toString());
        param.put("confirm_password",et_frg_change_pwd_conform_pwd.getText().toString());
        ChangePasswordService.changePassword(getContext(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                if(response != null && response.length() > 0) {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");

                        if(status.equals("1")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(message);
                            builder.setCancelable(false);
                            String positiveText = "OK";
                            builder.setPositiveButton(positiveText,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // positive button logic
                                            if (dialog != null)
                                                dialog.cancel();
                                            //Logout User
                                            getSession().clearSession();

                                            Intent redirect = new Intent(getContext(),LoginActivity_.class);
                                            getActivity().startActivity(redirect);
                                            getActivity().finish();
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            // display dialog
                            dialog.show();
                        }else {
                            DialogUtility.dialogWithPositiveButton(message,getContext());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
