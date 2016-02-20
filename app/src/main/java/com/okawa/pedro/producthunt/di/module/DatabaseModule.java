package com.okawa.pedro.producthunt.di.module;

import android.database.sqlite.SQLiteDatabase;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.database.CategoryRepository;
import com.okawa.pedro.producthunt.database.PostRepository;
import com.okawa.pedro.producthunt.database.SessionRepository;

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
    public CategoryRepository providesCategoryRepository(DaoSession daoSession) {
        return new CategoryRepository(daoSession);
    }

    @Singleton
    @Provides
    public PostRepository providesPostRepository(DaoSession daoSession) {
        return new PostRepository(daoSession);
    }

    @Singleton
    @Provides
    public SessionRepository providesSessionRepository(DaoSession daoSession) {
        return new SessionRepository(daoSession);
    }

}
