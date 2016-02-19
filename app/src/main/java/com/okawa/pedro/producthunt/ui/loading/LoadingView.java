package com.okawa.pedro.producthunt.ui.loading;

/**
 * Created by pokawa on 19/02/16.
 */
public interface LoadingView {

    void onLoadData();
    void onDataLoaded();
    void onError(String error);

}
