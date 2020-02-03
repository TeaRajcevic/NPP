package com.rajcevic.tea.DiaryWebApp.utils;

import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static Date setCurrentDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}
