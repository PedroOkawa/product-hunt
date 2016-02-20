package com.okawa.pedro.producthunt.database;

import java.util.Collection;
import java.util.List;

import greendao.Category;
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

    public void updateCategories(Collection<Category> categories) {
        categoryDao.insertOrReplaceInTx(categories);
    }

    public List<Category> selectCategories() {
        return categoryDao.loadAll();
    }

}
