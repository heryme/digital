package com.digitalscale.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.digitalscale.constant.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rahul Padaliya on 4/3/2017.
 */
public class AndroidUtility {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    /**
     * Set By Default Selection For Radio button
     *
     * @param radioGroup
     */
    public static void selectFirstVisibleRadioButton(RadioGroup radioGroup) {

        int childCount = radioGroup.getChildCount();

        for (int i = 0; i < childCount; i++) {
            RadioButton rButton = (RadioButton) radioGroup.getChildAt(i);

            if (rButton.getVisibility() == View.VISIBLE) {
                rButton.setChecked(true);
                return;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    /**
     * Create Image directory
     *
     * @return
     */
    public static File getOutputMediaFile() {

        File file = new File(Constant.APP_DIR + Constant.SUB_DIR + Constant.IMAGES_DIR);
        if (!file.exists())
            file.mkdirs();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        return new File(file.getPath() + File.separator + formatter.format(now) + ".jpg");
    }

    /**
     * Convert Centimeter
     *
     * @param str
     * @return
     * @throws NumberFormatException
     */
    public static String convertCmTofeetInches(String str) throws NumberFormatException {

        if (!str.equals("")) {
            Double value = new Double(str);
            int feet = (int) Math.floor(value / 30.48);
            int inches = (int) Math.round((value / 2.54) - ((int) feet * 12));
            String ouput = feet + "' " + inches + "\"";
            Log.d("TAG", "Converted Feet And Inch" + ouput);
            String feetData = String.valueOf(feet);
            String InchData = String.valueOf(inches);
            return feetData + "." + InchData;
        }
        return null;
    }

    public static String feetToCentimeter(String feet) {
        if (feet != null && feet.length() > 0) {
            double dCentimeter = 0d;

        /*String[] parts = feet.split("\\.");
        String Feet = parts[0];
        String Inch = parts[1];*/

            dCentimeter += Math.round((Double.valueOf(feet)) * 30.48);
            //dCentimeter += ((Double.valueOf(Inch))*2.54);
            Log.d("TAG", "Feet" + dCentimeter);
            return String.valueOf(dCentimeter).replace("."," ");
        }
        return null;
    }

    public static String ftToCm(String ft) {

        return String.valueOf(Double.valueOf(ft) / 0.032808);
    }

    public static String cmToFt(String cm) {
        return String.valueOf(Double.valueOf(cm) * 0.032808);
    }

    /**
     * Hide Keyboard
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
