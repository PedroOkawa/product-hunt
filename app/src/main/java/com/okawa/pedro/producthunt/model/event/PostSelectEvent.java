package com.okawa.pedro.producthunt.model.event;

/**
 * Created by pokawa on 21/02/16.
 */
public class PostSelectEvent {

    private long postId;

    public PostSelectEvent(long postId) {
        this.postId = postId;
    }

    public long getPostId() {
        return postId;
    }
}
