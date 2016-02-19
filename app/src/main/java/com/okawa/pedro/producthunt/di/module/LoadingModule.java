package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.database.SessionRepository;
import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.loading.LoadingPresenter;
import com.okawa.pedro.producthunt.presenter.loading.LoadingPresenterImpl;
import com.okawa.pedro.producthunt.ui.loading.LoadingView;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 19/02/16.
 */
@Module
public class LoadingModule {

    private LoadingView loadingView;

    public LoadingModule(LoadingView loadingView) {
        this.loadingView = loadingView;
    }

    @Activity
    @Provides
    public LoadingView providesLoadingView() {
        return loadingView;
    }

    @Activity
    @Provides
    public LoadingPresenter providesLoadingPresenter(LoadingView loadingView,
                                                     ApiManager apiManager,
                                                     SessionRepository sessionRepository) {
        return new LoadingPresenterImpl(loadingView, apiManager, sessionRepository);
    }

}
