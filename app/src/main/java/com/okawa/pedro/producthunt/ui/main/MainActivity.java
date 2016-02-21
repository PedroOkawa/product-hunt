package com.okawa.pedro.producthunt.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerMainComponent;
import com.okawa.pedro.producthunt.di.module.MainModule;
import com.okawa.pedro.producthunt.presenter.main.MainPresenter;
import com.okawa.pedro.producthunt.ui.common.BaseActivity;

import javax.inject.Inject;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainActivity extends BaseActivity implements MainView {

    private ActivityMainBinding binding;

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
        binding = (ActivityMainBinding) getDataBinding();

        mainPresenter.initialize(binding);
    }

    @Override
    public void initializeToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onRequest() {
        binding.srlActivityMainPosts.setRefreshing(true);
    }

    @Override
    public void onComplete() {
        binding.pbActivityMain.hide();
        binding.srlActivityMainPosts.setRefreshing(false);
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        binding.pbActivityMain.hide();
        binding.srlActivityMainPosts.setRefreshing(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mainPresenter.updateGridLayoutSpan();
    }
}
