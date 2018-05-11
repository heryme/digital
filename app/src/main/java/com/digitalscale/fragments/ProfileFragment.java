package com.digitalscale.fragments;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.digitalscale.R;
import com.digitalscale.adapter.ProfileTabsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Vishal Gadhiya on 3/15/2017.
 */

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends MasterFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    @ViewById(R.id.materialup_tabs)
    TabLayout tabLayout;

    @ViewById(R.id.materialup_viewpager)
    ViewPager viewPager;

    Boolean value = false;

    @AfterViews
    public void init() {
        if(getArguments().getBoolean("goal")){
            value = getArguments().getBoolean("goal");
            debugLog("Profile Fragment-->" + value);
        }
        setUpTabs();
    }

    /**
     * Set Up Tab
     */
    private void setUpTabs() {
        //Set up tabs titles
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.nav_menu_profile)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.btn_change_pwd)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lbl_goal)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setAdapter(new ProfileTabsAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3);

        if(value){
             viewPager.setCurrentItem(2);
        }else {
            viewPager.setCurrentItem(0);
        }

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                debugLog(TAG,""+viewPager.getCurrentItem());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}

