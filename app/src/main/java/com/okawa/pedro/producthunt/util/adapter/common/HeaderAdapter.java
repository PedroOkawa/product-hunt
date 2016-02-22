package com.okawa.pedro.producthunt.util.adapter.common;

import android.databinding.ViewDataBinding;

import java.util.List;

/**
 * Created by pokawa on 22/02/16.
 */
public abstract class HeaderAdapter<T, K extends ViewDataBinding> extends BindingAdapter<T, K> {

    public static final int VIEW_TYPE_HEADER = 0x0000;
    public static final int VIEW_TYPE_ITEM = 0x0001;

    public HeaderAdapter(List data) {
        super(data);
    }

    @Override
    public abstract int getItemViewType(int viewType);

    public abstract boolean isHeader(int position);
}
