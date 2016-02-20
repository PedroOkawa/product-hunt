package com.okawa.pedro.producthunt.database;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import greendao.DaoSession;
import greendao.Post;
import greendao.PostDao;

/**
 * Created by pokawa on 20/02/16.
 */
public class PostRepository {

    private PostDao postDao;

    public PostRepository(DaoSession daoSession) {
        postDao = daoSession.getPostDao();
    }

    public void updatePosts(Collection<Post> posts) {
        postDao.insertOrReplaceInTx(posts);
    }

    public List<Post> selectPostByDate(Date date) {
        return postDao.queryBuilder().where(PostDao.Properties.Date.eq(date)).list();
    }

    public Post selectPostById(long id) {
        return postDao.queryBuilder().where(PostDao.Properties.Id.eq(id)).unique();
    }

}
