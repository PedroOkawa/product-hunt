package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.MainPresenter;
import com.okawa.pedro.producthunt.presenter.MainPresenterImpl;
import com.okawa.pedro.producthunt.ui.main.MainView;

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
    public MainPresenter providesPresenter() {
        return new MainPresenterImpl(mainView);
    }

}
