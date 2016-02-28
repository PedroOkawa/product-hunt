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

    private ProductHuntApp productHuntApp;
    private boolean isTest;

    public AppModule(ProductHuntApp productHuntApp, boolean isTest) {
        this.productHuntApp = productHuntApp;
        this.isTest = isTest;
    }

    @Singleton
    @Provides
    public ProductHuntApp providesApp() {
        return productHuntApp;
    }

    @Singleton
    @Provides
    public boolean providesIsTest() {
        return isTest;
    }

}
