package com.okawa.pedro.producthunt.util.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.okawa.pedro.producthunt.ui.main.MainActivity;

/**
 * Created by pokawa on 19/02/16.
 */
public class CallManager {

    public void main(AppCompatActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

}
