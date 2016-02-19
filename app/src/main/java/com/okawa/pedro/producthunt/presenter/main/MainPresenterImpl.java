package com.okawa.pedro.producthunt.presenter.main;

import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.ui.main.MainView;
import com.okawa.pedro.producthunt.util.manager.ApiManager;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;
    private ApiManager apiManager;

    private ActivityMainBinding binding;

    public MainPresenterImpl(MainView mainView, ApiManager apiManager) {
        this.mainView = mainView;
        this.apiManager = apiManager;
    }

    @Override
    public void initializeViews(ActivityMainBinding binding) {
        /* STORES BINDING */
        this.binding = binding;
    }

    @Override
    public void requestData() {

    }
}
