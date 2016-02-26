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
    private static final String DEFAULT_CATEGORY_NAME = "Tech";
    private static final String DEFAULT_CATEGORY_SLUG = "tech";

    public static final int WHERE_ALL = 0x0000;
    public static final int WHERE_DATE = 0x0001;

    public static final int ORDER_BY_ID = 0x0000;
    public static final int ORDER_BY_VOTE = 0x0001;
    public static final int ORDER_BY_TITLE = 0x0002;
    public static final int ORDER_BY_USER = 0x0003;

    private Category currentCategory;
    private int currentWhere;
    private int currentOrder;

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

        this.currentOrder = ORDER_BY_ID;
        this.currentWhere = WHERE_ALL;
    }

    /* ORDER */

    public void setOrderBy(int order) {
        this.currentOrder = order;
    }

    /* WHERE */

    public void setWhereType(int where) {
        this.currentWhere = where;
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

        resetLastUpdatePostId();
    }

    public long getCurrentCategoryId() {
        return currentCategory == null ? CURRENT_CATEGORY_ID : currentCategory.getId();
    }

    public String getCurrentCategoryName() {
        return currentCategory == null ? DEFAULT_CATEGORY_NAME : currentCategory.getName();
    }

    public String getCurrentCategorySlug() {
        return currentCategory == null ? DEFAULT_CATEGORY_SLUG : currentCategory.getSlug();
    }

    /* COMMENT */

    public void updateComments(List<Comment> comments) {
        commentDao.insertOrReplaceInTx(comments);

        Session session = selectSession();
        session.setLastCommentId(getLastUpdatedCommentId());
        sessionDao.update(session);
    }

    public List<Comment> selectCommentsFromPost(long postId, int offset) {
        return commentDao
                .queryBuilder()
                .orderDesc(CommentDao.Properties.CreatedAt)
                .where(CommentDao.Properties.PostId.eq(postId), CommentDao.Properties.ParentCommentId.isNull())
                .offset(offset)
                .limit(DatabaseModule.SELECT_LIMIT)
                .list();
    }

    public long getLastUpdatedCommentId() {
        Comment comment = commentDao
                .queryBuilder()
                .orderDesc(CommentDao.Properties.UpdateDate)
                .limit(1)
                .unique();

        return comment != null ? comment.getId() : 0;
    }

    /* POST */

    public void updatePosts(Collection<Post> posts) {
        postDao.insertOrReplaceInTx(posts);

        Session session = selectSession();
        session.setLastPostId(getLastUpdatedPostId());
        sessionDao.update(session);
    }

    public List<PostContent> selectPostsByCategory(Date date, Context context, int offset) {

        QueryBuilder queryBuilder = postDao.queryBuilder();

        switch(currentWhere) {
            case WHERE_DATE:
                queryBuilder
                        .where(PostDao.Properties.CategoryId.eq(getCurrentCategoryId()),
                                PostDao.Properties.Day.eq(configHelper.convertDateToString(date)));
                break;
            case WHERE_ALL:
                queryBuilder
                        .where(PostDao.Properties.CategoryId.eq(getCurrentCategoryId()));
                break;
        }

        switch(currentOrder) {
            case ORDER_BY_ID:
                queryBuilder
                        .orderDesc(PostDao.Properties.Id);
                break;
            case ORDER_BY_VOTE:
                queryBuilder
                        .orderDesc(PostDao.Properties.VotesCount);
                break;
            case ORDER_BY_TITLE:
                queryBuilder
                        .orderAsc(PostDao.Properties.Name);
                break;
            case ORDER_BY_USER:
                queryBuilder
                        .orderAsc(PostDao.Properties.UserName);
                break;
        }

        queryBuilder
                .limit(DatabaseModule.SELECT_LIMIT)
                .offset(offset)
                .distinct();

        return defineHeaders(context, queryBuilder.list(), offset);
    }

    public Post selectPostById(long id) {
        resetPostSession();

        return postDao
                .queryBuilder()
                .where(PostDao.Properties.Id.eq(id))
                .unique();
    }

    private void resetLastUpdatePostId() {
        Session session = selectSession();
        session.setLastPostId((long)(Integer.MAX_VALUE));
        sessionDao.update(session);
    }

    public long getLastUpdatedPostId() {
        Post post = postDao
                .queryBuilder()
                .orderDesc(PostDao.Properties.UpdateDate)
                .limit(1)
                .unique();

        return post != null ? post.getId() : Integer.MAX_VALUE;
    }

    /* SCREENSHOT */

    public void updateScreenshot(Screenshot screenshot) {
        screenshotDao.insertOrReplace(screenshot);
    }

    /* SESSION */

    public void updateSession(Session session) {
        session.setId(SESSION_ID);
        session.setLastPostId((long)(Integer.MAX_VALUE));
        session.setLastPostDay(configHelper.convertDateToString(new Date()));
        session.setLastCommentId((long)(Integer.MAX_VALUE));
        session.setLastVoteId(0L);
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

    public void resetLastPostSession() {
        Session session = selectSession();
        session.setLastPostId((long)(Integer.MAX_VALUE));
        session.setLastPostDay(configHelper.convertDateToString(new Date()));
        sessionDao.update(session);
    }

    public void updateLastPostDate(String day) {
        Session session = selectSession();
        session.setLastPostDay(day);
        sessionDao.update(session);
    }

    public String getLastPostId() {
        Session session = selectSession();
        return String.valueOf(session.getLastPostId());
    }

    public String getLastCommentId() {
        Session session = selectSession();
        return String.valueOf(session.getLastCommentId());
    }

    public String getLastVoteId() {
        Session session = selectSession();
        return String.valueOf(session.getLastVoteId());
    }

    private void resetPostSession() {
        Session session = selectSession();
        session.setLastVoteId(0L);
        session.setLastCommentId((long)(Integer.MAX_VALUE));
        sessionDao.update(session);
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

    public void updateVotes(long postId, Collection<Vote> votes) {
        voteDao.insertOrReplaceInTx(votes);

        Session session = selectSession();

        session.setLastVoteId(getLastUpdatedVoteId(postId));
        sessionDao.update(session);
    }

    public List<Vote> selectVotesFromPost(long postId, int offset) {
        Session session = selectSession();
        sessionDao.update(session);

        return voteDao
                .queryBuilder()
                .where(VoteDao.Properties.PostId.eq(postId))
                .limit(DatabaseModule.SELECT_LIMIT)
                .offset(offset)
                .list();
    }

    public long getLastUpdatedVoteId(long postId) {
        Vote vote = voteDao
                .queryBuilder()
                .where(VoteDao.Properties.PostId.eq(postId))
                .orderDesc(VoteDao.Properties.UpdateDate)
                .limit(1)
                .unique();

        return vote != null ? vote.getId() : 0;
    }

    /* POST CONTENT */

    private List<PostContent> defineHeaders(Context context, List<Post> posts, int offset) {
        List<PostContent> postContents = new ArrayList<>();

        Session session = selectSession();

        for(Post post : posts) {

            if((posts.indexOf(post) == 0 && offset == 0) || !(post.getDay().equals(session.getLastPostDay()))) {
                PostContent headerContent = new PostContent();

                headerContent.setIsHeader(true);
                headerContent.setHeader(configHelper.getDateString(context, post.getCreatedAt()));

                updateLastPostDate(post.getDay());

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
