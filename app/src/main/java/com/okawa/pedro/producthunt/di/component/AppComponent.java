package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pokawa on 19/02/16.
 */
@Singleton
@Component( modules = { AppModule.class } )
public interface AppComponent {

    void inject(ProductHuntApp productHuntApp);

    ProductHuntApp providesApp();
}
