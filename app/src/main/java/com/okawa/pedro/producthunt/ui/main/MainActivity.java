package com.okawa.pedro.producthunt.ui.main;

import android.os.Bundle;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerMainComponent;
import com.okawa.pedro.producthunt.di.module.MainModule;
import com.okawa.pedro.producthunt.presenter.MainPresenter;
import com.okawa.pedro.producthunt.ui.common.BaseActivity;

import javax.inject.Inject;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainActivity extends BaseActivity implements MainView {

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected int layoutToInflate() {
        return R.layout.activity_main;
    }

    @Override
    protected void initializeComponent(AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void doOnCreated(Bundle savedInstanceState) {
        mainPresenter.initializeViews((ActivityMainBinding) getDataBinding());
    }

    @Override
    public void onLoadData() {

    }

    @Override
    public void onDataLoaded() {

    }

    @Override
    public void onError(String error) {

    }
}
