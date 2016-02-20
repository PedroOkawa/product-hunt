package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.database.CategoryRepository;
import com.okawa.pedro.producthunt.database.PostRepository;
import com.okawa.pedro.producthunt.database.SessionRepository;
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
    public OkHttpClient providesOkHttpClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Singleton
    @Provides
    public ApiInterface provideApiInterface(OkHttpClient okHttpClient) {
        return new Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(ApiInterface.BASE_URL)
                .build()
                .create(ApiInterface.class);
    }

    @Singleton
    @Provides
    public ApiManager providesApiManager(ProductHuntApp productHuntApp,
                                         ApiInterface apiInterface,
                                         ConfigHelper configHelper,
                                         CategoryRepository categoryRepository,
                                         PostRepository postRepository,
                                         SessionRepository sessionRepository) {
        return new ApiManager(productHuntApp, apiInterface, configHelper, categoryRepository, postRepository, sessionRepository);
    }

}
