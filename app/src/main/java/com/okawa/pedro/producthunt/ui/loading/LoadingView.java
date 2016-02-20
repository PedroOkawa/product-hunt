package com.okawa.pedro.producthunt.ui.loading;

/**
 * Created by pokawa on 19/02/16.
 */
public interface LoadingView {

    void onRequest();
    void onComplete();
    void onError(String error);

}
