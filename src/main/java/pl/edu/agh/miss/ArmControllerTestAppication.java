package pl.edu.agh.miss;

import pl.edu.agh.miss.controllers.ArmController;
import pl.edu.agh.miss.controllers.impl.PololuArmController;

public class ArmControllerTestAppication {
    private static short PRODUCT_ID = 123;
    private static short VENDOR_ID = 123;

    public static void main(String[] args) {
        if(args.length == 2){
            PRODUCT_ID = Short.parseShort(args[0]);
            VENDOR_ID = Short.parseShort(args[1]);
        }
        ArmController armController = new PololuArmController(PRODUCT_ID, VENDOR_ID);
        armController.setPosition(4, 4, 4, 90);


    }
}
