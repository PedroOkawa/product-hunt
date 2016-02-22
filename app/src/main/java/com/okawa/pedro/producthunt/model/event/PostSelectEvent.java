package com.okawa.pedro.producthunt.model.event;

import android.view.View;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostSelectEvent {

    private long postId;
    private View view;

    public PostSelectEvent(long postId, View view) {
        this.postId = postId;
        this.view = view;
    }

    public long getPostId() {
        return postId;
    }

    public View getView() {
        return view;
    }
}
