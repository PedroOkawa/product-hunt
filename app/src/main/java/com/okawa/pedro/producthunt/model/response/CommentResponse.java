package com.okawa.pedro.producthunt.model.response;

import java.util.List;

import greendao.Comment;

/**
 * Created by pokawa on 22/02/16.
 */
public class CommentResponse {

    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
