package com.okawa.pedro.producthunt;

import com.okawa.pedro.producthunt.di.component.DaggerTestAppComponent;
import com.okawa.pedro.producthunt.di.module.AppModule;

/**
 * Created by pokawa on 24/02/16.
 */
public class ProductHuntAppTest extends ProductHuntApp {

    @Override
    protected void initializeComponent() {
        appComponent = DaggerTestAppComponent
                .builder()
                .appModule(new AppModule(this, true))
                .build();
    }
}
