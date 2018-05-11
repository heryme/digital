package com.digitalscale.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.widget.Toast;

import com.digitalscale.tools.Session;
import com.digitalscale.utility.ValidationUtils;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Vishal Gadhiya on 3/7/2017.
 */

public class MasterActivity extends AppCompatActivity {

    public static final String TAG = MasterActivity.class.getSimpleName();

    public GoogleApiClient mGoogleApiClient;
    public ValidationUtils validationUtils = new ValidationUtils(MasterActivity.this);
    Session session = null;

    public MasterActivity() {
    }

    public void debugLog(String message) {
        Log.d(TAG, ">>>   " + message);
    }

    /* When app is run in only debug mode than print log otherwise not logged*/
    public void debugLog(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, ">>>   " + message);
    }

    public void toastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public Session getSession() {
        if (session == null)
            session = new Session(getApplicationContext());

        return session;
    }
}
