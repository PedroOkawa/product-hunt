package com.okawa.pedro.producthunt.ui.post;

/**
 * Created by pokawa on 21/02/16.
 */
public interface PostDetailsView {

    void onRequestVotes();
    void onCompleteVotes();
    void onErrorVotes(String error);
    void onRequestComments();
    void onCompleteComments();
    void onErrorComments(String error);

}
