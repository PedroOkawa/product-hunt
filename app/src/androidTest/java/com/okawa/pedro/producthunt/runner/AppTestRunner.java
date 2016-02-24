package com.okawa.pedro.producthunt.runner;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.okawa.pedro.producthunt.ProductHuntAppTest;

/**
 * Created by pokawa on 24/02/16.
 */
public class AppTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, ProductHuntAppTest.class.getName(), context);
    }
}
