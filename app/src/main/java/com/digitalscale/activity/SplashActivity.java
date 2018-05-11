 package com.digitalscale.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Window;

import com.digitalscale.R;
import com.digitalscale.utility.DateUtility;
import com.digitalscale.utility.SocialLoginUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.WindowFeature;

@Fullscreen
@EActivity(R.layout.activity_splash)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class SplashActivity extends MasterActivity {

    private static final int SPLASH_INTERVAL = 2000; // 2 sec

    public static String selectedDate = null;

    @AfterViews
    public void init() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        } else {
            selectedDate = DateUtility.getCurrentDateIn_yyyy_mm_dd();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SocialLoginUtil.generateHashKey(SplashActivity.this);
                        Thread.sleep(SPLASH_INTERVAL);
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                redirectTo();
                            } else {
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        } else {
                            redirectTo();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /* Redirect on main screen if user already login,
     * Here check user data is available in shared preferences?
     * if yes then user is Logged In otherwise not
     * */
    private void redirectTo() {
      /*  if (!getSession().getIsUserVerifyEmailId()) {*/
            if (getSession().getUserId().trim().length() > 0 && !getSession().getIsUserFillBasicDetails()) {
                startActivity(new Intent(SplashActivity.this, Question1Activity_.class));
            } else if (getSession().getUserId().trim().length() > 0) {
                startActivity(new Intent(SplashActivity.this, MainActivity_.class));
            } else {
                if (getSession().getIsUserVerifyEmailId()) {
                    startActivity(new Intent(SplashActivity.this, EmailVerificationActivity_.class));
                }else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity_.class));
                }
            }
       /* }else {
            startActivity(new Intent(SplashActivity.this, EmailVerificationActivity_.class));
        }*/
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                redirectTo();
        } else {
            toastMessage(getString(R.string.toast_msg_Permission_denied_to_your_location));
        }
        return;
    }
}
