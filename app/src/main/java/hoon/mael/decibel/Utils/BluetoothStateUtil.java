package hoon.mael.decibel.Utils;

public class BluetoothStateUtil {
    private static Boolean isReceiveStart = false;
    private static String receiveEndString = "";

    public static Boolean getReceiveStatus() {
        return isReceiveStart;
    }

    public static void setIsReceiveStarted(Boolean value) {
        isReceiveStart = value;
    }

    public static String getReceiveEndString() {
        return receiveEndString;
    }

    public static void setReceiveEndString(String value) {
        receiveEndString = value;
    }
}
