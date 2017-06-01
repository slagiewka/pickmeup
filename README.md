# pickmeup
6DOF arm pick up software with inverse kinematics

## Installation & runnig
1. Clone the repository
2. Run `mvn install` (given Maven is installed).
3. Install `librxtx-java` (on Ubuntu) or `java-rxtx` (on Arch).
4. Run `ln -s /dev/ttyACM0 /dev/ttyS80`. Double-check if no other resource was previously using `/dev/ttyACM0`.
5. Run `ls -al /dev | grep ttyACM`, check the owning group (dialout or uucp).
6. Run `sudo usermod -aG <owning_group> <your_username>`.
7. Relog.
8. Run the application from your IDE.
