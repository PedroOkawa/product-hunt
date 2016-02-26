package com.okawa.pedro.producthunt.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.test.espresso.IdlingResource;

import com.okawa.pedro.producthunt.ui.loading.LoadingActivity;

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
        boolean isIdle = isLoadingActivityRunning();
        if (isIdle && callback != null) {
            callback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    private boolean isLoadingActivityRunning() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTasks = activityManager.getAppTasks();

        for(ActivityManager.AppTask appTask : appTasks) {
            if(appTask.getTaskInfo().baseActivity != null) {
                if (LoadingActivity.class.getPackage().getName().equalsIgnoreCase(appTask.getTaskInfo().baseActivity.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
