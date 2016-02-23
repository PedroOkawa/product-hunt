package com.okawa.pedro.producthunt.util.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pokawa on 23/02/16.
 */
public class ParametersBuilder {

    private String PARAMETER_DAY = "day";
    private String PARAMETER_DAYS_AGO = "days_ago";
    private String PARAMETER_OLDER = "older";
    private String PARAMETER_NEWER = "newer";
    private String PARAMETER_ORDER = "order";
    private String PARAMETER_CATEGORY = "search[category]";
    private String VALUE_ASC = "ASC";
    private String VALUE_DESC = "DESC";

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
            throw new IllegalArgumentException("ParametersBuilder: It's necessary to call init() first!");
        }
    }

}
