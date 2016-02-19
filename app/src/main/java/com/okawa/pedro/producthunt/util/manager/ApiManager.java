package com.okawa.pedro.producthunt.util.manager;

import android.content.Context;

import com.okawa.pedro.producthunt.database.SessionRepository;
import com.okawa.pedro.producthunt.network.ApiInterface;
import com.okawa.pedro.producthunt.util.listener.ApiListener;

import java.io.IOException;
import java.util.Properties;

import greendao.Session;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pokawa on 19/02/16.
 */
public class ApiManager {

    private static final String API_PROPERTIES = "api.properties";
    private static final String API_ID = "api_id";
    private static final String API_SECRET = "api_secret";
    private static final String ACCESS_GRANT_TYPE = "client_credentials";

    private ApiInterface apiInterface;
    private Context context;
    private SessionRepository sessionRepository;

    public ApiManager(Context context,
                      ApiInterface apiInterface,
                      SessionRepository sessionRepository) {
        this.context = context;
        this.apiInterface = apiInterface;
        this.sessionRepository = sessionRepository;
    }

    public void validateSession(final ApiListener apiListener) {
        Properties properties = new Properties();

        try {
            properties.load(context.getAssets().open(API_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
            apiListener.onError(e.getMessage());
            return;
        }

        String apiId = properties.getProperty(API_ID);
        String apiSecret = properties.getProperty(API_SECRET);

        apiInterface
                .grantAccess(apiId, apiSecret, ACCESS_GRANT_TYPE)
                .observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Session>() {
                    @Override
                    public void onCompleted() {
                        apiListener.onDataLoaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        apiListener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Session session) {
                        sessionRepository.updateSession(session);
                    }
                });
    }

}