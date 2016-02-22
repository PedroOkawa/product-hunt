package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.di.module.ApiModule;
import com.okawa.pedro.producthunt.di.module.AppModule;
import com.okawa.pedro.producthunt.di.module.CallModule;
import com.okawa.pedro.producthunt.di.module.ConfigModule;
import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.manager.ApiManager;
import com.okawa.pedro.producthunt.util.manager.CallManager;
import com.okawa.pedro.producthunt.util.receiver.NetworkReceiver;

import javax.inject.Singleton;

import dagger.Component;
import greendao.DaoSession;
import okhttp3.OkHttpClient;

/**
 * Created by pokawa on 19/02/16.
 */
@Singleton
@Component( modules = { AppModule.class, ApiModule.class, CallModule.class, ConfigModule.class, DatabaseModule.class } )
public interface AppComponent {

    void inject(ProductHuntApp productHuntApp);
    void inject(NetworkReceiver networkReceiver);

    /* APP */

    ProductHuntApp providesApp();

    /* API */

    OkHttpClient providesOkHttpClient();
    ApiInterface provideApiInterface();
    ApiManager providesApiManager();

    /* CALL MANAGER */

    CallManager providesCallManager();

    /* CONFIG HELPER */

    ConfigHelper providesConfigHelper();

    /* DATABASE */

    DaoSession providesDaoSession();
    DatabaseRepository providesDatabaseRepository();
}
