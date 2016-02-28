package com.okawa.pedro.producthunt.util.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.okawa.pedro.producthunt.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pokawa on 20/02/16.
 */
public class ConfigHelper {

    public static final String CONNECTION_ERROR = "It seems that you not online";

    /* CONNECTION */

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null);
    }

    /* LAYOUT MANAGER */

    public int defineSpanItem(Context context) {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        int orientation = context.getResources().getConfiguration().orientation;

        if(isTablet) {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                return 3;
            }
            return 2;
        } else {
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                return 2;
            }
            return 1;
        }
    }

    public int defineSpanCount(Context context) {
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);

        if(isTablet) {
            return 6;
        } else {
            return 2;
        }
    }

    /* DATE */

    public String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public Date convertStringToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDateString(Context context, Date date) {
        if(checkIsToday(date)) {
            return context.getString(R.string.main_activity_today);
        } else {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
    }

    public boolean checkSameDate(Date dateA, Date dateB) {
        return removeTime(dateA).compareTo(removeTime(dateB)) == 0;
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

    /* ASSETS */

    public BufferedReader getJsonFromAssets(Context context, String fileName) {
        BufferedReader buffered = null;
        try {
            InputStream inputStream = context.getResources().getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            buffered = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
            Log.wtf("TEST", "WTF: " + e.getMessage());
            return null;
        }
        return buffered;
    }

}
