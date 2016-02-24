package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.ProductHuntApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 19/02/16.
 */
@Module
public class AppModule {

    private boolean runningTest;
    private ProductHuntApp productHuntApp;

    public AppModule(boolean runningTest, ProductHuntApp productHuntApp) {
        this.runningTest = runningTest;
        this.productHuntApp = productHuntApp;
    }

    @Singleton
    @Provides
    public boolean providesIsRunningTest() {
        return runningTest;
    }

    @Singleton
    @Provides
    public ProductHuntApp providesApp() {
        return productHuntApp;
    }

}
