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
        public final static Property PostId = new Property(0, Long.class, "postId", true, "POST_ID");
        public final static Property Date = new Property(1, String.class, "date", false, "DATE");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Tagline = new Property(3, String.class, "tagline", false, "TAGLINE");
        public final static Property VotesCount = new Property(4, Long.class, "votesCount", false, "VOTES_COUNT");
        public final static Property RedirectUrl = new Property(5, String.class, "redirectUrl", false, "REDIRECT_URL");
        public final static Property User = new Property(6, Long.class, "user", false, "userId");
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
                "\"POST_ID\" INTEGER PRIMARY KEY ," + // 0: postId
                "\"DATE\" TEXT," + // 1: date
                "\"NAME\" TEXT," + // 2: name
                "\"TAGLINE\" TEXT," + // 3: tagline
                "\"VOTES_COUNT\" INTEGER," + // 4: votesCount
                "\"REDIRECT_URL\" TEXT," + // 5: redirectUrl
                "\"userId\" INTEGER);"); // 6: user
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
 
        Long postId = entity.getPostId();
        if (postId != null) {
            stmt.bindLong(1, postId);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(2, date);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String tagline = entity.getTagline();
        if (tagline != null) {
            stmt.bindString(4, tagline);
        }
 
        Long votesCount = entity.getVotesCount();
        if (votesCount != null) {
            stmt.bindLong(5, votesCount);
        }
 
        String redirectUrl = entity.getRedirectUrl();
        if (redirectUrl != null) {
            stmt.bindString(6, redirectUrl);
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
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // postId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // date
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // tagline
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // votesCount
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // redirectUrl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Post entity, int offset) {
        entity.setPostId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTagline(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setVotesCount(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setRedirectUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Post entity, long rowId) {
        entity.setPostId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Post entity) {
        if(entity != null) {
            return entity.getPostId();
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
    public List<Post> _queryUser_Posts(Long postId) {
        synchronized (this) {
            if (user_PostsQuery == null) {
                QueryBuilder<Post> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.PostId.eq(null));
                user_PostsQuery = queryBuilder.build();
            }
        }
        Query<Post> query = user_PostsQuery.forCurrentThread();
        query.setParameter(0, postId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM POST T");
            builder.append(" LEFT JOIN USER T0 ON T.\"userId\"=T0.\"USER_ID\"");
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
