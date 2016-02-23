package com.okawa.pedro.producthunt.presenter.filter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.okawa.pedro.producthunt.R;
import com.okawa.pedro.producthunt.databinding.ActivityFilterBinding;
import com.okawa.pedro.producthunt.ui.filter.FilterView;

/**
 * Created by pokawa on 22/02/16.
 */
public class FilterPresenterImpl implements FilterPresenter {

    private FilterView filterView;

    private ActivityFilterBinding binding;
    private Context context;

    public FilterPresenterImpl(FilterView filterView) {
        this.filterView = filterView;
    }

    @Override
    public void initialize(ActivityFilterBinding binding) {

        /* STORES BINDING */

        this.binding = binding;

        /* CONTEXT */

        context = binding.getRoot().getContext();
    }
}
