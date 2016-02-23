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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostContent that = (PostContent) o;

        if (isHeader != that.isHeader) return false;
        if (header != null ? !header.equals(that.header) : that.header != null) return false;
        return !(post != null ? !post.equals(that.post) : that.post != null);

    }

    @Override
    public int hashCode() {
        int result = (isHeader ? 1 : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        return result;
    }
}
