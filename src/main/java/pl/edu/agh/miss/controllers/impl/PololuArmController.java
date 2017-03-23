package pl.edu.agh.miss.controllers.impl;

import pl.edu.agh.miss.controllers.ArmController;
import pl.edu.agh.miss.ik.InverseKinematicsCalculator;

import java.util.Map;

public class PololuArmController implements ArmController {
    InverseKinematicsCalculator inverseKinematicsCalculator;

    public void setPosition(double x, double y, double z, double angle) {
        Map<Integer, Double> result = inverseKinematicsCalculator.calculateResult();

    }

}
