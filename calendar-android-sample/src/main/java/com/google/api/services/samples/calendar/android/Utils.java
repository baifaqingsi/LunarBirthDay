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

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Common utilities.
 * 
 * @author Yaniv Inbar
 */
public class Utils {

  /**
   * Logs the given throwable and shows an error alert dialog with its message.
   * 
   * @param activity activity
   * @param tag log tag to use
   * @param t throwable to log and show
   */
  public static void logAndShow(Activity activity, String tag, Throwable t) {
    Log.e(tag, "Error", t);
    String message = t.getMessage();
    if (t instanceof GoogleJsonResponseException) {
      GoogleJsonError details = ((GoogleJsonResponseException) t).getDetails();
      if (details != null) {
        message = details.getMessage();
      }
    } else if (t.getCause() instanceof GoogleAuthException) {
      message = ((GoogleAuthException) t.getCause()).getMessage();
    }
    showError(activity, message);
  }

  /**
   * Logs the given message and shows an error alert dialog with it.
   * 
   * @param activity activity
   * @param tag log tag to use
   * @param message message to log and show or {@code null} for none
   */
  public static void logAndShowError(Activity activity, String tag, String message) {
    String errorMessage = getErrorMessage(activity, message);
    Log.e(tag, errorMessage);
    showErrorInternal(activity, errorMessage);
  }

  /**
   * Shows an error alert dialog with the given message.
   * 
   * @param activity activity
   * @param message message to show or {@code null} for none
   */
  public static void showError(Activity activity, String message) {
    String errorMessage = getErrorMessage(activity, message);
    showErrorInternal(activity, errorMessage);
  }

  private static void showErrorInternal(final Activity activity, final String errorMessage) {
    activity.runOnUiThread(new Runnable() {
      public void run() {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
      }
    });
  }

  private static String getErrorMessage(Activity activity, String message) {
    Resources resources = activity.getResources();
    if (message == null) {
      return resources.getString(R.string.error);
    }
    return resources.getString(R.string.error_format, message);
  }

  public static ArrayList<Lunar> getRepeatLunar(String date){
    ArrayList<Lunar> birth = new ArrayList<>();

    String[] split = date.split("-");
    if (split.length< 4) {
      return null;
    }
    String year = split[0];
    String month = split[1];
    String day = split[2];
    String repeat = split[3];

    Solar solar = new Solar();

    solar.setSolarDay(Integer.valueOf(day));
    solar.setSolarMonth(Integer.valueOf(month));
    solar.setSolarYear(Integer.valueOf(year));
    solar.setRepeat(Integer.valueOf(repeat));
    Lunar lunar = LunarSolarConverter.SolarToLunar(solar);
    birth.add(lunar);
    int years = Integer.valueOf(year);
    for (int i = 0; i < Integer.valueOf(repeat); i++) {
      years ++ ;
      solar.setSolarDay(Integer.valueOf(day));
      solar.setSolarMonth(Integer.valueOf(month));
      solar.setSolarYear(years);
      Lunar lunars = LunarSolarConverter.SolarToLunar(solar);
      birth.add(lunars);
    }

    return birth;
  }
}
