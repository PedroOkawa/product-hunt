package com.okawa.pedro.producthunt.di.module;

import android.database.sqlite.SQLiteDatabase;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by pokawa on 19/02/16.
 */
@Module
public class DatabaseModule {

    public static final int SELECT_LIMIT = 20;

    private static final String DATABASE_NAME = "ProductHuntDB";

    @Singleton
    @Provides
    public DaoSession providesDaoSession(ProductHuntApp productHuntApp) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(productHuntApp, DATABASE_NAME, null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        return daoMaster.newSession();
    }

    @Singleton
    @Provides
    public DatabaseRepository providesDatabaseRepository(DaoSession daoSession, ConfigHelper configHelper) {
        return new DatabaseRepository(daoSession, configHelper);
    }

}
