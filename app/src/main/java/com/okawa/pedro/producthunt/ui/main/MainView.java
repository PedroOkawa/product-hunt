package com.okawa.pedro.producthunt.ui.main;

/**
 * Created by pokawa on 19/02/16.
 */
public interface MainView {

    void onLoadData();
    void onDataLoaded();
    void onError(String error);

}
