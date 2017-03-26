package pl.edu.agh.miss.controllers.impl;

import com.iamcontent.device.controller.pololu.maestro.PololuMaestroServoCard;
import com.iamcontent.device.controller.pololu.maestro.usb.UsbPololuMicroMaestroServoCard;
import pl.edu.agh.miss.controllers.ArmController;
import pl.edu.agh.miss.ik.InverseKinematicsCalculator;
import pl.edu.agh.miss.ik.impl.Lynx6DOFInverseKinematicsCalculator;
import pl.edu.agh.miss.usb.ShowTopology;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.miss.usb.FindUsbDevice.getUsbDevicesWithId;

public class PololuArmController implements ArmController {
    private final short PRODUCT_ID;
    private final short VENDOR_ID;

    InverseKinematicsCalculator inverseKinematicsCalculator;

    public PololuArmController(short productId, short vendorId) {
        PRODUCT_ID = productId;
        VENDOR_ID = vendorId;
        inverseKinematicsCalculator = new Lynx6DOFInverseKinematicsCalculator();
    }

    public void setPosition(double x, double y, double z, double angle) {
        Map<Integer, Double> result = inverseKinematicsCalculator.calculateResults(x, y, z, angle);
        setServoPositions(result);
    }

    private void setServoPositions(Map<Integer, Double> positions){
        PololuMaestroServoCard servoCard = getServo();
        if(servoCard != null){
            for (Map.Entry<Integer, Double> channelValueEntry : positions.entrySet()) {
                short channel = channelValueEntry.getKey().shortValue();
                short value = channelValueEntry.getValue().shortValue();
                System.out.println("Channel: " + channel + ", value: " + value);
                servoCard.setRawPosition(channel, value);
            }
        }
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
