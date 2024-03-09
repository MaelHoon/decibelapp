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
    private static final String HIGHEST_STANDARD_DECIBEL_END_KEY = "HIGHEST_STANDARD_DECIBEL_END_KEY";
    private static final String HIGHEST_DECIBEL_END_KEY = "HIGHEST_DECIBEL_END_KEY";

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
        return preferences.getString(key, "0");
    }

    public String getPoliceName(String key){
        return preferences.getString(key,"영등포");
    }

    public void setHighestDecibel(String value) {
        editor.putString(HIGHEST_DECIBEL_KEY, value);
        editor.apply();
    }

    public int getHighestDecibel() {
        return (int)Math.round(Double.parseDouble(preferences.getString(HIGHEST_DECIBEL_KEY, "0")));
    }

    public void setHighestStandardDecibelEnd(String value) {//수신종료시 등가소음 값 저장
        editor.putString(HIGHEST_STANDARD_DECIBEL_END_KEY, value);
        editor.apply();
    }

    public String getHighestStandardDecibelEnd() {
        String highestStandardDecibel = preferences.getString(HIGHEST_STANDARD_DECIBEL_END_KEY, "0");
        String standardBackgroundDecibel = preferences.getString("standardInput1", "0");

        double correctionDecibelValue = CalculateUtil.calculateCorrection(Double.parseDouble(highestStandardDecibel), Double.parseDouble(standardBackgroundDecibel));
        int correctionDecibelValueRound = (int)Math.round(Double.parseDouble(highestStandardDecibel))+(int)Math.round(correctionDecibelValue);
        return String.valueOf(correctionDecibelValueRound);
    }

    public void setHighestDecibelEnd(String value) {//수신종료시 최고소음 값 저장
        editor.putString(HIGHEST_DECIBEL_END_KEY, value);
        editor.apply();
    }

    public String getHighestDecibelEnd() {
        String highestDecibel = preferences.getString(HIGHEST_DECIBEL_END_KEY, "0");
        String standardBackgroundDecibel = preferences.getString("standardInput1", "0");

        double correctionDecibelValue = CalculateUtil.calculateCorrection(Double.parseDouble(highestDecibel), Double.parseDouble(standardBackgroundDecibel));
        int correctionDecibelValueRound = (int)Math.round(Double.parseDouble(highestDecibel))+(int)Math.round(correctionDecibelValue);
        return String.valueOf(correctionDecibelValueRound);
    }

    public void reSetHighestDecibel() {
        editor.putString(HIGHEST_DECIBEL_KEY, "0");
        editor.apply();
    }
}
