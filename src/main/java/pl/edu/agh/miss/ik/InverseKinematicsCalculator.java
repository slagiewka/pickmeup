package pl.edu.agh.miss.ik;

import java.util.Map;

public interface InverseKinematicsCalculator {
    Map<Integer, Double> calculateResults(double x, double z, double angle);
}
