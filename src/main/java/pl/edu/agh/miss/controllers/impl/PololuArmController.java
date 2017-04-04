package pl.edu.agh.miss.controllers.impl;

import com.iamcontent.device.controller.pololu.maestro.PololuMaestroServoCard;
import com.iamcontent.device.controller.pololu.maestro.usb.UsbPololuMicroMaestroServoCard;
import pl.edu.agh.miss.controllers.ArmController;
import pl.edu.agh.miss.ik.InverseKinematicsCalculator;
import pl.edu.agh.miss.ik.impl.Lynx6DOFInverseKinematicsCalculator;
import pl.edu.agh.miss.usb.ShowTopology;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.miss.usb.FindUsbDevice.getUsbDevicesWithId;

public class PololuArmController implements ArmController {
    private final short PRODUCT_ID;
    private final short VENDOR_ID;
    private final short X_RANGE = 20;
    private final short Y_RANGE = 20;
    private final short ARM_RANGE = 20;

    InverseKinematicsCalculator inverseKinematicsCalculator;

    public PololuArmController(short productId, short vendorId) {
        PRODUCT_ID = productId;
        VENDOR_ID = vendorId;
        inverseKinematicsCalculator = new Lynx6DOFInverseKinematicsCalculator();
    }

    public void setPosition(double x, double y, double z, double angle) {
        if(Math.abs(x) > X_RANGE || Math.abs(y) > Y_RANGE || x*x + y*y > ARM_RANGE*ARM_RANGE || z < 0){
            System.out.println("Target unreachable. Out of range");
            return;
        }

        Map<Integer, Double> result = new LinkedHashMap<>();
        double width = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        result.put(0, calculateBaseRotationAngle(x,y));
        double height = z;
        result.putAll(inverseKinematicsCalculator.calculateResults(width, height, angle));
        setServoPositions(result);
    }

    private double calculateBaseRotationAngle(double x, double y) {
        return Math.atan2(y, x) * 180 / Math.PI;
    }

    private void setServoPositions(Map<Integer, Double> positions){
        PololuMaestroServoCard servoCard = getServo();

        if(servoCard != null){
            for (Map.Entry<Integer, Double> channelValueEntry : positions.entrySet()) {
                short channel = channelValueEntry.getKey().shortValue();

                short value = calculateServoPosition(channel, channelValueEntry.getValue());
                System.out.println("Channel: " + channel + ", value: " + value);
                servoCard.setRawPosition(channel, value);
            }
        }
    }

    private short calculateServoPosition(short channel, double angle){
        short DIVIDER = 0;
        short SUBTRAHEND = 0;
        switch(channel){
            case 0:
            case 3:
                DIVIDER = 45;
                break;
            case 1:
                DIVIDER = 60;
                break;
            case 2:
                DIVIDER = 55;
                SUBTRAHEND = 45;
                break;
        }
        return (short)((angle - SUBTRAHEND) * 500 / DIVIDER + 1500);
    }


    private PololuMaestroServoCard getServo(){
        UsbHub virtualRootUsbHub = ShowTopology.getVirtualRootUsbHub();
        List<UsbDevice> usbDevices = null;

		/* This will recursively search for all devices with the specified vendor and product id. */
        usbDevices = getUsbDevicesWithId(virtualRootUsbHub, VENDOR_ID, PRODUCT_ID);

        if(usbDevices.size() == 1){
            return new UsbPololuMicroMaestroServoCard(usbDevices.get(0));
        } else {
            System.out.println("Device not found");
            return null;
        }
    }
}
