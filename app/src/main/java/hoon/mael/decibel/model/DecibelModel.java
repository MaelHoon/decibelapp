package hoon.mael.decibel.model;

public class DecibelModel {

    private static String currentDecibel;
    private static String averageDecibel;

    public static void setCurrentDecibel(String value){
        currentDecibel = value;
    }
    public static String getCurrentDecibel(){
        return currentDecibel;
    }

    public static void setAverageDecibel(String value){
        averageDecibel = value;
    }

    public static String getAverageDecibel(){
        return averageDecibel;
    }
}
