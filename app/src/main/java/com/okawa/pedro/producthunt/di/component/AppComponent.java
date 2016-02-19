package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.database.CategoryRepository;
import com.okawa.pedro.producthunt.database.SessionRepository;
import com.okawa.pedro.producthunt.di.module.ApiModule;
import com.okawa.pedro.producthunt.di.module.AppModule;
import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import javax.inject.Singleton;

import dagger.Component;
import greendao.DaoSession;
import okhttp3.OkHttpClient;

/**
 * Created by pokawa on 19/02/16.
 */
@Singleton
@Component( modules = { AppModule.class, ApiModule.class, DatabaseModule.class } )
public interface AppComponent {

    void inject(ProductHuntApp productHuntApp);

    /* APP */

    ProductHuntApp providesApp();

    /* API */

    OkHttpClient providesOkHttpClient();
    ApiInterface provideApiInterface();
    ApiManager providesApiManager();

    /* DATABASE */

    DaoSession providesDaoSession();
    CategoryRepository providesCategoryRepository();
    SessionRepository providesSessionRepository();
}
