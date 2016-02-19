package com.okawa.pedro.producthunt.util.listener;

/**
 * Created by pokawa on 19/02/16.
 */
public interface ApiListener {

    void onDataLoaded();
    void onError(String error);

}
