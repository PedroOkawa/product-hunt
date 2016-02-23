package com.okawa.pedro.producthunt.presenter.main;

import com.okawa.pedro.producthunt.databinding.ActivityMainBinding;

/**
 * Created by pokawa on 19/02/16.
 */
public interface MainPresenter {

    void initialize(ActivityMainBinding binding);
    void openDatePicker();
    void dispose();

}
