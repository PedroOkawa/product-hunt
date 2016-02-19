package com.okawa.pedro.producthunt.ui.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.okawa.pedro.producthunt.ProductHuntApp;
import com.okawa.pedro.producthunt.di.component.AppComponent;

/**
 * Created by pokawa on 19/02/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ViewDataBinding dataBinding;

    protected abstract @LayoutRes int layoutToInflate();
    protected abstract void initializeComponent(AppComponent appComponent);
    protected abstract void doOnCreated(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDataBinding();
        initializeComponent(((ProductHuntApp) getApplication()).getAppComponent());
        doOnCreated(savedInstanceState);
    }

    private void initializeDataBinding() {
        dataBinding = DataBindingUtil.setContentView(this, layoutToInflate());
    }

    protected ViewDataBinding getDataBinding() {
        return dataBinding;
    }
}
