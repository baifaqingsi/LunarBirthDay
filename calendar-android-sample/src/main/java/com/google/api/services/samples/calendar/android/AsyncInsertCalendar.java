/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.calendar.android;

import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Strings;
import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Asynchronously insert a new calendar.
 * 
 * @author Yaniv Inbar
 */
class AsyncInsertCalendar extends CalendarAsyncTask {

  private final Calendar entry;
  ArrayList<Lunar> repeatLunar;
  private Lunar mLunar;
  private int mLunarDay;
  private String mRealDay;

  private int mLunarMonth;
  private String mRealMonth;

  private int mLunarYear;
  private int mRepeat;

  AsyncInsertCalendar(CalendarSampleActivity calendarSample, Calendar entry) {
    super(calendarSample);
    this.entry = entry;
  }

  public AsyncInsertCalendar(CalendarSampleActivity calendarSample,Calendar entry,ArrayList<Lunar> repeatLunar) {
    super(calendarSample,repeatLunar);
    this.entry = entry;
    this.repeatLunar = repeatLunar;
  }

  @Override
  protected void doInBackground() throws IOException {
    Calendar calendar = client.calendars().insert(entry).setFields(CalendarInfo.FIELDS).execute();
    creatBirth();
    model.add(calendar);
  }

  private void creatBirth(){

    // Refer to the Java quickstart on how to setup the environment:
// https://developers.google.com/google-apps/calendar/quickstart/java
// Change the scope to CalendarScopes.CALENDAR and delete any stored
// credentials.

    for (int i = 0; i < repeatLunar.size() - 1; i++) {
      mLunar = repeatLunar.get(i);
      mLunarDay = mLunar.getLunarDay();
      mLunarMonth = mLunar.getLunarMonth();
      mLunarYear = mLunar.getLunarYear();
      mRepeat = mLunar.getRepeat();

      createEvent(getDate(mLunarYear, mLunarMonth, mLunarDay));


    }



  }

  private String getDate(int year,int month,int day) {
    String s = year + "-" + month + "-" + day;
    Date date = null;
    java.util.Calendar c = java.util.Calendar.getInstance();//获取一个日历实例
    if(year >= java.util.Calendar.YEAR)
    c.set(year, month-1, day);//设定日历的日期
    date = c.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(date);
  }

  private void test(String year, String month, String day) {
      System.out.println(year + "-" + month +"-" + day);
  }


  private void createEvent(String date) {
    try {
        Event event = new Event()
                .setSummary("BirthDay")
                .setLocation("At Home")
                .setDescription("你的农历生日到了");
      System.out.println("你的农历生日到了 " + date);
        DateTime startDateTime = new DateTime(date +"T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Shanghai");
        event.setStart(start);

        DateTime endDateTime = new DateTime(date +"T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Shanghai");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=YEARLY;COUNT=" + 1};
        event.setRecurrence(Arrays.asList(recurrence));


        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        event = client.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    } catch (IOException e) {
      e.printStackTrace();
      Log.d(AddOrEditCalendarActivity.TAG, "IOException: " + e);
    }
  }
}
