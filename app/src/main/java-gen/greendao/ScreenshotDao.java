package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.Screenshot;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SCREENSHOT".
*/
public class ScreenshotDao extends AbstractDao<Screenshot, Long> {

    public static final String TABLENAME = "SCREENSHOT";

    /**
     * Properties of entity Screenshot.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Big = new Property(1, String.class, "big", false, "BIG");
        public final static Property Small = new Property(2, String.class, "small", false, "SMALL");
    };


    public ScreenshotDao(DaoConfig config) {
        super(config);
    }
    
    public ScreenshotDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SCREENSHOT\" (" + //
                "\"ID\" INTEGER PRIMARY KEY ," + // 0: id
                "\"BIG\" TEXT," + // 1: big
                "\"SMALL\" TEXT);"); // 2: small
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SCREENSHOT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Screenshot entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String big = entity.getBig();
        if (big != null) {
            stmt.bindString(2, big);
        }
 
        String small = entity.getSmall();
        if (small != null) {
            stmt.bindString(3, small);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Screenshot readEntity(Cursor cursor, int offset) {
        Screenshot entity = new Screenshot( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // big
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // small
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Screenshot entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBig(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSmall(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Screenshot entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Screenshot entity) {
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
    
}