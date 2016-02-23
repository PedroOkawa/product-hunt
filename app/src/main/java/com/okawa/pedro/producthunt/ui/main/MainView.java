package com.okawa.pedro.producthunt.ui.main;

import android.view.View;

/**
 * Created by pokawa on 19/02/16.
 */
public interface MainView {

    void initializeToolbar(String title);
    void setToolbarName(String title);
    void onRequest();
    void onComplete();
    void onError(String error);
    void openPostDetails(long postId, View view);

}
