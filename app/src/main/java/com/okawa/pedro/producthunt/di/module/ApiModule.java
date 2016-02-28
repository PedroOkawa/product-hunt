package com.okawa.pedro.producthunt.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.database.DatabaseRepository;
import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pokawa on 19/02/16.
 */
@Module
public class ApiModule {

    private static final long CONNECTION_TIMEOUT = 60;

    @Singleton
    @Provides
    public Gson providesGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSzzz").create();
    }

    @Singleton
    @Provides
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    public ApiInterface provideApiInterface(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(ApiInterface.BASE_URL)
                .build()
                .create(ApiInterface.class);
    }

    @Singleton
    @Provides
    public ApiManager providesApiManager(boolean isTest,
                                         Gson gson,
                                         ProductHuntApp productHuntApp,
                                         ApiInterface apiInterface,
                                         ConfigHelper configHelper,
                                         DatabaseRepository databaseRepository) {
        return new ApiManager(isTest, gson, productHuntApp, apiInterface, configHelper, databaseRepository);
    }

}
