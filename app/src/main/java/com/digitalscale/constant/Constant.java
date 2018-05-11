package com.digitalscale.constant;

import android.os.Environment;

/**
 * Created by Rahul Padaliya on 5/15/2017.
 */
public class Constant {

    public static final String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String SUB_DIR = "/Weight Plate/";
    public static final String IMAGES_DIR = "Images/";

    public static final String PLACES_DETAIL_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_DETAILS = "/details";
    public static final String OUT_JSON = "/json";
    public static final String API_KEY = "AIzaSyCh82y4lvlivJWcf19aVDXBvP0CfOFcXSY";

    /**
     * Height : Feet Spinner Item Array
     */
    public static final String[] spinnerItemFtArray = {
            "1", "2",
            "3", "4",
            "5", "6",
            "7", "8" };


    /**
     * Height : Inch Spinner Item Array
     */
    public static final String[] spinnerItemInArray = {
            "0", "1", "2",
            "3", "4", "5", "6",
            "7", "8","9","10","11"};


    /**
     * Formula Utility
     */
    public static final String OZ = "oz";
    public static final String LB = "lb";
    public static final String KG = "kg";
    public static final String GM = "gm";
    public static final String ML = "ml";

}
