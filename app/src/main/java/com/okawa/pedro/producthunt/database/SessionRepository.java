package com.okawa.pedro.producthunt.database;

import greendao.DaoSession;
import greendao.Session;
import greendao.SessionDao;

/**
 * Created by pokawa on 19/02/16.
 */
public class SessionRepository {

    private static final long SESSION_ID = Long.MAX_VALUE;
    private SessionDao sessionDao;

    public SessionRepository(DaoSession daoSession) {
        this.sessionDao = daoSession.getSessionDao();
    }

    public void updateSession(Session session) {
        session.setId(SESSION_ID);
        sessionDao.insertOrReplace(session);
    }

    public Session selectSession() {
        return sessionDao.queryBuilder().where(SessionDao.Properties.Id.eq(SESSION_ID)).unique();
    }

    public boolean containsSession() {
        return sessionDao.count() > 0;
    }

}
