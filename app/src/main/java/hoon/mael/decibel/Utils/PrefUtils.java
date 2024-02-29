package hoon.mael.decibel.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    private final SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String NOTICE_TITLE_KEY = "NOTICE_TITLE_KEY";
    public static final String NOTICE_CONTENT_KEY = "NOTICE_CONTENT_KEY";
    private static final String HIGHEST_DECIBEL_KEY = "HIGHEST_DECIBEL_KEY";

    public PrefUtils(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void setHighestDecibel(String value) {
        editor.putString(HIGHEST_DECIBEL_KEY, value);
        editor.apply();
    }

    public String getHighestDecibel() {
        return preferences.getString(HIGHEST_DECIBEL_KEY, "0");
    }

    public void reSetHighestDecibel() {
        editor.putString(HIGHEST_DECIBEL_KEY, "0");
        editor.apply();
    }
}
