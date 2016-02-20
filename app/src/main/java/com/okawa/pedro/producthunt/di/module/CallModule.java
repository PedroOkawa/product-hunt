package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.util.manager.CallManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 20/02/16.
 */
@Module
public class CallModule {

    @Singleton
    @Provides
    public CallManager providesCallManager() {
        return new CallManager();
    }

}
