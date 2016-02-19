package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.main.MainPresenter;
import com.okawa.pedro.producthunt.presenter.main.MainPresenterImpl;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 19/02/16.
 */
@Module
public class MainModule {

    private MainView mainView;

    public MainModule(MainView mainView) {
        this.mainView = mainView;
    }

    @Activity
    @Provides
    public MainView providesMainView() {
        return mainView;
    }

    @Activity
    @Provides
    public MainPresenter providesPresenter(MainView mainView,
                                           ApiManager apiManager) {
        return new MainPresenterImpl(mainView, apiManager);
    }

}
