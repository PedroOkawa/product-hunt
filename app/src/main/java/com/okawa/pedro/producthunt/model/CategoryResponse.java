package com.okawa.pedro.producthunt.model;

import java.util.List;

import greendao.Category;

/**
 * Created by pokawa on 20/02/16.
 */
public class CategoryResponse {

    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
