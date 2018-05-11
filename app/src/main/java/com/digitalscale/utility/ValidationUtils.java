package com.digitalscale.utility;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.digitalscale.R;

/**
 * Created by Vishal Gadhiya on 3/7/2017.
 */

public class ValidationUtils {

    private Context context;

    public ValidationUtils(Context context) {
        this.context = context;
    }

    // Is valid edit text or empty
    public boolean isValidEditText(EditText editText) {
        String str = editText.getText().toString().trim();
        if (str != null && !str.equals("") && str.length() > 0) {
            editText.setError(null);
            return true;
        }
        else {
            editText.requestFocus();
            editText.setCursorVisible(true);
            editText.setError(context.getString(R.string.msg_field_required));
            return false;
        }

    }

    // is valid email
    public boolean isValidEditTextEmail(EditText edtEmail) {
        String email = edtEmail.getText().toString().trim();
        if (email == null ? false : Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(null);
            return true;
        }
        else {
            edtEmail.requestFocus();
            edtEmail.setCursorVisible(true);
            edtEmail.setError(context.getString(R.string.msg_enter_valid_email));
            return false;
        }
    }

    /* check input value is 0(ZERO)*/
    public boolean checkInputIsZero(EditText editText) {
        String temp = editText.getText().toString().trim();
        if (temp != null && temp.length() > 0) {
            Float f = Float.parseFloat(temp);
            if (f == 0f) {
                editText.requestFocus();
                editText.setCursorVisible(true);
                editText.setError(context.getString(R.string.msg_enter_valid_data));
                return true;
            }
        }

        return false;
    }

    // is valid password
    public boolean isValidEditTextPassword(EditText edtPassword) {
        String password = edtPassword.getText().toString().trim();

        if (password.contains(" ")) {
            edtPassword.requestFocus();
            edtPassword.setCursorVisible(true);
            edtPassword.setError(context.getString(R.string.msg_space_is_not_allowed));
            return false;
        }
        if (password != null && password.length() > 0) {
            if (password.length() < 6) {
                edtPassword.requestFocus();
                edtPassword.setCursorVisible(true);
                edtPassword.setError(context.getString(R.string.msg_password_must_be_6_digits));
                return false;
            }
        }
        return true;
    }

    public boolean isValidAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView){
       String str =  autoCompleteTextView.getText().toString().trim();
        if (str != null && !str.equals("") && str.length() > 0) {
            autoCompleteTextView.setError(null);
            return true;
        }
        else {
            autoCompleteTextView.requestFocus();
            autoCompleteTextView.setCursorVisible(true);
            autoCompleteTextView.setError(context.getString(R.string.msg_this_field_is_required));
            return false;
        }
    }


    // Verification code must be 6 digits
    public boolean isVerificationCodeIsValid(EditText editText) {
        if (isValidEditText(editText)) {
            if (editText.getText().toString().length() < 6) {
                editText.requestFocus();
                editText.setCursorVisible(true);
                editText.setError(context.getString(R.string.msg_password_must_be_6_digits));
                return false;
            }
        } else
            return false;

        return true;
    }

    public static boolean isConformPassword(EditText editText,EditText editText1){
        if (editText.getText().toString().equals(editText1.getText().toString())){
            return true;
        }else {
            editText1.setError("Confirm  password does not match");
            return false;
        }
    }

    /**
     * Upadate Profile Spinner Validation
     * @param context
     * @param sp_frg_profile_ft
     * @param spn_frg_profile_inch
     * @return
     */
    public static boolean spinnerValidation(Context context,Spinner sp_frg_profile_ft,
                                      Spinner spn_frg_profile_inch){
        if(!sp_frg_profile_ft.getSelectedItem().toString().equals("")){
            if(!spn_frg_profile_inch.getSelectedItem().toString().equals("")){
                return true;
            }else {
                //toastMessage("Please select inch");
                Toast.makeText(context,context.getString(R.string.toast_select_inch),Toast.LENGTH_SHORT).show();
                return  false;
            }
        }else {
            //toastMessage("Please select feet");
            Toast.makeText(context,context.getString(R.string.toast_select_feet),Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /* check input value is 0(ZERO)*/
    public boolean checkInputValidHeights(EditText editText) {
        String temp = editText.getText().toString().trim();
        if (temp != null && temp.length() > 0) {
            Float f = Float.parseFloat(temp);
            if (f > 273f) {
                editText.requestFocus();
                editText.setCursorVisible(true);
                editText.setError(context.getString(R.string.msg_your_height_should_be));
                return false;
            }
        }
        return true;
    }
}
