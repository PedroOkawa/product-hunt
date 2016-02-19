package com.okawa.pedro.producthunt.database;

import greendao.CategoryDao;
import greendao.DaoSession;

/**
 * Created by pokawa on 19/02/16.
 */
public class CategoryRepository {

    private CategoryDao categoryDao;

    public CategoryRepository(DaoSession daoSession) {
        this.categoryDao = daoSession.getCategoryDao();
    }


}
