package com.digitalscale.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.NavigationView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Rahul Padaliya on 4/26/2017.
 */
public class FontUtility {

    public static void condBold(TextView textView, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_bold.ttf");
        textView.setTypeface(tf,Typeface.NORMAL);
    }

    public static void condLight(TextView textView, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_light.ttf");
        textView.setTypeface(tf,Typeface.NORMAL);
    }

    public static void condLightItalic(TextView textView, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_lightItalic.ttf");
        textView.setTypeface(tf,Typeface.NORMAL);
    }

    public static void condRegular(TextView textView, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "poiret_one_regular.ttf");
        textView.setTypeface(tf,Typeface.NORMAL);
    }

    public static void condLight(EditText editText, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_light.ttf");
        editText.setTypeface(tf,Typeface.NORMAL);
    }

    public static void condLight(Button button, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_light.ttf");
        button.setTypeface(tf,Typeface.NORMAL);
    }

    public static void condLight(RadioButton radioButton, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_light.ttf");
        radioButton.setTypeface(tf,Typeface.NORMAL);
    }


    public static void condLight(AutoCompleteTextView autoCompleteTextView, Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_light.ttf");
        autoCompleteTextView.setTypeface(tf,Typeface.NORMAL);
    }

    /**
     * Set Custom Font In Navigation Item
     * @param context
     * @param mi
     */
    public static void applyFontToMenuItem(Context context,MenuItem mi) {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "open_sans_cond_bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomFontNavigationDrawer("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    /**
     * Get NavigationView
     * @param context
     * @param navigationView
     */
    public  static void navigationView(Context context,NavigationView navigationView){
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    FontUtility.applyFontToMenuItem(context,subMenuItem);
                }
            }

            //the method we have create in activity
            FontUtility.applyFontToMenuItem(context,mi);
        }
    }

}
