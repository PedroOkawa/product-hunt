package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 20/02/16.
 */
@Module
public class ConfigModule {

    @Singleton
    @Provides
    public ConfigHelper providesConfigHelper() {
        return new ConfigHelper();
    }

}
