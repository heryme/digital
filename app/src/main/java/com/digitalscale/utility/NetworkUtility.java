package com.digitalscale.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Vishal Gadhiya on 4/10/2017.
 */

public class NetworkUtility {

    /**
     * Check Internet Available or Not
     */
    public static boolean checkIsInternetConnectionAvailable(final Context context) {
        boolean flag = false;
        final ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        flag = conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

        if (flag)
            return true;
        else {
            Toast.makeText(context, "Sorry!!! Internet connection is not available", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
