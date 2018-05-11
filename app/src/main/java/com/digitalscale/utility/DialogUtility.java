package com.digitalscale.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalscale.R;
import com.digitalscale.fragments.TabGoalFragment;

/**
 * Created by Vishal Gadhiya on 3/8/2017.
 */

public class DialogUtility {
    public static ProgressDialog showProgress(final Context context) {
       final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

            }
        });

        return progressDialog;
    }

    public static void hideProgress(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.hide();
    }


    /**
     * Show Pop up for height centimeter and foot
     * @param context
     * @param textView
     * @param imageView
     */
    public static void heightUnitSettingPopUp(Context context, final TextView textView, ImageView imageView) {
        PopupMenu popupMenu = new PopupMenu(context, imageView);

        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_basic_setting1_height, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_basic_setting1_cm:
                        textView.setText("cm");
                        break;
                    case R.id.menu_basic_setting1_ft:
                        textView.setText("ft");
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    /**
     * Show Pop Up For Weight Unit UNIT_KG And Lb(Pound)
     * @param context
     * @param textView
     * @param imageView
     */
    public static void weightUnitSettingPopUp(final Context context, final TextView textView, ImageView imageView) {
        PopupMenu popupMenu = new PopupMenu(context, imageView);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_basic_setting2_weight, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_basic_setting_weight_kg:
                        textView.setText(context.getString(R.string.lbl_kg));
                        FontUtility.condBold(textView,context);
                        //Set Tab GoalQuestion2 Unit
                        //TabGoalFragment.tabGoalQuestion2unit = textView.getText().toString();
                        break;
                    case R.id.menu_basic_setting_weight_lb:
                        FontUtility.condBold(textView,context);
                        textView.setText(context.getString(R.string.lbl_lb));
                        //Set Tab GoalQuestion2 Unit
                        //TabGoalFragment.tabGoalQuestion2unit = textView.getText().toString();
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }

    public static void dialogWithPositiveButton(final String msg,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setCancelable(false);
        String positiveText = "OK";/*getString(android.R.string.yes);*/
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        if (dialog != null)
                            dialog.cancel();
                    }
                });

            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();
        }



}
