package hoon.mael.decibel.Utils;

import android.content.Context;
import android.content.Intent;

public class PageUtil {
    private static int currentPage;

    public static void setPage(int value) {
        currentPage = value;
    }

    public static int getPage() {
        return currentPage;
    }

    public static void plusPageIndex() {
        currentPage++;
    }

    public static void minusPageIndex() {
        currentPage--;
    }

    public static void startActivity(Context context,Class<?> activityClass){
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
