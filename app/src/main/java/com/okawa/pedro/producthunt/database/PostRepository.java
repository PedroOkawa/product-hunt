package com.okawa.pedro.producthunt.database;

import java.text.SimpleDateFormat;
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
        String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return postDao.queryBuilder().where(PostDao.Properties.Date.eq(day)).list();
    }

    public Post selectPostById(long id) {
        return postDao.queryBuilder().where(PostDao.Properties.Id.eq(id)).unique();
    }

}
