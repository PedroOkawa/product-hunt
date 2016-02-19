package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.ApiManager;

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
    private OkHttpClient providesOkHttpClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    public Retrofit provideApiInterface(OkHttpClient okHttpClient) {
        return new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(ApiInterface.BASE_URL)
                .build();
    }

    @Singleton
    @Provides
    public ApiManager providesApiManager() {
        return new ApiManager();
    }

}
