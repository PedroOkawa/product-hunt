package com.okawa.pedro.producthunt.util.helper;

import android.content.Context;
import android.content.res.Configuration;

import com.okawa.pedro.producthunt.R;

/**
 * Created by pokawa on 20/02/16.
 */
public class ConfigHelper {

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
