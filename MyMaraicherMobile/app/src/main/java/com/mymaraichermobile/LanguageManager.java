package com.mymaraichermobile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

public class LanguageManager {

    //region Private variables
    private static final String LANGUAGE_PREF_KEY = "language_pref";
    private static final String DEFAULT_LANGUAGE = "fr";
    //endregion

    //region Methods
    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return preferences.getString(LANGUAGE_PREF_KEY, DEFAULT_LANGUAGE);
    }

    public static void saveLanguage(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE_PREF_KEY, language);
        editor.apply();
    }

    public static boolean isLanguageChanged(Context context, String newLanguage) {
        String currentLanguage = getLanguage(context);

        Log.d("SETTINGS", "NOM " + currentLanguage);
        Log.d("SETTINGS", "NOM " + newLanguage);

        return !currentLanguage.equals(newLanguage);
    }

    public static void changeLanguage(Context context, String lang) {
        // On sauvegarde la nouvelle langue
        saveLanguage(context, lang.toLowerCase());

        // On met à jour la configuration de la langue
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        Resources resources = context.getResources();
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Rafraîchir l'activité actuelle
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }
    //endregion
}