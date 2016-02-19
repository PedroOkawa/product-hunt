package com.okawa.pedro.producthunt;

import android.app.Application;

import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerAppComponent;
import com.okawa.pedro.producthunt.di.module.AppModule;

/**
 * Created by pokawa on 19/02/16.
 */
public class ProductHuntApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent();
    }

    private void initializeComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
