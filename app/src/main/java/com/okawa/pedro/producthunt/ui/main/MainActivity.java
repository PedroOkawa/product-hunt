package com.okawa.pedro.producthunt.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerMainComponent;
import com.okawa.pedro.producthunt.di.module.MainModule;
import com.okawa.pedro.producthunt.presenter.main.MainPresenter;
import com.okawa.pedro.producthunt.ui.common.BaseActivity;
import com.okawa.pedro.producthunt.util.manager.CallManager;

import javax.inject.Inject;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainActivity extends BaseActivity implements MainView {

    private ActivityMainBinding binding;

    @Inject
    MainPresenter mainPresenter;

    @Inject
    CallManager callManager;

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
    public void initializeToolbar(String title) {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setToolbarName(String title) {
        getSupportActionBar().setTitle(title);
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
    public void openPostDetails(long postId, View view) {
        callManager.postDetails(this, postId, view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.dispose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            binding.dlActivityMain.openDrawer(GravityCompat.START);
            return true;
        }

        if(item.getItemId() == R.id.mainMenuFilter) {
            callManager.filter(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CallManager.FILTER_RESULT) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }
}
