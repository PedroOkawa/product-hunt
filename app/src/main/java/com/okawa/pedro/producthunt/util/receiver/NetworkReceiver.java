package com.okawa.pedro.producthunt.util.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.model.event.ConnectionEvent;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by pokawa on 22/02/16.
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Inject
    ConfigHelper configHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((ProductHuntApp)context.getApplicationContext()).getAppComponent().inject(this);

        if(configHelper.isConnected(context)) {
            EventBus.getDefault().postSticky(new ConnectionEvent());
        }

    }
}
