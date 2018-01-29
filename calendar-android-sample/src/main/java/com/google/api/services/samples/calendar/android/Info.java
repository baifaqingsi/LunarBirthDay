package com.google.api.services.samples.calendar.android;

import java.util.List;

/**
 * Created by hc on 2018/1/27.
 */

public class Info {

    public String year;
    public String month;
    public String day;
    public int repeat;
    private List<Lunar> mLunars;

    public List<Lunar> getLunars() {
        return mLunars;
    }

    public void setLunars(List<Lunar> lunars) {
        mLunars = lunars;
    }

    @Override
    public String toString() {
        return "Info{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", repeat=" + repeat +
                '}';
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
