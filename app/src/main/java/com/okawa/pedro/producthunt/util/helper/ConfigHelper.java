package com.okawa.pedro.producthunt.util.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.database.DatabaseRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import greendao.Category;

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
