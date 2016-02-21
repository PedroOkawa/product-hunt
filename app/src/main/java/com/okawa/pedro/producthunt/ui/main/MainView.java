package com.okawa.pedro.producthunt.ui.main;

/**
 * Created by pokawa on 19/02/16.
 */
public interface MainView {

    void initializeToolbar();
    void onRequest();
    void onComplete();
    void onError(String error);

}
