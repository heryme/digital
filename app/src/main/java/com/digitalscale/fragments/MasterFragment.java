package com.digitalscale.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.widget.Toast;

import com.digitalscale.tools.Session;

/**
 * Created by Vishal Gadhiya on 2/27/2017.
 */

public class MasterFragment extends Fragment {
    private static final String TAG = MasterFragment.class.getSimpleName();

    Session session = null;

    public void debugLog(String message) {
        Log.d(TAG, message);
    }

    public void debugLog(String tag,String message) {
        if (BuildConfig.DEBUG)
        Log.d(tag, message);
    }

    public void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    public Session getSession() {
        if (session == null)
            session = new Session(getContext());

        return session;
    }
}
