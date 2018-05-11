package com.digitalscale.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.digitalscale.fragments.ChangePasswordFragment_;
import com.digitalscale.fragments.GoalActivityFragment;
import com.digitalscale.fragments.TabGoalFragment;
import com.digitalscale.fragments.TabGoalFragment_;
import com.digitalscale.fragments.TipsFragment;
import com.digitalscale.fragments.UpdateProfileFragment_;

/**
 * Created by Rahul Padaliya on 5/13/2017.
 */
public class ProfileTabsAdapter extends FragmentPagerAdapter {

    public ProfileTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UpdateProfileFragment_();
            case 1:
                return new ChangePasswordFragment_();
            case 2:
                return new TabGoalFragment_();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
