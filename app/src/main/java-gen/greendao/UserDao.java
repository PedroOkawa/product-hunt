package greendao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import greendao.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property UserId = new Property(0, Long.class, "userId", true, "USER_ID");
        public final static Property CreatedAt = new Property(1, java.util.Date.class, "createdAt", false, "CREATED_AT");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Image = new Property(3, String.class, "image", false, "IMAGE");
        public final static Property Username = new Property(4, String.class, "username", false, "USERNAME");
        public final static Property Headline = new Property(5, String.class, "headline", false, "HEADLINE");
        public final static Property TwitterUser = new Property(6, String.class, "twitterUser", false, "TWITTER_USER");
        public final static Property TwitterProfile = new Property(7, String.class, "twitterProfile", false, "TWITTER_PROFILE");
        public final static Property PostId = new Property(8, Long.class, "postId", true, "POST_ID");
    };

    private DaoSession daoSession;

    private Query<User> user_PostsQuery;
    private Query<User> post_MakersQuery;

    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"USER_ID\" INTEGER PRIMARY KEY ," + // 0: userId
                "\"CREATED_AT\" INTEGER," + // 1: createdAt
                "\"NAME\" TEXT," + // 2: name
                "\"IMAGE\" TEXT," + // 3: image
                "\"USERNAME\" TEXT," + // 4: username
                "\"HEADLINE\" TEXT," + // 5: headline
                "\"TWITTER_USER\" TEXT," + // 6: twitterUser
                "\"TWITTER_PROFILE\" TEXT," + // 7: twitterProfile
                "\"POST_ID\" INTEGER PRIMARY KEY );"); // 8: postId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(1, userId);
        }
 
        java.util.Date createdAt = entity.getCreatedAt();
        if (createdAt != null) {
            stmt.bindLong(2, createdAt.getTime());
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(4, image);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String headline = entity.getHeadline();
        if (headline != null) {
            stmt.bindString(6, headline);
        }
 
        String twitterUser = entity.getTwitterUser();
        if (twitterUser != null) {
            stmt.bindString(7, twitterUser);
        }
 
        String twitterProfile = entity.getTwitterProfile();
        if (twitterProfile != null) {
            stmt.bindString(8, twitterProfile);
        }
    }

    @Override
    protected void attachEntity(User entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // createdAt
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // image
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // username
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // headline
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // twitterUser
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // twitterProfile
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreatedAt(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUsername(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHeadline(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTwitterUser(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTwitterProfile(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "posts" to-many relationship of User. */
    public List<User> _queryUser_Posts(Long postId) {
        synchronized (this) {
            if (user_PostsQuery == null) {
                QueryBuilder<User> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.PostId.eq(null));
                user_PostsQuery = queryBuilder.build();
            }
        }
        Query<User> query = user_PostsQuery.forCurrentThread();
        query.setParameter(0, postId);
        return query.list();
    }

    /** Internal query to resolve the "makers" to-many relationship of Post. */
    public List<User> _queryPost_Makers(Long userId) {
        synchronized (this) {
            if (post_MakersQuery == null) {
                QueryBuilder<User> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.UserId.eq(null));
                post_MakersQuery = queryBuilder.build();
            }
        }
        Query<User> query = post_MakersQuery.forCurrentThread();
        query.setParameter(0, userId);
        return query.list();
    }

}
