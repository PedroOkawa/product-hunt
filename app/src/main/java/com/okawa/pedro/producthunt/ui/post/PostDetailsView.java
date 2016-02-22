package com.okawa.pedro.producthunt.ui.post;

/**
 * Created by pokawa on 21/02/16.
 */
public interface PostDetailsView {

    void initializeToolbar(String title);
    void initializeStatusBar();
    void changeStatusBarColor(int color);
    void onRequest();
    void onComplete();
    void onErrorComments(String error);
    void onErrorVotes(String error);

}
