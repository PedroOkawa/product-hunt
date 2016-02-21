package greendao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import greendao.Post;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "POST".
*/
public class PostDao extends AbstractDao<Post, Long> {

    public static final String TABLENAME = "POST";

    /**
     * Properties of entity Post.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property UserIdFK = new Property(1, Long.class, "userIdFK", false, "USER_ID_FK");
        public final static Property ThumbnailIdFK = new Property(2, Long.class, "thumbnailIdFK", false, "THUMBNAIL_ID_FK");
        public final static Property CategoryId = new Property(3, Long.class, "categoryId", false, "CATEGORY_ID");
        public final static Property Date = new Property(4, String.class, "date", false, "DATE");
        public final static Property Name = new Property(5, String.class, "name", false, "NAME");
        public final static Property Tagline = new Property(6, String.class, "tagline", false, "TAGLINE");
        public final static Property VotesCount = new Property(7, Long.class, "votesCount", false, "VOTES_COUNT");
        public final static Property RedirectUrl = new Property(8, String.class, "redirectUrl", false, "REDIRECT_URL");
    };

    private DaoSession daoSession;

    private Query<Post> user_PostsQuery;

    public PostDao(DaoConfig config) {
        super(config);
    }
    
    public PostDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"POST\" (" + //
                "\"ID\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID_FK\" INTEGER," + // 1: userIdFK
                "\"THUMBNAIL_ID_FK\" INTEGER," + // 2: thumbnailIdFK
                "\"CATEGORY_ID\" INTEGER," + // 3: categoryId
                "\"DATE\" TEXT," + // 4: date
                "\"NAME\" TEXT," + // 5: name
                "\"TAGLINE\" TEXT," + // 6: tagline
                "\"VOTES_COUNT\" INTEGER," + // 7: votesCount
                "\"REDIRECT_URL\" TEXT);"); // 8: redirectUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"POST\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Post entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userIdFK = entity.getUserIdFK();
        if (userIdFK != null) {
            stmt.bindLong(2, userIdFK);
        }
 
        Long thumbnailIdFK = entity.getThumbnailIdFK();
        if (thumbnailIdFK != null) {
            stmt.bindLong(3, thumbnailIdFK);
        }
 
        Long categoryId = entity.getCategoryId();
        if (categoryId != null) {
            stmt.bindLong(4, categoryId);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(5, date);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(6, name);
        }
 
        String tagline = entity.getTagline();
        if (tagline != null) {
            stmt.bindString(7, tagline);
        }
 
        Long votesCount = entity.getVotesCount();
        if (votesCount != null) {
            stmt.bindLong(8, votesCount);
        }
 
        String redirectUrl = entity.getRedirectUrl();
        if (redirectUrl != null) {
            stmt.bindString(9, redirectUrl);
        }
    }

    @Override
    protected void attachEntity(Post entity) {
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
    public Post readEntity(Cursor cursor, int offset) {
        Post entity = new Post( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userIdFK
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // thumbnailIdFK
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // categoryId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // date
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // name
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // tagline
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // votesCount
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // redirectUrl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Post entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserIdFK(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setThumbnailIdFK(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setCategoryId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTagline(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setVotesCount(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setRedirectUrl(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Post entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Post entity) {
        if(entity != null) {
            return entity.getId();
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
    public List<Post> _queryUser_Posts(Long id) {
        synchronized (this) {
            if (user_PostsQuery == null) {
                QueryBuilder<Post> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Id.eq(null));
                user_PostsQuery = queryBuilder.build();
            }
        }
        Query<Post> query = user_PostsQuery.forCurrentThread();
        query.setParameter(0, id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getUserDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getThumbnailDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getScreenshotDao().getAllColumns());
            builder.append(" FROM POST T");
            builder.append(" LEFT JOIN USER T0 ON T.\"USER_ID_FK\"=T0.\"ID\"");
            builder.append(" LEFT JOIN THUMBNAIL T1 ON T.\"THUMBNAIL_ID_FK\"=T1.\"ID\"");
            builder.append(" LEFT JOIN SCREENSHOT T2 ON T.\"ID\"=T2.\"ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Post loadCurrentDeep(Cursor cursor, boolean lock) {
        Post entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);
        offset += daoSession.getUserDao().getAllColumns().length;

        Thumbnail thumbnail = loadCurrentOther(daoSession.getThumbnailDao(), cursor, offset);
        entity.setThumbnail(thumbnail);
        offset += daoSession.getThumbnailDao().getAllColumns().length;

        Screenshot screenshot = loadCurrentOther(daoSession.getScreenshotDao(), cursor, offset);
        entity.setScreenshot(screenshot);

        return entity;    
    }

    public Post loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Post> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Post> list = new ArrayList<Post>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Post> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Post> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
