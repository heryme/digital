package com.digitalscale.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.digitalscale.R;
import com.digitalscale.fragments.DiaryFragment_;
import com.digitalscale.fragments.HomeFragment;
import com.digitalscale.fragments.ProfileFragment;
import com.digitalscale.fragments.ProfileFragment_;
import com.digitalscale.utility.DateUtility;
import com.digitalscale.utility.FontUtility;
import com.digitalscale.utility.LoginWithGmailManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@EActivity(R.layout.activity_home)
public class MainActivity extends MasterActivity {

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.navigation_view)
    NavigationView navigationView;
    @ViewById(R.id.drawer)
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle actionBarDrawerToggle;

    LoginWithGmailManager loginWithGmailManager;

    /**
     * Title List
     */
    private ArrayList<String> titleList;

    @AfterViews
    public void init() {
        Log.d(TAG,"Load MainActivity");
        loginWithGmailManager = new LoginWithGmailManager(getApplicationContext());
        titleList = new ArrayList<>();
        setSupportActionBar(toolbar);

        //Display Back Arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Not Set By Default Title Ex:By Default Set App Name :Digital Scale
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Set Font Style Of  Navigation Drawer Items
        FontUtility.navigationView(MainActivity.this,navigationView);

        initNavigationDrawer();

        //When First Time Open The App Then Load Tab
        if (getIntent().getBooleanExtra("OPEN_DIARY_FM", false)) {
            toolbar.setTitle(getString(R.string.nav_menu_diary));
            loadFragment(new DiaryFragment_(), "fm_diary",false,getString(R.string.nav_menu_diary));
        } else
            loadFragment(HomeFragment.newInstance(), "fm_home",false,getString(R.string.nav_menu_home));

        setToolBar();

        Log.d(TAG,"Language---->" + Locale.getDefault().getDisplayLanguage());
    }

    /**
     * Set Up Toolbar
     */
    private void setToolBar() {
        final View.OnClickListener toolbarListener = actionBarDrawerToggle.getToolbarNavigationClickListener();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "On Back Entry On Back Arrow =" + getSupportFragmentManager().getBackStackEntryCount());
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                    actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragmentHandling();
                        }
                    });
                } else {
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    actionBarDrawerToggle.setToolbarNavigationClickListener(toolbarListener);
                }
            }
        });

    }

    /**
     * Set Up Navigation Drawer
     */
    public void initNavigationDrawer() {
        navigationView.setCheckedItem(R.id.home);
        navigationView.setItemTextColor(ContextCompat.getColorStateList(MainActivity.this, R.color.colorSky));
        navigationView.setItemIconTintList(null/*ContextCompat.getColorStateList(MainActivity.this, R.color.colorSky)*/);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                debugLog("getVisibleFragment() >> " + getVisibleFragment());

                switch (id) {
                    case R.id.home:
                        if (getVisibleFragment() != HomeFragment.newInstance())
                            loadFragment(HomeFragment.newInstance(), "fm_home",true,getString(R.string.nav_menu_home));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.profile:
                        ProfileFragment profileFragment = new ProfileFragment_();
                        Bundle args = new Bundle();
                        args.putBoolean("goal",false);
                        profileFragment.setArguments(args);
                        loadFragment(profileFragment, "fm_profile",true,getString(R.string.nav_menu_profile));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.diary:
                        SplashActivity.selectedDate = DateUtility.getCurrentDateIn_yyyy_mm_dd();
                        loadFragment(new DiaryFragment_(), "fm_diary",true,getString(R.string.nav_menu_diary));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.gole:
                        ProfileFragment profileFragment1 = new ProfileFragment_();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("goal",true);
                        profileFragment1.setArguments(bundle);
                        loadFragment(profileFragment1, "fm_profile",true,getString(R.string.lbl_goal));
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout:
                        appCloseAlertDialog(getString(R.string.nav_menu_logout));
                        break;
                }

                return true;
            }
        });

         actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    /**
     * Load Fragment
     * @param fragment
     * @param tag
     * @param isMenuItem
     * @param title
     */
    public void loadFragment(Fragment fragment, String tag, boolean isMenuItem,String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment f = getVisibleFragment();

        if(isMenuItem){
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0;i < fm.getBackStackEntryCount();i++){
                Log.d(TAG,"Back Stack Entry--->" + getFragmentManager().getBackStackEntryCount());
                //Clear Fragment Back Stack
                fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                titleList.clear();
            }
        }

        //fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        fragmentTransaction.add(R.id.content_frame, fragment, tag);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        //fragmentTransaction.commit();
        fragmentTransaction.commit();
        debugLog(TAG," Fragment Stack Count-->" + fragmentManager.getBackStackEntryCount());
        titleList.add(title);
        toolbar.setTitle(title);
        debugLog(TAG,"Toolbar Title -->" + toolbar.getTitle());
    }

    /**
     * User logout from all social media or normal login
     */
    private void logout() {
        googleLogOut();
         //googleLogout();
        //LoginActivity.googleLogout();
        Question1Activity.question1OptionID = "";
        Question2Activity.question2OptionID = "";
        //googleLogout();
        FbLogout();
        //twitterLogout();
        getSession().clearSession();
        //startActivity(new Intent(MainActivity.this,LoginActivity_.class));
        Intent intent = new Intent(MainActivity.this,LoginActivity_.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);// clear back stack
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();

    }

    /**
     * Fb Logout
     */
    private void FbLogout() {
        LoginManager.getInstance().logOut();
    }

    private void twitterLogout() {
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
    }

    /**
     * Check Fragment Visible Or Not
     * @return
     */
    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    debugLog("visible fragment >> " + fragment);
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 1){
            appCloseAlertDialog("close");
        } else {
            fragmentHandling();
        }
    }

    /**
     * Handle Fragment On Back Press
     */
    private void fragmentHandling() {
         //Fragment Remove From The Stack
         getSupportFragmentManager().popBackStack();
        //Size Decrease Of The Title List
        if (titleList.size() > 0)
            titleList.remove(titleList.size() - 1);

        //Set ToolBar Title According To Title List
        if (titleList.size() > 0)
            toolbar.setTitle(titleList.get(titleList.size() - 1));
    }

    /**
     *  App close alert dialog
     * @param msg
     */
    private void appCloseAlertDialog(final String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        if(msg.equals("close")){
            builder.setTitle(getString(R.string.dlg_title_app_close_alert_dialog));
            builder.setMessage(getString(R.string.dlg_msg_app_close_alert_dialog));
        }else {
            builder.setTitle(getString(R.string.dlg_title_app_logout_alert_dialog));
            builder.setMessage(getString(R.string.dlg_msg_app_logout_alert_dialog));
        }

        String positiveText = "Yes";/*getString(android.R.string.yes);*/
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        debugLog("Click");
                        if(msg.equals("close")){
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                            finishAffinity();
                        }else {
                            logout();
                        }

                    }
                });

        String negativeText = "No";/*getString(android.R.string.no)*/
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        if (dialog != null)
                            dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void googleLogOut() {
        try {
            Auth.GoogleSignInApi.signOut(loginWithGmailManager.mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            //Toast.makeText(MainActivity.this, "G+ loggedOut", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            debugLog("<<<<<<< google logout exception >>>>>>>> \n");
            e.printStackTrace();
        }

    }
}
