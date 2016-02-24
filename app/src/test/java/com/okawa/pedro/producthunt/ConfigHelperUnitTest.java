package com.okawa.pedro.producthunt;

import com.okawa.pedro.producthunt.util.helper.ConfigHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pokawa on 24/02/16.
 */
@RunWith(JUnit4.class)
public class ConfigHelperUnitTest {

    private static final String TEST_VALUE_DATE_YMD = "2016-01-01";
    private static final String TEST_VALUE_DATE_DMY = "01/01/2016";

    private ConfigHelper configHelper;

    @Before
    public void setup() {
        configHelper = new ConfigHelper();
    }

    @Test
    public void checkDateTextYMD() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);

        assert(configHelper.convertDateToString(calendar.getTime()).equals(TEST_VALUE_DATE_YMD));
    }

    @Test
    public void checkDateTextDMY() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);

        assert(configHelper.getDateString(null, calendar.getTime()).equals(TEST_VALUE_DATE_DMY));
    }

    @Test
    public void checkSameDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);

        Date dateA = calendar.getTime();
        Date dateB = calendar.getTime();

        assert(configHelper.checkSameDate(dateA, dateB));
    }

    @Test
    public void checkNotSameDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);

        Date dateA = calendar.getTime();

        calendar.set(2016, 0, 2);
        Date dateB = calendar.getTime();

        assert(!configHelper.checkSameDate(dateA, dateB));
    }

    @Test
    public void checkIsToday() {
        assert(configHelper.checkIsToday(new Date()));
    }

    @Test
    public void checkIsNotToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 0, 1);

        assert(!configHelper.checkIsToday(calendar.getTime()));
    }

}
