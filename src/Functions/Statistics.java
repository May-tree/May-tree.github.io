package Functions;

public class Statistics {

    public static double computeL(double k, double s, double p) {
        return Math.log(1 - p) / Math.log(1 - Math.pow(s, k));
    }
}
