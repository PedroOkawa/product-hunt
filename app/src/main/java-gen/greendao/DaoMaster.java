package greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import greendao.SessionDao;
import greendao.CategoryDao;
import greendao.UserDao;
import greendao.AvatarDao;
import greendao.PostDao;
import greendao.ThumbnailDao;
import greendao.ScreenshotDao;
import greendao.CommentDao;
import greendao.VoteDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        SessionDao.createTable(db, ifNotExists);
        CategoryDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        AvatarDao.createTable(db, ifNotExists);
        PostDao.createTable(db, ifNotExists);
        ThumbnailDao.createTable(db, ifNotExists);
        ScreenshotDao.createTable(db, ifNotExists);
        CommentDao.createTable(db, ifNotExists);
        VoteDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        SessionDao.dropTable(db, ifExists);
        CategoryDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        AvatarDao.dropTable(db, ifExists);
        PostDao.dropTable(db, ifExists);
        ThumbnailDao.dropTable(db, ifExists);
        ScreenshotDao.dropTable(db, ifExists);
        CommentDao.dropTable(db, ifExists);
        VoteDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(SessionDao.class);
        registerDaoClass(CategoryDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(AvatarDao.class);
        registerDaoClass(PostDao.class);
        registerDaoClass(ThumbnailDao.class);
        registerDaoClass(ScreenshotDao.class);
        registerDaoClass(CommentDao.class);
        registerDaoClass(VoteDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
