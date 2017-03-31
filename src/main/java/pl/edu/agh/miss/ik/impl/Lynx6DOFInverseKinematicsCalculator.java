package pl.edu.agh.miss.ik.impl;

import pl.edu.agh.miss.ik.InverseKinematicsCalculator;

import java.util.LinkedHashMap;
import java.util.Map;

public class Lynx6DOFInverseKinematicsCalculator implements InverseKinematicsCalculator {
    private static final double BASE_HEIGHT = 6;
    private static final double HUMERUS = 12;
    private static final double ULNA = 12;
    private static final double HAND = 14;

    @Override
    public Map<Integer, Double> calculateResults(double x, double z, double angle) {
        Map<Integer, Double> servoPositions = new LinkedHashMap<>();
        double xb = (x-HAND*Math.cos(angle*Math.PI/180))/2*HUMERUS;
        double zb = (z-BASE_HEIGHT-HAND*Math.sin(angle*Math.PI/180))/2*HUMERUS;
        double q = Math.sqrt((1/(Math.pow(xb,2)+Math.pow(zb,2)))-1);
        double p1 = Math.atan2(xb-q*zb,zb+q*xb)*180/Math.PI;
        double p2 = Math.atan2(xb+q*zb, zb-q*xb)*180/Math.PI;
        double t1 = p1-90;
        double t2 = p2-t1;
        double t3 = angle - p2;
        servoPositions.put(1, t1);
        servoPositions.put(2, t2);
        servoPositions.put(3, t3);
        return servoPositions;
    }

}
