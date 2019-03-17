package com.skynet.lian.utils;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;


public class LanguageUtils {

    /* ------------------------------------- */
    private static Locale myLocale;
    private String lang = "";
    // Lưu ngôn ngữ đã cài đặt
    public static void saveLocale(String lang, AppCompatActivity activity) {
        String langPref = "Language";
        SharedPreferences prefs =  activity.getSharedPreferences("CommonPrefs",
                AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    public static String loadLocale(AppCompatActivity activity) {
        String langPref = "Language";
        SharedPreferences prefs = activity.getSharedPreferences("CommonPrefs",
                AppCompatActivity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language, activity);
        return language;
    }
    // method phục vụ cho việc thay đổi ngôn ngữ.
    public static void changeLang(String lang, AppCompatActivity activity) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang, activity);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());

    }
}
