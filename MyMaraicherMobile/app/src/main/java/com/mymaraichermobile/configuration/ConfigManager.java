package com.mymaraichermobile.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ConfigManager extends AppCompatActivity {

    //region Private variables
    private static final String LANG_KEY = "lang_pref";
    private static final String IP_KEY = "ip_pref";
    private static final String PORT_KEY = "port_pref";
    private static final String DEFAULT_LANGUAGE = "fr";
    private static final String DEFAULT_IP = "91.177.209.51";
    private static final int DEFAULT_PORT = 50001;

    //endregion

    //region Methods

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return preferences.getString(LANG_KEY, DEFAULT_LANGUAGE);
    }

    public static void saveLang(Context context, String language) {

        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANG_KEY, language);
        editor.apply();
    }

    public static void saveConfig(Context context, String ip, int port) {

        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IP_KEY, ip);
        editor.putString(PORT_KEY, String.valueOf(port));
        editor.apply();
    }

    public static String getIp(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return preferences.getString(IP_KEY, DEFAULT_IP);
    }

    public static String getPort(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return preferences.getString(PORT_KEY, String.valueOf(DEFAULT_PORT));
    }

    public static boolean isLanguageChanged(Context context, String newLanguage) {
        String currentLanguage = getLanguage(context);

        Log.d("SETTINGS", "NOM " + currentLanguage);
        Log.d("SETTINGS", "NOM " + newLanguage);

        return !currentLanguage.equals(newLanguage);
    }

    public static void changeLanguage(Context context, String lang) {

        // On met à jour la configuration de la langue
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        Resources resources = context.getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // On sauvegarde la nouvelle langue
        saveLang(context, lang.toLowerCase());

    }

    public static void handleLanguageAndConfiguration(Context context) {
        String selectedLanguage = ConfigManager.getLanguage(context);

        ConfigManager.changeLanguage(context, selectedLanguage.toLowerCase());

    }

    public static void refreshUi(Context context) {

        // Rafraîchir l'activité actuelle
        if (context instanceof Activity cActivity) {
            cActivity.recreate();
        }

    }

    //endregion
}