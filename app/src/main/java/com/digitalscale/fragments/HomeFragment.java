package com.digitalscale.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitalscale.R;
import com.digitalscale.activity.AddFoodActivity_;
import com.digitalscale.activity.BluetoothDevicesActivity_;
import com.digitalscale.activity.MainActivity;
import com.digitalscale.tools.Constant;
import com.digitalscale.utility.BluetoothUtility;
import com.digitalscale.utility.FontUtility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Vishal Gadhiya on 3/16/2017.
 */

@EFragment(R.layout.fragment_home)
public class HomeFragment extends MasterFragment implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    @ViewById(R.id.materialup_tabs)
    TabLayout tabLayout;

    @ViewById(R.id.materialup_appbar)
    AppBarLayout appBarLayout;

    @ViewById(R.id.materialup_viewpager)
    ViewPager viewPager;

    @ViewById(R.id.materialup_profile_image)
    ImageView mProfileImage;

    @ViewById(R.id.iv_add_food)
    ImageView iv_add_food;

    private int mMaxScrollSize;

    public static Fragment newInstance() {
        return new HomeFragment_();
    }

    @AfterViews
    public void init() {
        appBarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appBarLayout.getTotalScrollRange();

        setUpTabs();

        setHasOptionsMenu(true);

        setProfilePic();

        //Click event
        clickEventProfileImage();
    }

    /**
     * Click Event Of mProfileImage
     */
    private void clickEventProfileImage(){
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment_();
                Bundle args = new Bundle();
                args.putBoolean("goal",false);
                profileFragment.setArguments(args);
                ((MainActivity)getContext()).loadFragment(profileFragment, "fm_profile",true,getString(R.string.nav_menu_profile));
            }
        });
    }

    /**
     * Set Profile Picture
     */
    private void setProfilePic() {

        debugLog("getSession().getProfilePic() >> " + getSession().getProfilePic());
        if (getSession().getProfilePic().length() > 0) {
            Glide.with(getActivity())
                    .load(getSession().getProfilePic())
                    .error(R.drawable.ic_user_placeholder)
                    .into(mProfileImage);
        }else if(getSession().getBasicDetailGender().equalsIgnoreCase("Female")){
                Glide.with(getActivity())
                    .load(R.drawable.ic_female)
                    .error(R.drawable.ic_user_placeholder)
                    .into(mProfileImage);
        }else {
            Glide.with(getActivity())
                    .load(R.drawable.ic_male)
                    .error(R.drawable.ic_user_placeholder)
                    .into(mProfileImage);
        }
    }

    /* Click event */
    @Click
    public void iv_add_food() {
        if (BluetoothUtility.checkBluetoothCompatibility())
        createAddFoodDialog(getContext());
        else
            toastMessage(getString(R.string.toast_msg_phone_does_not_support_bt));
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    /**
     * Tabs Adapter
     */
    class TabsAdapter extends FragmentPagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return GoalActivityFragment.newInstance();
                case 1:
                    return TipsFragment.newInstance();
            }
            return null;
        }
    }

    /**
     * Open Add Food Dialog
     * @param context
     */
    public static void createAddFoodDialog(final Context context) {
        final Dialog dlgAddFood = new Dialog(context);
        dlgAddFood.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgAddFood.setContentView(R.layout.dialog_add_food);

        TextView tv_dialog_add_food_msg = (TextView) dlgAddFood.findViewById(R.id.tv_dialog_add_food_msg);
        FontUtility.condLight(tv_dialog_add_food_msg,context);

        //Set TextView Font Style
        TextView tvDialogAddFoodBreakfast = (TextView) dlgAddFood.findViewById(R.id.tvDialogAddFoodBreakfast);
        FontUtility.condLight(tvDialogAddFoodBreakfast,context);
        TextView tvDialogAddFoodLunch = (TextView) dlgAddFood.findViewById(R.id.tvDialogAddFoodLunch);
        FontUtility.condLight(tvDialogAddFoodLunch,context);
        TextView tvDialogAddFoodSnacks = (TextView) dlgAddFood.findViewById(R.id.tvDialogAddFoodSnacks);
        FontUtility.condLight(tvDialogAddFoodSnacks,context);
        TextView tvDialogAddFoodDinner = (TextView) dlgAddFood.findViewById(R.id.tvDialogAddFoodDinner);
        FontUtility.condLight(tvDialogAddFoodDinner,context);

        RelativeLayout rlBreakfast = (RelativeLayout) dlgAddFood.findViewById(R.id.rlDialogAddFoodBreakfast);
        RelativeLayout rlDinner = (RelativeLayout) dlgAddFood.findViewById(R.id.rlDialogAddFoodDinner);
        RelativeLayout rlLunch = (RelativeLayout) dlgAddFood.findViewById(R.id.rlDialogAddFoodLunch);
        RelativeLayout rlSnacks = (RelativeLayout) dlgAddFood.findViewById(R.id.rlDialogAddFoodSnacks);

        rlBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddFoodActivity_.class).putExtra("food_type", Constant.BREAKFAST));
                dlgAddFood.dismiss();
            }
        });

        rlDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddFoodActivity_.class).putExtra("food_type", Constant.DINNER));
                dlgAddFood.dismiss();
            }
        });

        rlLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddFoodActivity_.class).putExtra("food_type", Constant.LUNCH));
                dlgAddFood.dismiss();
            }
        });

        rlSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, AddFoodActivity_.class).putExtra("food_type", Constant.SNACKS));
                dlgAddFood.dismiss();
            }
        });
        dlgAddFood.show();
    }

    /**
     * Set Up Tab
     */
    private void setUpTabs() {

        //Set up tabs titles
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lbl_goal_activity)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.lbl_tips)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));
        debugLog("TAG", "DiaryFragment.selectedTabPosition >> " + DiaryFragment.selectedTabPosition);
        viewPager.setCurrentItem(DiaryFragment.selectedTabPosition);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.setCurrentItem(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bluetooth:
                if (BluetoothUtility.checkBluetoothCompatibility())
                    startActivity(new Intent(getContext(), BluetoothDevicesActivity_.class));
                else
                    toastMessage(getString(R.string.toast_msg_phone_does_not_support_bt));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
