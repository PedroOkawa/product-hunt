package com.okawa.pedro.producthunt.di.module;

import com.okawa.pedro.producthunt.di.scope.Activity;
import com.okawa.pedro.producthunt.presenter.filter.FilterPresenter;
import com.okawa.pedro.producthunt.presenter.filter.FilterPresenterImpl;
import com.okawa.pedro.producthunt.ui.filter.FilterView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by pokawa on 22/02/16.
 */
@Module
public class FilterModule {

    private FilterView filterView;

    public FilterModule(FilterView filterView) {
        this.filterView = filterView;
    }

    @Activity
    @Provides
    public FilterView providesFilterView() {
        return filterView;
    }

    @Activity
    @Provides
    public FilterPresenter providesFilterPresenter(FilterView filterView) {
        return new FilterPresenterImpl(filterView);
    }

}
