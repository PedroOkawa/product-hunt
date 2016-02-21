package com.okawa.pedro.producthunt.database;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import greendao.Avatar;
import greendao.AvatarDao;
import greendao.Category;
import greendao.CategoryDao;
import greendao.DaoSession;
import greendao.Post;
import greendao.PostDao;
import greendao.Screenshot;
import greendao.ScreenshotDao;
import greendao.Session;
import greendao.SessionDao;
import greendao.Thumbnail;
import greendao.ThumbnailDao;
import greendao.User;
import greendao.UserDao;

/**
 * Created by pokawa on 19/02/16.
 */
public class DatabaseRepository {
    private static final long SESSION_ID = Long.MAX_VALUE;

    private AvatarDao avatarDao;
    private CategoryDao categoryDao;
    private PostDao postDao;
    private ScreenshotDao screenshotDao;
    private SessionDao sessionDao;
    private ThumbnailDao thumbnailDao;
    private UserDao userDao;

    private ConfigHelper configHelper;

    public DatabaseRepository(DaoSession daoSession, ConfigHelper configHelper) {
        this.avatarDao = daoSession.getAvatarDao();
        this.categoryDao = daoSession.getCategoryDao();
        this.postDao = daoSession.getPostDao();
        this.screenshotDao = daoSession.getScreenshotDao();
        this.sessionDao = daoSession.getSessionDao();
        this.thumbnailDao = daoSession.getThumbnailDao();
        this.userDao = daoSession.getUserDao();

        this.configHelper = configHelper;
    }

    /* AVATAR */

    public void updateAvatar(Avatar avatar) {
        avatarDao.insertOrReplace(avatar);
    }

    /* CATEGORY */

    public void updateCategories(Collection<Category> categories) {
        categoryDao.insertOrReplaceInTx(categories);
    }

    public List<Category> selectCategories() {
        return categoryDao.loadAll();
    }

    public boolean checkCategoriesLoaded() {
        return categoryDao.count() > 0;
    }

    /* POST */

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

    /* SCREENSHOT */

    public void updateScreenshot(Screenshot screenshot) {
        screenshotDao.insertOrReplace(screenshot);
    }

    /* SESSION */

    public void updateSession(Session session) {
        session.setId(SESSION_ID);
        sessionDao.insertOrReplace(session);
    }

    public Session selectSession() {
        return sessionDao.queryBuilder().where(SessionDao.Properties.Id.eq(SESSION_ID)).unique();
    }

    public boolean containsSession() {
        return sessionDao.count() > 0;
    }

    /* THUMBNAIL */

    public void updateThumbnail(Thumbnail thumbnail) {
        thumbnailDao.insertOrReplace(thumbnail);
    }

    /* USER */

    public void updateUser(User user) {
        userDao.insertOrReplace(user);
    }

}
