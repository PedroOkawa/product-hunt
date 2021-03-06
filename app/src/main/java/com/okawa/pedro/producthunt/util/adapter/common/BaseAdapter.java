package com.okawa.pedro.producthunt.util.adapter.common;

import android.support.v7.widget.RecyclerView;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pokawa on 20/02/16.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    private List<T> data;
    private Set<T> uniques;

    public BaseAdapter(List<T> data) {
        this.data = data;
        this.uniques = new LinkedHashSet<>();
    }

    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }
        return data.size();
    }

    public void addDataSet(Collection<T> data) {
        uniques.addAll(data);
        this.data.clear();
        this.data.addAll(uniques);
        notifyDataSetChanged();
    }

    public void add(T item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public int getItemPosition(T item) {
        return data.indexOf(item);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(T item) {
        data.remove(item);
        notifyItemRemoved(getItemPosition(item));
    }

    public void reset() {
        uniques.clear();
        data.clear();
        notifyDataSetChanged();
    }

    public void move(int oldPosition, int newPosition) {
        swap(data, oldPosition, newPosition);
    }

    private void swap(List<T> data, int oldPosition, int newPosition) {
        T item = data.remove(oldPosition);
        add(item, newPosition);
    }

}
