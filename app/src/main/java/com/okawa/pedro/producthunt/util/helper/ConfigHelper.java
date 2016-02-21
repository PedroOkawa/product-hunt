package com.okawa.pedro.producthunt.util.helper;

import android.content.Context;
import android.content.res.Configuration;

import com.okawa.pedro.producthunt.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pokawa on 20/02/16.
 */
public class ConfigHelper {

    private int daysAgo = 0;

    /* DATE FORMAT */

    public String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public void resetDaysAgo() {
        daysAgo = 0;
    }

    public String getDaysAgo() {
        return String.valueOf(daysAgo++);
    }

    public boolean checkIsToday(Date date) {
        return removeTime(new Date()).compareTo(removeTime(date)) == 0;
    }

    private Date removeTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /* LAYOUT MANAGER */

    public int defineSpanCount(Context context) {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        int orientation = context.getResources().getConfiguration().orientation;

        if(isTablet) {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                return 2;
            }
            return 3;
        } else {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                return 1;
            }
            return 2;
        }
    }

}
