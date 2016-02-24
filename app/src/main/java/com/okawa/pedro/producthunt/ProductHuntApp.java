package com.okawa.pedro.producthunt;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerAppComponent;
import com.okawa.pedro.producthunt.di.module.AppModule;
import io.fabric.sdk.android.Fabric;

/**
 * Created by pokawa on 19/02/16.
 */
public class ProductHuntApp extends Application {

    protected AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initializeComponent();
    }

    protected void initializeComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
