package com.digitalscale.utility;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.digitalscale.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vishal Gadhiya on 4/3/2017.
 */

public class DateUtility {

    private static final String TAG = DateUtility.class.getSimpleName();

    /**
     * Get Current Date
     * @return
     */
    public static String getCurrentDateIn_yyyy_mm_dd() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Log.d(TAG, "Today formatted Date >> " + formattedDate);
        return formattedDate;
    }

    /**
     * Get Next or Previous date of particular date
     * For Next date : 1
     * For Previous date : 0
     */
    public static String getNextOrPreviousDateOf(String date_yyyy_MM_dd, int nextOrPrevious) {
        String formattedDate = null;
        try {
            String sDate = date_yyyy_MM_dd;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(sDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            if (nextOrPrevious == 1)
                c.add(Calendar.DATE, 1);
            else
                c.add(Calendar.DATE, -1);

            formattedDate = dateFormat.format(c.getTime());
            Log.d(TAG, " Next or previous date of " + date_yyyy_MM_dd + "  >> " + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getPrevious15DayDateFromToday() {
        String previousDayDateFromToday = null;
        int previousDay = -15;
        try {
            String cDate = getCurrentDateIn_yyyy_mm_dd();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(cDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, previousDay);
            previousDayDateFromToday = dateFormat.format(c.getTime());
            Log.d(TAG, " previous 15 daY date from today (" + cDate + ")  >> " + previousDayDateFromToday);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return previousDayDateFromToday;
    }

    private static Date convertStringToDate(String dateInString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            date = format.parse(dateInString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isDate15DaysPrevious(String date) {
        Date fifineDaysPreviousDate = convertStringToDate(getPrevious15DayDateFromToday());
        Date otherDate = convertStringToDate(date);

        if (fifineDaysPreviousDate != null && otherDate != null) {
            return fifineDaysPreviousDate.after(otherDate);
        } else
            return false;
    }



}
