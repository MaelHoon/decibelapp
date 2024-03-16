package hoon.mael.decibel.Utils;

import android.content.Context;
import android.content.Intent;

public class PageUtil {
    private static int currentNoticePage = 0;
    private static Boolean isInputNoticeActivity = false;
    private static Boolean isDecibelIntroActivity = false;

    public static void setInputNoticeActivity(Boolean value){isInputNoticeActivity = value;}
    public static Boolean getInputNoticeActivity(){return isInputNoticeActivity;}

    public static void setDecibelIntroActivity(Boolean value){isDecibelIntroActivity = value;}
    public static Boolean getDecibelIntroActivity(){return isDecibelIntroActivity;}

    public static void setNoticePage(int value) {
        currentNoticePage = value;
    }

    public static int getPage() {
        return currentNoticePage;
    }

    public static void startActivity(Context context,Class<?> activityClass){
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
