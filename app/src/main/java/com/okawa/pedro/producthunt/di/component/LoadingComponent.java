package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.di.module.LoadingModule;
import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.loading.LoadingPresenter;
import com.okawa.pedro.producthunt.ui.loading.LoadingActivity;
import com.okawa.pedro.producthunt.ui.loading.LoadingView;

import dagger.Component;

/**
 * Created by pokawa on 19/02/16.
 */
@Activity
@Component( dependencies = AppComponent.class, modules = LoadingModule.class )
public interface LoadingComponent {

    void inject(LoadingActivity loadingActivity);

    /* LOADING */

    LoadingView providesLoadingView();
    LoadingPresenter providesLoadingPresenter();
}
