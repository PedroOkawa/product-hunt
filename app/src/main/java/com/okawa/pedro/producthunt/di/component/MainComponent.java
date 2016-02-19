package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.di.module.MainModule;
import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.main.MainPresenter;
import com.okawa.pedro.producthunt.ui.main.MainActivity;
import com.okawa.pedro.producthunt.ui.main.MainView;

import dagger.Component;

/**
 * Created by pokawa on 19/02/16.
 */
@Activity
@Component( dependencies = AppComponent.class, modules = MainModule.class )
public interface MainComponent {

    void inject(MainActivity mainActivity);

    /* MAIN */

    MainView providesMainView();
    MainPresenter providesMainPresenter();
}
