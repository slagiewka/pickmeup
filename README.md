# pickmeup
6DOF arm pick up software with inverse kinematics

## Installation & running
1. Clone the repository
2. Run `mvn install` (given Maven is installed).
3. Install `librxtx-java` (on Ubuntu) or `java-rxtx` (on Arch).
4. Run `ln -s /dev/ttyACM0 /dev/ttyS80`. Double-check if no other resource was previously using `/dev/ttyACM0`.
5. Run `ls -al /dev | grep ttyACM`, check the owning group (dialout or uucp).
6. Run `sudo usermod -aG <owning_group> <your_username>`.
7. Relog.
8. Run the application from your IDE.

## Inverse kinematics equations
![Alt text](/equations.png)
![Alt text](/robo.png)
## API
1. public Map<Integer, Double> calculateResults(double x, double z, double angle)

   calculates angles for servos  
   takes distance to the target(x), given height(z) and an angle to pick the item up at(angle)  
   returns Map with servo number and calculated angle  
2. public void setPosition(double x, double y, double height, double angle, short speed, boolean clench)

   sets up arm to given position  
   takes x and y coordinates of the target, given height, angle to pick the item up at, servo speed and boolean flag which is responsible for clenching  
