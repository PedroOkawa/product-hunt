package com.okawa.pedro.producthunt.model;

import java.util.List;

import greendao.Post;

/**
 * Created by pokawa on 20/02/16.
 */
public class PostResponse {

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
