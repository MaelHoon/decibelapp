package hoon.mael.decibel.Utils;

import android.content.Context;
import android.widget.Toast;

public class MessageUtils {
    public static void showToastMassage(Context context, String Message){
        Toast.makeText(context,Message,Toast.LENGTH_SHORT).show();
    }

}
