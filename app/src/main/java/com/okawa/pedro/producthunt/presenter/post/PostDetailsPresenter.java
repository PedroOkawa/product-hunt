package com.okawa.pedro.producthunt.presenter.post;

import android.view.LayoutInflater;

import com.okawa.pedro.producthunt.databinding.ActivityPostDetailsBinding;

/**
 * Created by pokawa on 21/02/16.
 */
public interface PostDetailsPresenter {

    void initialize(ActivityPostDetailsBinding binding, long postId, LayoutInflater layoutInflater);
    void setActive(boolean active);

}
