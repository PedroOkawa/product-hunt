package com.okawa.pedro.producthunt.presenter;

import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;
import com.okawa.pedro.producthunt.ui.main.MainView;

/**
 * Created by pokawa on 19/02/16.
 */
public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;
    private ActivityMainBinding binding;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void initializeViews(ActivityMainBinding binding) {
        /* STORES BINDING */
        this.binding = binding;
    }
}
