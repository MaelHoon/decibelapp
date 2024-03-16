package hoon.mael.decibel;

public class Constants {

    public static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";
    public static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel";
    public static final String INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity";
    public static final String PAGE_INDEX = "pageIndex";
    public static final String PAGE_INDEX_END = "pageIndexEnd"; // ble수신 중지시 페이지 인덱스 저장

    public static final int NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001;

    public static final int PAGE_CHANGE_INTERVAL = 3; //3초
    private Constants() {}
}
