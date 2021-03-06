package com.okawa.pedro.producthunt.util.adapter.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by pokawa on 20/02/16.
 */
public abstract class BindingAdapter<T, K extends ViewDataBinding> extends BaseAdapter<T> {

    public BindingAdapter(List<T> data) {
        super(data);
    }

    protected abstract @LayoutRes int layoutToInflate(int viewType);

    protected abstract void doOnBindViewHolder(BindViewHolder bindViewHolder, K binding, T item, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutToInflate(viewType), parent, false);
        return new BindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BindViewHolder bindViewHolder = (BindViewHolder) holder;
        doOnBindViewHolder(bindViewHolder, bindViewHolder.getBinding(), getItem(position), position);
        bindViewHolder.getBinding().executePendingBindings();
    }

    protected class BindViewHolder extends RecyclerView.ViewHolder {
        private K mBinding;

        public BindViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
        }

        public K getBinding() {
            return mBinding;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BindViewHolder that = (BindViewHolder) o;

            return !(mBinding != null ? !mBinding.equals(that.mBinding) : that.mBinding != null);

        }

        @Override
        public int hashCode() {
            return mBinding != null ? mBinding.hashCode() : 0;
        }
    }
}
