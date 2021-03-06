package com.okawa.pedro.producthunt.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.okawa.pedro.producthunt.ui.main.MainActivity;

import java.util.List;

/**
 * Created by pokawa on 25/02/16.
 */
public class LoadingIdlingResource implements IdlingResource {

    private Context context;
    private ResourceCallback callback;

    public LoadingIdlingResource(Context context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return LoadingIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = !isMainActivityRunning();
        if (isIdle && callback != null) {
            callback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    private boolean isMainActivityRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getClassName().equals(MainActivity.class.getName());
    }

}
