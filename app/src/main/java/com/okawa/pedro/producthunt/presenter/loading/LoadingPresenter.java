package com.okawa.pedro.producthunt.presenter.loading;

import com.okawa.pedro.producthunt.databinding.ActivityLoadingBinding;

/**
 * Created by pokawa on 19/02/16.
 */
public interface LoadingPresenter {

    void initialize(ActivityLoadingBinding binding);
    void validateToken();
    void dispose();

}
