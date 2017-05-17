package pl.edu.agh.miss;

import au.edu.federation.caliko.FabrikBone2D;
import au.edu.federation.caliko.FabrikChain2D;
import au.edu.federation.utils.Vec2f;
import driver.MapPoint;
import driver.SCIP;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import pl.edu.agh.miss.controllers.ArmController;
import pl.edu.agh.miss.controllers.impl.PololuArmController;
import pl.edu.agh.miss.ik.InverseKinematicsCalculator;
import pl.edu.agh.miss.ik.impl.Lynx6DOFInverseKinematicsCalculator;
import pl.edu.agh.miss.visualisation.Application;
import tools.SerialPortHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ArmControllerTestApplication {
    private static short PRODUCT_ID = 0x0089;
    private static short VENDOR_ID = 0x1ffb;
    private static float LASER_DIST = 9;

    private static final float BASE_HEIGHT = 6;
    private static final float HUMERUS = 12;
    private static final float ULNA = 12;
    private static final float HAND = 14;

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException, IOException, InterruptedException {
        if(args.length == 2){
            PRODUCT_ID = Short.parseShort(args[0]);
            VENDOR_ID = Short.parseShort(args[1]);
        }

        SerialPort hokuyoSerialPort = SerialPortHelper.getHokuyoSerialPort("/dev/ttyS80");
        SCIP scip = new SCIP(hokuyoSerialPort.getInputStream(), hokuyoSerialPort.getOutputStream());
        scip.laserOn();

        MapPoint closest = new MapPoint(1000.0, 0.0, 0);
        List<MapPoint> pointList = scip.singleScan();
        scip.laserOff();
        hokuyoSerialPort.close();
        for (MapPoint point : pointList) {
            if (point.getDistance() > 15.0 && point.getDistance() < closest.getDistance()) {
                closest = point;
            }
        }

        double x = (closest.xValue() / 10) + LASER_DIST;
        double y = closest.yValue() / 10;
        System.out.println("x: " + x + ". y: " + y);
        double z = 7.0;

        ArmController armController = new PololuArmController(PRODUCT_ID, VENDOR_ID);
        armController.setPosition(16, 0, 25, 0, false);
        Thread.sleep(2000);
        armController.setPosition(x, y, z+2.0, -30, false);
//        try {
        Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        armController.setPosition(x, y, z, -30, true);
        Thread.sleep(2000);
        armController.setPosition(16, 0, 25, 0, true);
//        InverseKinematicsCalculator inverseKinematicsCalculator = new Lynx6DOFInverseKinematicsCalculator();
//        FabrikChain2D chain = createChain(inverseKinematicsCalculator.calculateResults(Math.sqrt(x*x + y*y)+8.0, z, -45));
//        if(chain != null){
//            Application.drawChain(chain);
//        }
    }

    private static FabrikChain2D createChain(Map<Integer, Double> angles){
        if(angles.size() < 3){
            System.out.println("Cannot create chain");
            return null;
        }

        FabrikChain2D chain = new FabrikChain2D();

        FabrikBone2D basebone;
        basebone = new FabrikBone2D(new Vec2f(0.0f, -BASE_HEIGHT), new Vec2f(0.0f, 0.0f) );
        basebone.setClockwiseConstraintDegs(0);
        basebone.setAnticlockwiseConstraintDegs(0);
        chain.addBone(basebone);

        chain.setFixedBaseMode(true);
        chain.setBaseboneConstraintType(FabrikChain2D.BaseboneConstraintType2D.GLOBAL_ABSOLUTE);
        chain.setBaseboneConstraintUV( new Vec2f(0.0f, 1.0f) );

        float ang1 = (float)(90 + angles.get(1));
        float ang2 = ang1 + (float)( angles.get(2) - 90);
        float ang3 = ang1 + (float)(90 + angles.get(2)) + (float)(angles.get(3) - 180);
        chain.addConsecutiveBone(new Vec2f((float)Math.cos(Math.toRadians(ang1)), (float)Math.sin(Math.toRadians(ang1))), HUMERUS);
        chain.addConsecutiveBone(new Vec2f((float)Math.cos(Math.toRadians(ang2)), (float)Math.sin(Math.toRadians(ang2))), ULNA);
        chain.addConsecutiveBone(new Vec2f((float)Math.cos(Math.toRadians(ang3)), (float)Math.sin(Math.toRadians(ang3))), HAND);

        return chain;
    }
}
