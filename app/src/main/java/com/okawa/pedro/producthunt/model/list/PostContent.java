package com.okawa.pedro.producthunt.model.list;

import greendao.Post;

/**
 * Created by pokawa on 22/02/16.
 */
public class PostContent {

    private boolean isHeader;
    private String header;
    private Post post;

    public void setIsHeader(boolean header) {
        isHeader = header;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
