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

    public AppModule(ProductHuntApp productHuntApp) {
        this.productHuntApp = productHuntApp;
    }

    @Singleton
    @Provides
    public ProductHuntApp providesApp() {
        return productHuntApp;
    }

}
