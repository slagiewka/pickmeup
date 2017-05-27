package pl.edu.agh.miss;

import driver.MapPoint;
import driver.SCIP;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import pl.edu.agh.miss.controllers.ArmController;
import pl.edu.agh.miss.controllers.impl.PololuArmController;
import tools.SerialPortHelper;

import java.io.IOException;
import java.util.List;

public class ArmControllerTestApplication {
    private static short PRODUCT_ID = 0x0089;
    private static short VENDOR_ID = 0x1ffb;
    private static float LASER_DIST = 9;

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException, IOException, InterruptedException {
        if(args.length == 2){
            PRODUCT_ID = Short.parseShort(args[0]);
            VENDOR_ID = Short.parseShort(args[1]);
        }

        MapPoint closest = getClosestPoint();

        double x = (closest.xValue() / 10) + LASER_DIST;
        double y = closest.yValue() / 10;
        System.out.println("x: " + x + ". y: " + y);
        double z = 7.0;
        short speed = 15;

        ArmController armController = new PololuArmController(PRODUCT_ID, VENDOR_ID);
        armController.setPosition(16, 0, 25, 0, speed, false);
        Thread.sleep(2000);
        armController.setPosition(x, y, z+2.0, -30, speed, false);
        Thread.sleep(2000);
        armController.setPosition(x, y, z, -30, speed, true);
        Thread.sleep(2000);
        armController.setPosition(16, 0, 25, 0, speed, true);
//        }
    }

    private static MapPoint getClosestPoint() throws PortInUseException, IOException, NoSuchPortException, UnsupportedCommOperationException {
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
        return closest;
    }


}
