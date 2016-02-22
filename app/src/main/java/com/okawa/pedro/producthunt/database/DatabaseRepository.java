package com.okawa.pedro.producthunt.database;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import greendao.Avatar;
import greendao.AvatarDao;
import greendao.Category;
import greendao.CategoryDao;
import greendao.Comment;
import greendao.CommentDao;
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
    private static final long CURRENT_CATEGORY_ID = 1;
    private static final String CURRENT_CATEGORY_NAME = "tech";

    private Category currentCategory;
    private int daysAgo;

    private AvatarDao avatarDao;
    private CategoryDao categoryDao;
    private CommentDao commentDao;
    private PostDao postDao;
    private ScreenshotDao screenshotDao;
    private SessionDao sessionDao;
    private ThumbnailDao thumbnailDao;
    private UserDao userDao;

    public DatabaseRepository(DaoSession daoSession) {
        this.avatarDao = daoSession.getAvatarDao();
        this.categoryDao = daoSession.getCategoryDao();
        this.commentDao = daoSession.getCommentDao();
        this.postDao = daoSession.getPostDao();
        this.screenshotDao = daoSession.getScreenshotDao();
        this.sessionDao = daoSession.getSessionDao();
        this.thumbnailDao = daoSession.getThumbnailDao();
        this.userDao = daoSession.getUserDao();
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

    public void setCurrentCategory(String name) {
        this.currentCategory = categoryDao
                .queryBuilder()
                .where(CategoryDao.Properties.Name.eq(name))
                .unique();
    }

    public long getCurrentCategoryId() {
        return currentCategory == null ? CURRENT_CATEGORY_ID : currentCategory.getId();
    }

    public String getCurrentCategoryName() {
        return currentCategory == null ? CURRENT_CATEGORY_NAME : currentCategory.getSlug();
    }

    /* COMMENT */

    public void updateComments(Collection<Comment> comments) {
        commentDao.insertOrReplaceInTx(comments);
    }

    public List<Comment> selectCommentsFromPost(long postId) {
        return commentDao
                .queryBuilder()
                .orderDesc(CommentDao.Properties.CreatedAt)
                .where(CommentDao.Properties.PostId.eq(postId))
                .list();
    }

    /* POST */

    public void updatePosts(Collection<Post> posts) {
        postDao.insertOrReplaceInTx(posts);
    }

    public List<Post> selectPostByDate(Date date) {
        return postDao
                .queryBuilder()
                .where(PostDao.Properties.CreatedAt.eq(convertDateToString(date)))
                .list();
    }

    public List<Post> selectAllPostsPaged(int offset) {
        return postDao
                .queryBuilder()
                .orderDesc(PostDao.Properties.CreatedAt)
                .limit(DatabaseModule.SELECT_LIMIT)
                .offset(offset)
                .list();
    }

    public List<Post> selectPostsByCategoryPaged(int offset) {
        return postDao
                .queryBuilder()
                .orderDesc(PostDao.Properties.CreatedAt)
                .where(PostDao.Properties.CategoryId.eq(getCurrentCategoryId()))
                .limit(DatabaseModule.SELECT_LIMIT)
                .offset(offset)
                .list();
    }

    public Post selectPostById(long id) {
        return postDao
                .queryBuilder()
                .where(PostDao.Properties.Id.eq(id))
                .unique();
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
        return sessionDao
                .queryBuilder()
                .where(SessionDao.Properties.Id.eq(SESSION_ID))
                .unique();
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

    /* DATE FORMAT */

    public String convertDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public void resetDaysAgo() {
        daysAgo = 0;
    }

    public String getDaysAgo() {
        return String.valueOf(daysAgo++);
    }

    public boolean checkIsToday(Date date) {
        return removeTime(new Date()).compareTo(removeTime(date)) == 0;
    }

    private Date removeTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

}
