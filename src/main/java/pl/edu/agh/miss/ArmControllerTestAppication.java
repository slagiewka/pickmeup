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

public class ArmControllerTestAppication {
    private static short PRODUCT_ID = 123;
    private static short VENDOR_ID = 123;

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException, IOException {
        if(args.length == 2){
            PRODUCT_ID = Short.parseShort(args[0]);
            VENDOR_ID = Short.parseShort(args[1]);
        }
        ArmController armController = new PololuArmController(PRODUCT_ID, VENDOR_ID);
        armController.setPosition(4, 4, 4, 90);

        SerialPort hokuyoSerialPort = SerialPortHelper.getHokuyoSerialPort("/dev/ttyS8");
        SCIP scip = new SCIP(hokuyoSerialPort.getInputStream(), hokuyoSerialPort.getOutputStream());
        scip.laserOn();

        MapPoint closest = new MapPoint(1000.0, 0.0, 0);
        List<MapPoint> pointList = scip.singleScan();
        for (MapPoint point : pointList) {
            if (point.getDistance() > 15.0 && point.getDistance() < closest.getDistance()) {
                closest = point;
            }
        }
    }
}
