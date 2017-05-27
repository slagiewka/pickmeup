package pl.edu.agh.miss.ik.impl;

import pl.edu.agh.miss.ik.InverseKinematicsCalculator;

import java.util.LinkedHashMap;
import java.util.Map;

public class Lynx6DOFInverseKinematicsCalculator implements InverseKinematicsCalculator {
    public static final float BASE_HEIGHT = 6;
    public static final float HUMERUS = 12;
    public static final float ULNA = 12;
    public static final float HAND = 12;

    @Override
    public Map<Integer, Double> calculateResults(double x, double z, double angle) {
        Map<Integer, Double> servoPositions = new LinkedHashMap<>();
        double xb = (x - HAND * Math.cos(angle * Math.PI / 180)) / (ULNA + HUMERUS);
        double zb = (z - BASE_HEIGHT - HAND * Math.sin(angle * Math.PI / 180)) / (ULNA + HUMERUS);
        double q = Math.sqrt((1 / (Math.pow(xb, 2) + Math.pow(zb, 2))) - 1);
        double p1 = Math.atan2(zb + q * xb, xb - q * zb) * 180 / Math.PI;
        double p2 = Math.atan2(zb - q * xb, xb + q * zb)  * 180 / Math.PI;
        double t1 = p1-90;
        double t2 = p2-t1;
        double t3 = angle - p2;
        if(t1 > -60 && t1 < 60 && t2 > -20 && t2 < 80 && t3 > -45 && t3 < 45){
            servoPositions.put(1, t1);
            servoPositions.put(2, -t2);
            servoPositions.put(3, t3);
        } else {
            System.out.println("Servo angles out of possible range");
        }
        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
        System.out.println("t3 = " + t3);
        return servoPositions;
    }

}
