package com.okawa.pedro.producthunt.di.component;

import com.okawa.pedro.producthunt.di.module.FilterModule;
import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.filter.FilterPresenter;
import com.okawa.pedro.producthunt.ui.filter.FilterActivity;
import com.okawa.pedro.producthunt.ui.filter.FilterView;

import dagger.Component;

/**
 * Created by pokawa on 22/02/16.
 */
@Activity
@Component( dependencies = AppComponent.class, modules = FilterModule.class )
public interface FilterComponent {

    void inject(FilterActivity filterActivity);

    /* FILTER */

    FilterView providesFilterView();
    FilterPresenter providesFilterPresenter();

}
