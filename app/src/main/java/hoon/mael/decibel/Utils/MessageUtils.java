package hoon.mael.decibel.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class MessageUtils {
    public static void showToastMassage(Context context, String Message){
        Toast.makeText(context,Message,Toast.LENGTH_SHORT).show();
    }

    public static void disableNavigationBar(Activity activity){
        int newUiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}
