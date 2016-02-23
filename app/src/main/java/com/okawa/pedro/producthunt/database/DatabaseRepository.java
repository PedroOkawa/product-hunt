package com.okawa.pedro.producthunt.database;

import android.content.Context;
import android.util.Log;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.model.list.PostContent;
import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
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
    private static final String CURRENT_CATEGORY_NAME = "Tech";
    private static final String CURRENT_CATEGORY_SLUG = "tech";

    private Category currentCategory;
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

        lastDate = new Date();
    }

    public long getCurrentCategoryId() {
        return currentCategory == null ? CURRENT_CATEGORY_ID : currentCategory.getId();
    }

    public String getCurrentCategoryName() {
        return currentCategory == null ? CURRENT_CATEGORY_NAME : currentCategory.getName();
    }

    public String getCurrentCategorySlug() {
        return currentCategory == null ? CURRENT_CATEGORY_SLUG : currentCategory.getSlug();
    }

    /* COMMENT */

    public void updateComments(Collection<Comment> comments) {
        commentDao.insertOrReplaceInTx(comments);
    }

    public List<Comment> selectCommentsFromPost(long postId, int offset) {
        return commentDao
                .queryBuilder()
                .orderDesc(CommentDao.Properties.CreatedAt)
                .where(CommentDao.Properties.PostId.eq(postId))
                .offset(offset)
                .limit(DatabaseModule.SELECT_LIMIT)
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

    public List<PostContent> selectPostsByCategoryPaged(Context context, int offset) {
        QueryBuilder queryBuilder = postDao
                .queryBuilder()
                .where(PostDao.Properties.CategoryId.eq(getCurrentCategoryId()))
                .orderDesc(PostDao.Properties.CreatedAt)
                .limit(DatabaseModule.SELECT_LIMIT)
                .offset(offset);

        List<Post> posts = queryBuilder.list();

        return defineHeaders(context, posts, offset);
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

    public String getDaysAgo() {
        Post post = postDao
                .queryBuilder()
                .where(PostDao.Properties.CategoryId.eq(getCurrentCategoryId()))
                .orderAsc(PostDao.Properties.CreatedAt)
                .limit(1)
                .unique();

        Date date = post != null ? post.getCreatedAt() : new Date();

        return String.valueOf(configHelper.calculateDifferenceDays(date));
    }

    /* POST CONTENT */

    private List<PostContent> defineHeaders(Context context, List<Post> posts, int offset) {
        List<PostContent> postContents = new ArrayList<>();

        for(Post post : posts) {

            if((posts.indexOf(post) == 0 && offset == 0) ||
                    !(configHelper.checkSameDate(post.getCreatedAt(), lastDate))) {
                PostContent headerContent = new PostContent();

                headerContent.setIsHeader(true);
                headerContent.setHeader(configHelper.getDateString(context, post.getCreatedAt()));

                lastDate = post.getCreatedAt();

                postContents.add(headerContent);
            }

            PostContent postContent = new PostContent();

            postContent.setIsHeader(false);
            postContent.setPost(post);

            postContents.add(postContent);
        }

        return postContents;
    }

}
