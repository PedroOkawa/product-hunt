package com.okawa.pedro.producthunt.ui.loading;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityLoadingBinding;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerLoadingComponent;
import com.okawa.pedro.producthunt.di.module.LoadingModule;
import com.okawa.pedro.producthunt.presenter.loading.LoadingPresenter;
import com.okawa.pedro.producthunt.ui.common.BaseActivity;
import com.okawa.pedro.producthunt.util.manager.CallManager;

import javax.inject.Inject;

/**
 * Created by pokawa on 19/02/16.
 */
public class LoadingActivity extends BaseActivity implements LoadingView {

    private ActivityLoadingBinding binding;

    @Inject
    CallManager callManager;

    @Inject
    LoadingPresenter loadingPresenter;

    @Override
    protected int layoutToInflate() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initializeComponent(AppComponent appComponent) {
        DaggerLoadingComponent
                .builder()
                .appComponent(appComponent)
                .loadingModule(new LoadingModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void doOnCreated(Bundle savedInstanceState) {
        binding = (ActivityLoadingBinding) getDataBinding();

        loadingPresenter.validateToken();
    }

    @Override
    public void onRequest() {
        binding.pbActivityLoading.show();
    }

    @Override
    public void onComplete() {
        binding.pbActivityLoading.hide();
        callManager.main(this);
    }

    @Override
    public void onError(String error) {
        binding.pbActivityLoading.hide();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        finish();
    }
}
