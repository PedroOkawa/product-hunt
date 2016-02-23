package greendao;

import com.google.gson.annotations.SerializedName;

import greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "POST".
 */
public class Post {

    // KEEP FIELDS - put your custom fields here

    private Long id;
    private Long userIdFK;
    private String userName;
    private Long thumbnailIdFK;
    @SerializedName("category_id")
    private Long categoryId;
    @SerializedName("created_at")
    private java.util.Date createdAt;
    private String name;
    private String tagline;
    @SerializedName("votes_count")
    private Long votesCount;
    @SerializedName("redirect_url")
    private String redirectUrl;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient PostDao myDao;

    private User user;
    private Long user__resolvedKey;

    private Thumbnail thumbnail;
    private Long thumbnail__resolvedKey;

    @SerializedName("screenshot_url")
    private Screenshot screenshot;
    private Long screenshot__resolvedKey;
    // KEEP FIELDS END

    public Post() {
    }

    public Post(Long id) {
        this.id = id;
    }

    public Post(Long id, Long userIdFK, Long thumbnailIdFK, String userName, Long categoryId, java.util.Date createdAt, String name, String tagline, Long votesCount, String redirectUrl) {
        this.id = id;
        this.userIdFK = userIdFK;
        this.thumbnailIdFK = thumbnailIdFK;
        this.userName = userName;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.name = name;
        this.tagline = tagline;
        this.votesCount = votesCount;
        this.redirectUrl = redirectUrl;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPostDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserIdFK() {
        return userIdFK;
    }

    public void setUserIdFK(Long userIdFK) {
        this.userIdFK = userIdFK;
    }

    public Long getThumbnailIdFK() {
        return thumbnailIdFK;
    }

    public void setThumbnailIdFK(Long thumbnailIdFK) {
        this.thumbnailIdFK = thumbnailIdFK;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Long getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Long votesCount) {
        this.votesCount = votesCount;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        Long __key = this.userIdFK;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userIdFK = user == null ? null : user.getId();
            user__resolvedKey = userIdFK;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Thumbnail getThumbnail() {
        Long __key = this.thumbnailIdFK;
        if (thumbnail__resolvedKey == null || !thumbnail__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ThumbnailDao targetDao = daoSession.getThumbnailDao();
            Thumbnail thumbnailNew = targetDao.load(__key);
            synchronized (this) {
                thumbnail = thumbnailNew;
            	thumbnail__resolvedKey = __key;
            }
        }
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        synchronized (this) {
            this.thumbnail = thumbnail;
            thumbnailIdFK = thumbnail == null ? null : thumbnail.getId();
            thumbnail__resolvedKey = thumbnailIdFK;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Screenshot getScreenshot() {
        Long __key = this.id;
        if (screenshot__resolvedKey == null || !screenshot__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ScreenshotDao targetDao = daoSession.getScreenshotDao();
            Screenshot screenshotNew = targetDao.load(__key);
            synchronized (this) {
                screenshot = screenshotNew;
            	screenshot__resolvedKey = __key;
            }
        }
        return screenshot;
    }

    public void setScreenshot(Screenshot screenshot) {
        synchronized (this) {
            this.screenshot = screenshot;
            id = screenshot == null ? null : screenshot.getId();
            screenshot__resolvedKey = id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here

    public void sync() {
        setUser(user);
        getUser().sync();
        this.userName = user.getName();
        thumbnail.setId(id);
        setThumbnail(thumbnail);
        screenshot.setId(id);
        setScreenshot(screenshot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (userIdFK != null ? !userIdFK.equals(post.userIdFK) : post.userIdFK != null)
            return false;
        if (userName != null ? !userName.equals(post.userName) : post.userName != null)
            return false;
        if (thumbnailIdFK != null ? !thumbnailIdFK.equals(post.thumbnailIdFK) : post.thumbnailIdFK != null)
            return false;
        if (categoryId != null ? !categoryId.equals(post.categoryId) : post.categoryId != null)
            return false;
        if (createdAt != null ? !createdAt.equals(post.createdAt) : post.createdAt != null)
            return false;
        if (name != null ? !name.equals(post.name) : post.name != null) return false;
        if (tagline != null ? !tagline.equals(post.tagline) : post.tagline != null) return false;
        if (votesCount != null ? !votesCount.equals(post.votesCount) : post.votesCount != null)
            return false;
        if (redirectUrl != null ? !redirectUrl.equals(post.redirectUrl) : post.redirectUrl != null)
            return false;
        if (daoSession != null ? !daoSession.equals(post.daoSession) : post.daoSession != null)
            return false;
        if (myDao != null ? !myDao.equals(post.myDao) : post.myDao != null) return false;
        if (user != null ? !user.equals(post.user) : post.user != null) return false;
        if (user__resolvedKey != null ? !user__resolvedKey.equals(post.user__resolvedKey) : post.user__resolvedKey != null)
            return false;
        if (thumbnail != null ? !thumbnail.equals(post.thumbnail) : post.thumbnail != null)
            return false;
        if (thumbnail__resolvedKey != null ? !thumbnail__resolvedKey.equals(post.thumbnail__resolvedKey) : post.thumbnail__resolvedKey != null)
            return false;
        if (screenshot != null ? !screenshot.equals(post.screenshot) : post.screenshot != null)
            return false;
        return !(screenshot__resolvedKey != null ? !screenshot__resolvedKey.equals(post.screenshot__resolvedKey) : post.screenshot__resolvedKey != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userIdFK != null ? userIdFK.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (thumbnailIdFK != null ? thumbnailIdFK.hashCode() : 0);
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tagline != null ? tagline.hashCode() : 0);
        result = 31 * result + (votesCount != null ? votesCount.hashCode() : 0);
        result = 31 * result + (redirectUrl != null ? redirectUrl.hashCode() : 0);
        result = 31 * result + (daoSession != null ? daoSession.hashCode() : 0);
        result = 31 * result + (myDao != null ? myDao.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (user__resolvedKey != null ? user__resolvedKey.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (thumbnail__resolvedKey != null ? thumbnail__resolvedKey.hashCode() : 0);
        result = 31 * result + (screenshot != null ? screenshot.hashCode() : 0);
        result = 31 * result + (screenshot__resolvedKey != null ? screenshot__resolvedKey.hashCode() : 0);
        return result;
    }

    // KEEP METHODS END

}
