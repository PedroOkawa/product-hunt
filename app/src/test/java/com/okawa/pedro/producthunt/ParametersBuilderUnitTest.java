package com.okawa.pedro.producthunt;

import com.okawa.pedro.producthunt.di.module.DatabaseModule;
import com.okawa.pedro.producthunt.util.builder.ParametersBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Map;
import static com.okawa.pedro.producthunt.util.builder.ParametersBuilder.*;

/**
 * Created by pokawa on 24/02/16.
 */
@RunWith(JUnit4.class)
public class ParametersBuilderUnitTest {

    private static final String TEST_VALUE_DAY = "2016-01-01";
    private static final String TEST_VALUE_DAYS_AGO = "42";
    private static final String TEST_VALUE_OLDER = "12345";
    private static final String TEST_VALUE_NEWER = "54321";
    private static final String TEST_VALUE_CATEGORY = "games";

    private ParametersBuilder parametersBuilder;

    @Before
    public void setup() {
        parametersBuilder = new ParametersBuilder();
    }

    @Test(expected = IllegalAccessError.class)
    public void checkInitTest() {
        parametersBuilder.setAscending();
    }

    @Test
    public void checkDayParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setDay(TEST_VALUE_DAY)
                .generateParameters();

        assert(parameters.get(PARAMETER_DAY).equals(TEST_VALUE_DAY));
    }

    @Test
    public void checkDaysAgoParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setDaysAgo(TEST_VALUE_DAYS_AGO)
                .generateParameters();

        assert(parameters.get(PARAMETER_DAYS_AGO).equals(TEST_VALUE_DAYS_AGO));
    }

    @Test
    public void checkOlderParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setOlder(TEST_VALUE_OLDER)
                .generateParameters();

        assert(parameters.get(PARAMETER_OLDER).equals(TEST_VALUE_OLDER));
    }

    @Test
    public void checkNewerParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setNewer(TEST_VALUE_NEWER)
                .generateParameters();

        assert(parameters.get(PARAMETER_NEWER).equals(TEST_VALUE_NEWER));
    }

    @Test
    public void checkOrderAscParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setAscending()
                .generateParameters();

        assert(parameters.get(PARAMETER_ORDER).equals(VALUE_ASC));
    }

    @Test
    public void checkOrderDescParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setDescending()
                .generateParameters();

        assert(parameters.get(PARAMETER_ORDER).equals(VALUE_DESC));
    }

    @Test
    public void checkPerPageParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setPagination()
                .generateParameters();

        assert(parameters.get(PARAMETER_PER_PAGE).equals(String.valueOf(DatabaseModule.SELECT_LIMIT)));
    }

    @Test
    public void checkCategoryParameters() {
        Map<String, String> parameters = parametersBuilder
                .init()
                .setCategory(TEST_VALUE_CATEGORY)
                .generateParameters();

        assert(parameters.get(PARAMETER_CATEGORY).equals(TEST_VALUE_CATEGORY));
    }

    @After
    public void dispose() {
        parametersBuilder = null;
    }

}
