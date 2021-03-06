package greendao;

import java.util.List;
import greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here

import com.google.gson.annotations.SerializedName;
// KEEP INCLUDES END
/**
 * Entity mapped to table "USER".
 */
public class User {

    // KEEP FIELDS - put your custom fields here

    private Long id;
    @SerializedName("created_at")
    private java.util.Date createdAt;
    private String name;
    private String username;
    private String headline;
    @SerializedName("twitter_username")
    private String twitterUser;
    @SerializedName("profile_url")
    private String profileUrl;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserDao myDao;

    @SerializedName("image_url")
    private Avatar avatar;
    private Long avatar__resolvedKey;

    private List<Post> posts;
    // KEEP FIELDS END

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, java.util.Date createdAt, String name, String username, String headline, String twitterUser, String profileUrl) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.username = username;
        this.headline = headline;
        this.twitterUser = twitterUser;
        this.profileUrl = profileUrl;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getTwitterUser() {
        return twitterUser;
    }

    public void setTwitterUser(String twitterUser) {
        this.twitterUser = twitterUser;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    /** To-one relationship, resolved on first access. */
    public Avatar getAvatar() {
        Long __key = this.id;
        if (avatar__resolvedKey == null || !avatar__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AvatarDao targetDao = daoSession.getAvatarDao();
            Avatar avatarNew = targetDao.load(__key);
            synchronized (this) {
                avatar = avatarNew;
            	avatar__resolvedKey = __key;
            }
        }
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        synchronized (this) {
            this.avatar = avatar;
            id = avatar == null ? null : avatar.getId();
            avatar__resolvedKey = id;
        }
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Post> getPosts() {
        if (posts == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PostDao targetDao = daoSession.getPostDao();
            List<Post> postsNew = targetDao._queryUser_Posts(id);
            synchronized (this) {
                if(posts == null) {
                    posts = postsNew;
                }
            }
        }
        return posts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetPosts() {
        posts = null;
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
        avatar.setId(id);
        setAvatar(avatar);
    }
    // KEEP METHODS END

}
