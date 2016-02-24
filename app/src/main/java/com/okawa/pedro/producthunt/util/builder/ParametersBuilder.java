package com.okawa.pedro.producthunt.util.builder;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pokawa on 23/02/16.
 */
public class ParametersBuilder {

    public static final String PARAMETER_DAY = "day";
    public static final String PARAMETER_DAYS_AGO = "days_ago";
    public static final String PARAMETER_OLDER = "older";
    public static final String PARAMETER_NEWER = "newer";
    public static final String PARAMETER_ORDER = "order";
    public static final String PARAMETER_PER_PAGE = "per_page";
    public static final String PARAMETER_CATEGORY = "search[category]";
    public static final String VALUE_ASC = "ASC";
    public static final String VALUE_DESC = "DESC";

    private boolean isInit;

    private Map<String, String> parameters;

    public ParametersBuilder init() {
        parameters = new HashMap<>();
        isInit = true;
        return this;
    }

    public ParametersBuilder setDay(String day) {
        checkInit();
        parameters.put(PARAMETER_DAY, day);
        return this;
    }

    public ParametersBuilder setDaysAgo(String daysAgo) {
        checkInit();
        parameters.put(PARAMETER_DAYS_AGO, daysAgo);
        return this;
    }

    public ParametersBuilder setOlder(String id) {
        checkInit();
        parameters.put(PARAMETER_OLDER, id);
        return this;
    }

    public ParametersBuilder setNewer(String id) {
        checkInit();
        parameters.put(PARAMETER_NEWER, id);
        return this;
    }

    public ParametersBuilder setCategory(String category) {
        checkInit();
        parameters.put(PARAMETER_CATEGORY, category);
        return this;
    }

    public ParametersBuilder setPagination() {
        checkInit();
        parameters.put(PARAMETER_PER_PAGE, String.valueOf(DatabaseModule.SELECT_LIMIT));
        return this;
    }

    public ParametersBuilder setAscending() {
        checkInit();
        parameters.put(PARAMETER_ORDER, VALUE_ASC);
        return this;
    }

    public ParametersBuilder setDescending() {
        checkInit();
        parameters.put(PARAMETER_ORDER, VALUE_DESC);
        return this;
    }

    public Map<String, String> generateParameters() {
        isInit = false;
        return parameters;
    }

    private void checkInit() {
        if(!isInit) {
            throw new IllegalAccessError("ParametersBuilder: It's necessary to call init() first!");
        }
    }

}
