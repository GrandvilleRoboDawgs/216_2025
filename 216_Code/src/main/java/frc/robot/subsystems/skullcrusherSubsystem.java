package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class skullcrusherSubsystem extends SubsystemBase {
  private final DutyCycleEncoder skullcrushereEncoder;
  Spark Skullcrusher = new Spark(7);
   boolean skullcrusherMoving = false;
   boolean skullcrusherMovingdown = false;
  //to check whether or not skullcrusher has reached the goal position
   int targetLevel_checkerup;
   int targetLevel_checkerdown;
  //to convert the function parameters to usable values
   double targetleveldown;
   double targetlevelup;

  public skullcrusherSubsystem() {
    skullcrushereEncoder = new DutyCycleEncoder(4);
  }

  @Override
  public void periodic() {
    // Useful for debugging
    System.out.println("Skullcrusher Position: " + skullcrushereEncoder.get());
      //check if the skull crusher has reached the desired position
    if (skullcrusherMovingdown || skullcrusherMoving){
      if(isAtLevel(targetLevel2(targetLevel_checkerup))){
       skullcrusherMoving = false;
       stopSkullcrusher();
   } 
 
     else if(isAtLevel(targetLevel2(targetLevel_checkerdown))){
     skullcrusherMovingdown = false;
     stopSkullcrusher();
     }
   }
  }
  public boolean isItmoving() {
    if (skullcrusherMoving||skullcrusherMovingdown){
      return true;
    }else{
      return false;
    }
  }
  public void moveSkullcrusher(double speed) {
    Skullcrusher.set(speed);
  }

  public void stopSkullcrusher() {
    // Skullcrusher.kBrake;
    // Skullcrusher.stopMotor();
    Skullcrusher.set(10);

  }

  public double getEncoderDistance() {
    return skullcrushereEncoder.get();
  }

  public void moveupToLevel(int targetlevelup) {
    targetLevel_checkerup = targetlevelup;
    if (!skullcrusherMoving) {
        Skullcrusher.set(.75);
        skullcrusherMoving = true;
        } else if(isAtLevel(targetLevel2(targetlevelup))){
          targetLevel_checkerdown = targetlevelup;
            skullcrusherMoving = false;
            stopSkullcrusher();
    }
  }
public void movedowntolevel(int targetleveldown){
  targetLevel_checkerdown = targetleveldown;
        if (!skullcrusherMovingdown) {
        Skullcrusher.set(-.75);
        skullcrusherMovingdown = true;
        } else if(isAtLevel(targetLevel2(targetleveldown))){
            skullcrusherMoving = false;
            stopSkullcrusher();
    }
    
}
  private boolean isAtLevel(double target) {
    double position = getEncoderDistance();
    return Math.abs(position - target) < 0.05; // Tolerance of ±0.05
  }
  private double targetLevel2(double Level) {
    if (Level == 0.0) {
        return 8.0;
    } else if (Level == 1.0) {
        return 12.24;
    } else if (Level == 2.0) {
        return 17.0;
    } else {
        // Handle invalid level (if necessary)
        throw new IllegalArgumentException("Invalid level: " + Level);
    }
}
}

