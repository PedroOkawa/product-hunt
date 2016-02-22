package com.okawa.pedro.producthunt.database;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.model.list.PostContent;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import greendao.Vote;
import greendao.VoteDao;

/**
 * Created by pokawa on 19/02/16.
 */
public class DatabaseRepository {
    private static final long SESSION_ID = Long.MAX_VALUE;
    private static final long CURRENT_CATEGORY_ID = 1;
    private static final String CURRENT_CATEGORY_NAME = "tech";

    private Category currentCategory;
    private int daysAgo;

    private Date lastDate;

    private AvatarDao avatarDao;
    private CategoryDao categoryDao;
    private CommentDao commentDao;
    private PostDao postDao;
    private ScreenshotDao screenshotDao;
    private SessionDao sessionDao;
    private ThumbnailDao thumbnailDao;
    private UserDao userDao;
    private VoteDao voteDao;

    private ConfigHelper configHelper;

    public DatabaseRepository(DaoSession daoSession, ConfigHelper configHelper) {
        this.avatarDao = daoSession.getAvatarDao();
        this.categoryDao = daoSession.getCategoryDao();
        this.commentDao = daoSession.getCommentDao();
        this.postDao = daoSession.getPostDao();
        this.screenshotDao = daoSession.getScreenshotDao();
        this.sessionDao = daoSession.getSessionDao();
        this.thumbnailDao = daoSession.getThumbnailDao();
        this.userDao = daoSession.getUserDao();
        this.voteDao = daoSession.getVoteDao();

        this.configHelper = configHelper;

        resetDaysAgo();
        resetLastDate();
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
                .where(PostDao.Properties.CreatedAt.eq(date))
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

    public List<PostContent> selectPostsByCategoryPaged(int offset) {
        List<Post> posts = postDao
                .queryBuilder()
                .orderDesc(PostDao.Properties.CreatedAt)
                .where(PostDao.Properties.CategoryId.eq(getCurrentCategoryId()))
                .limit(DatabaseModule.SELECT_LIMIT)
                .offset(offset)
                .list();
        return defineHeaders(posts, offset);
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

    /* VOTE */

    public void updateVotes(Collection<Vote> votes) {
        voteDao.insertOrReplaceInTx(votes);
    }

    public List<Vote> selectVotes(long postId) {
        return voteDao
                .queryBuilder()
                .orderDesc(VoteDao.Properties.CreatedAt)
                .where(VoteDao.Properties.PostId.eq(postId))
                .list();
    }

    /* DAYS AGO */

    public void addDayAgo() {
        daysAgo++;
    }

    public void resetDaysAgo() {
        daysAgo = 0;
    }

    public String getDaysAgo() {
        return String.valueOf(daysAgo);
    }

    /* POST CONTENT */

    private List<PostContent> defineHeaders(List<Post> posts, int offset) {
        List<PostContent> postContents = new ArrayList<>();

        for(Post post : posts) {
            PostContent postContent = new PostContent();

            if((posts.indexOf(post) == 0 && offset == 0 && configHelper.checkIsToday(lastDate)) ||
                    !(configHelper.checkSameDate(post.getCreatedAt(), lastDate))) {
                postContent.setIsHeader(true);
                postContent.setHeader(configHelper.getDateString(post.getCreatedAt()));
                lastDate = post.getCreatedAt();
            } else {
                postContent.setIsHeader(false);
                postContent.setPost(post);
            }

            postContents.add(postContent);
        }

        return postContents;
    }

    public void resetLastDate() {
        this.lastDate = new Date();
    }

}
