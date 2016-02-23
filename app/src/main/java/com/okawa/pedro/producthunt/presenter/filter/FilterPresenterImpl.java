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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.filter_activity_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spActivityFilterSort.setAdapter(adapter);
    }
}
