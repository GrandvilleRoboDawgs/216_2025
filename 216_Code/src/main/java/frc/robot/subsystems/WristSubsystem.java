package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WristSubsystem extends SubsystemBase {
  private final DutyCycleEncoder wristEncoder;
  Spark wristMotor = new Spark(9);
  boolean wristMoving = false;
  boolean wristMovingdown = false;
  int targetLevel_checkerup;//for periodic function
  int targetLevel_checkerdown;
  double targetlevelup;
  double targetleveldown;

  public WristSubsystem() {
    wristEncoder = new DutyCycleEncoder(2);
  }

  @Override
  public void periodic() {
    // Useful for debugging
    System.out.println("Wrist Position: " + wristEncoder.get());
    if (wristMovingdown || wristMoving){
     if(isAtLevel(targetLevel2(targetLevel_checkerup))){
      wristMoving = false;
      stopWrist();
  } 
    else if(isAtLevel(targetLevel2(targetLevel_checkerdown))){
    wristMoving = false;
    stopWrist();
    }
  }
}

  public void moveWrist(double speed) {
    wristMotor.set(speed);
  }

  public void stopWrist() {
    wristMotor.set(.10);

  }

  public double getEncoderDistance() {
    return wristEncoder.get();
  }

  public void moveupToLevel(int targetlevelup) {
    targetLevel_checkerup = targetlevelup;
    if (!wristMoving) {
        wristMotor.set(.25);
        wristMoving = true;
        } 

  }
public void movedowntolevel(int targetleveldown){
  targetLevel_checkerdown = targetleveldown;
        if (!wristMovingdown) {
        wristMotor.set(-.25);
        wristMovingdown = true;
    
}}
  private boolean isAtLevel(double target) {
    double position = getEncoderDistance();
    return Math.abs(position - target) < 0.05; // Tolerance of ±0.05
  }
  private double targetLevel2(double Level) {
    if (Level == 0.0) {
        return 0.0;
    } else if (Level == 1.0) {
        return 2.24;
    } else if (Level == 2.0) {
        return 3.0;
    } else {
        // Handle invalid level (if necessary)
        throw new IllegalArgumentException("Invalid level: " + Level);
    }
}
}
