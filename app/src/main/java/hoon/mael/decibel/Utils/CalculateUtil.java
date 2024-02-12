package hoon.mael.decibel.Utils;

public class CalculateUtil {
    public static double calculateCorrection(double x, double y) {
        double d = x - y;
        d = round2(d);

        if (d < 3.0 || d > 10.0) {
            return 0.0;
        }
        return round1(-10 * Math.log10(1 - Math.pow(10, -0.1 * d))) * -1;
    }

    private static double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
