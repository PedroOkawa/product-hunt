package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.di.module.ApiModule;
import com.okawa.pedro.producthunt.di.module.AppModule;
import com.okawa.pedro.producthunt.di.module.CallModule;
import com.okawa.pedro.producthunt.di.module.ConfigModule;
import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.suite.MainActivityTest;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by pokawa on 23/02/16.
 */
@Singleton
@Component( modules = { AppModule.class, ApiModule.class, CallModule.class, ConfigModule.class, DatabaseModule.class } )
public interface TestAppComponent extends AppComponent {

    void inject(MainActivityTest mainActivityTest);

}
