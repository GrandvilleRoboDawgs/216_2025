// package frc.robot.subsystems;

// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableInstance;

// public class limelight {
//     private final NetworkTable limelightTable;

//     public limelight() {
//         limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
//     }

//     public boolean hasTarget() {
//         return limelightTable.getEntry("tv").getDouble(0) == 1;
//     }

//     public double getXOffset() {
//         return limelightTable.getEntry("tx").getDouble(0.0);
//     }

//     public double getYOffset() {
//         return limelightTable.getEntry("ty").getDouble(0.0);
//     }
    
// }