package hoon.mael.decibel.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    private final SharedPreferences preferences;
    private Context context;

    public PrefUtils(Context context){
        this.context = context;
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }
}
