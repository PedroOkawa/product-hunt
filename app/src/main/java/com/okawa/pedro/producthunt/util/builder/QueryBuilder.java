package com.okawa.pedro.producthunt.util.builder;

import greendao.PostDao;

/**
 * Created by pokawa on 23/02/16.
 */
public class QueryBuilder {

    private static final String WHERE_AND = " AND ";

    private boolean isInit;
    private StringBuilder stringBuilder;

    public QueryBuilder init() {
        isInit = true;
        stringBuilder = new StringBuilder();
        return this;
    }

    public QueryBuilder setPostByDate(long categoryId, String day) {
        checkInit();

        stringBuilder
                .append(PostDao.Properties.CategoryId.columnName)
                .append("='")
                .append(categoryId)
                .append("'")
                .append(WHERE_AND)
                .append(PostDao.Properties.Day.columnName)
                .append("='")
                .append(day)
                .append("'");

        return this;
    }

    public QueryBuilder setPostByCategory(long categoryId) {
        checkInit();

        stringBuilder
                .append(PostDao.Properties.CategoryId.columnName)
                .append("='")
                .append(categoryId)
                .append("'");

        return this;
    }

    public String generateQuery() {
        return stringBuilder.toString();
    }

    private void checkInit() {
        if(!isInit) {
            throw new IllegalArgumentException("QueryBuilder: It's necessary to call init() first!");
        }
    }
}
