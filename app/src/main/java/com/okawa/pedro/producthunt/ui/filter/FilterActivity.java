package com.okawa.pedro.producthunt.ui.filter;

import android.os.Bundle;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityFilterBinding;
import com.okawa.pedro.producthunt.di.component.AppComponent;
import com.okawa.pedro.producthunt.di.component.DaggerFilterComponent;
import com.okawa.pedro.producthunt.di.module.FilterModule;
import com.okawa.pedro.producthunt.presenter.filter.FilterPresenter;
import com.okawa.pedro.producthunt.ui.common.BaseActivity;

import javax.inject.Inject;

/**
 * Created by pokawa on 22/02/16.
 */
public class FilterActivity extends BaseActivity implements FilterView {

    private ActivityFilterBinding binding;

    @Inject
    FilterPresenter filterPresenter;

    @Override
    protected int layoutToInflate() {
        return R.layout.activity_filter;
    }

    @Override
    protected void initializeComponent(AppComponent appComponent) {
        DaggerFilterComponent
                .builder()
                .appComponent(appComponent)
                .filterModule(new FilterModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void doOnCreated(Bundle savedInstanceState) {

        /* DEFINE START TRANSITION ANIMATION */

        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

        /* BINDING */

        binding = (ActivityFilterBinding) getDataBinding();

        /* INITIALIZE VIEW */

        filterPresenter.initialize(binding);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /* DEFINE END TRANSITION ANIMATION */
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }
}
