package hoon.mael.decibel.Utils;

public class BluetoothStateUtil {
    private static Boolean isReceiveStart = false;
    private static String receiveEndString = "";
    private static Boolean toogle = false;

    public static int BLE_STATE_OBSERVER = 0;

    public static final int BLE_STATE_NONE = 0;
    public static final int BLE_STATE_STOP = 1;
    public static final int BLE_STATE_RUNNING = 2;

    public static void setBLEStateRunning() {
        BLE_STATE_OBSERVER = BLE_STATE_RUNNING;
    }

    public static void setBleStateStop() {
        BLE_STATE_OBSERVER = BLE_STATE_STOP;
    }

    public static int getBleState(){
        return BLE_STATE_OBSERVER;
    }

    public static void setToogle(Boolean value) { //측정이 종료되면 1회만 페이지 전환이 이루어지게 하는 토글 변수
        toogle = value;
    }

    public static Boolean getToogle() {
        return toogle;
    }

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
