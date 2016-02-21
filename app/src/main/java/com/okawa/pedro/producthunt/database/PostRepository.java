package com.okawa.pedro.producthunt.database;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

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
    private ConfigHelper configHelper;

    public PostRepository(DaoSession daoSession, ConfigHelper configHelper) {
        postDao = daoSession.getPostDao();
        this.configHelper = configHelper;
    }

    public void updatePosts(Collection<Post> posts) {
        postDao.insertOrReplaceInTx(posts);
    }

    public List<Post> selectPostByDate(Date date) {
        return postDao.queryBuilder().where(PostDao.Properties.Date.eq(configHelper.convertDateToString(date))).list();
    }

    public List<Post> selectAllPostsPaged(int offset) {
        return postDao.queryBuilder().orderDesc(PostDao.Properties.Date).limit(DatabaseModule.SELECT_LIMIT).offset(offset).list();
    }

    public Post selectPostById(long id) {
        return postDao.queryBuilder().where(PostDao.Properties.Id.eq(id)).unique();
    }

}
