// package frc.robot.subsystems;

// import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.config.SparkBaseConfig;
// import com.revrobotics.spark.config.SparkMaxConfig;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import com.revrobotics.AbsoluteEncoder;

// public class ElevatorSubsystem extends SubsystemBase {
//   private final SparkMax elevatorMotor;
//   private final AbsoluteEncoder elevatorEncoder;
//    boolean elevatormoving = false;
//    boolean elevatorMovingdown = false;
//    int targetLevel_checkerup;
//    int targetLevel_checkerdown;

//   public ElevatorSubsystem() {
//     elevatorMotor = new SparkMax(15, MotorType.kBrushless);
//     elevatorEncoder = elevatorMotor.getAbsoluteEncoder();
//     // AbsoluteEncoderConfig seAbsoluteEncoderConfig = new AbsoluteEncoderConfig();
//     // seAbsoluteEncoderConfig.positionConversionFactor(2);
//   }

//   @Override
//   public void periodic() {
//     // Useful for debugging
//     System.out.println("Elevator Position: " + elevatorEncoder.getPosition());
//     if (elevatorMovingdown || elevatormoving){
//       if(isAtLevel(leveltranslator(targetLevel_checkerup))){
//        elevatormoving = false;
//        stopElevator();
//    } 
//    //check if skull crusher has reached goal position
//      else if(isAtLevel(leveltranslator(targetLevel_checkerdown))){
//      elevatorMovingdown = false;
//      stopElevator();
//      }
//    }
//   }

//   public void moveElevator(double speed) {
//     elevatorMotor.set(speed);
//   }

//   public void stopElevator() {
//     SparkMaxConfig config_ = new SparkMaxConfig();
//     config_.idleMode(SparkBaseConfig.IdleMode.kBrake);
//     elevatorMotor.stopMotor();

//   }

//   public double getEncoderDistance() {
//     return elevatorEncoder.getPosition();
//   }
//   public boolean isItmoving() {
//     if (elevatorMovingdown||elevatormoving){
//       return true;
//     }else{
//       return false;
//     }
//   }
//   public void moveupToLevel(int targetLevel_up) {
//     targetLevel_checkerup = targetLevel_up;
//     if (!elevatormoving) {
//         elevatorMotor.set(.25);
//         elevatormoving = true;
//         } else if(isAtLevel(leveltranslator(targetLevel_up))){
//             elevatormoving = false;
//             stopElevator();
//     }
//   }
// public void movedowntolevel(int targetlevel3_down){
//   targetLevel_checkerdown = targetlevel3_down;
//         if (!elevatorMovingdown) {
//         elevatorMotor.set(-.25);
//         elevatorMovingdown = true;
//         } else if(isAtLevel(leveltranslator(targetlevel3_down))){
//             elevatorMovingdown = false;
//             stopElevator();
//     }

// }

//   private boolean isAtLevel(double target) {
//     double position = getEncoderDistance();
//     return Math.abs(position - target) < 0.05; // Tolerance of ±0.05
//   }
//   private double leveltranslator(int Level) {
//     if (Level == 0) {
//         return 0.0;
//     } else if (Level == 1) {
//         return 2.24;
//     } else if (Level == 2) {
//         return 3.0;
//     } else {
//         // Handle invalid level (if necessary)
//         throw new IllegalArgumentException("Invalid level: " + Level);
//     }
// }
// }
