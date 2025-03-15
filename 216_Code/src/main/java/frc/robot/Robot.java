// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license f
package frc.robot;
import org.opencv.core.Mat.Atable;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import edu.wpi.first.wpilibj.Timer;
// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.SparkBase;
// import com.revrobotics.spark.SparkFlex;
// import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.math.controller.PIDController;



public class Robot extends TimedRobot {
  boolean starttimer = false;
  int wristlevel = 0;
  Timer wristtTimer = new Timer();
  //constants for arm/wrsit
  double[] armPos = {2.55, 2.7};
  double[] wristPos = {1.28, 1.8, 2.2, 2.5};
  double[] elevatorPos = {5, 4000, 10662};
  double wTarget = wristPos[0];
  double aTarget = armPos[0];
  double eTarget = elevatorPos[0];
  double kPWrist = 1;
  UsbCamera camera1;
  NetworkTableEntry cameraSelection;
  private final Joystick operator = new Joystick(0);
  private final Joystick driver = new Joystick(1);
  Spark Piranha = new Spark(8);
  Spark Wrist = new Spark(9);
  Spark Skullcrusher = new Spark(7);
  Spark Algae = new Spark(6);
  TalonFX Torquer = new TalonFX(13);
  TalonFX AlgaeArm = new TalonFX(14);
  SparkMax elevatorMotor;
  // constants for PID
  double kP = 0.1;
  double Ki = 0;
  double Kd = 0;
  //encoders
    private final Encoder elevatorEncoder = new Encoder(3, 4, false, Encoder.EncodingType.k2X);//normally 4,5
    //absolute encoder
    private final DutyCycleEncoder wristEncoder = new DutyCycleEncoder(5, 4.0, 0.0);
    private final DutyCycleEncoder skullcrushEncoder = new DutyCycleEncoder(9,4.0,0.0);
    //auton selector
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Command m_autonomousCommand;
  // private final Joystick operator = new Joystick(0);
  // private final Joystick driver = new Joystick(1);
  // private final Drivetrain m_swerve = new Drivetrain();
  private final RobotContainer m_robotContainer;
  PIDController pid = new PIDController(kP, Ki, Kd);

  public Robot() {
    //elevatorEncoder.reset();
    m_robotContainer = new RobotContainer();
    elevatorMotor = new SparkMax(15,MotorType.kBrushless);
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    globalConfig.smartCurrentLimit(50).idleMode(IdleMode.kBrake);
    elevatorMotor.configure(globalConfig,ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);
    CameraServer.startAutomaticCapture();
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    // initializing array
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
    //SmartDashboard.putNumber("Elevator", wristEncoder.get());
    SmartDashboard.putNumber("Elevator Encoder", elevatorEncoder.getDistance());
    SmartDashboard.putNumber("Wrist Encoder", wristEncoder.get());
    SmartDashboard.putNumber("Arm Encoder", skullcrushEncoder.get());
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }


  }

  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }

  }

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    } 
    elevatorEncoder.reset();
   }

  @Override
  public void teleopPeriodic() {
    // double currArmPos = skullcrushEncoder.get();
    // double currWirstPos = wristEncoder.get();
  
    System.out.println("Wrist encoder: " + wristEncoder.isConnected());
    System.out.println("skullcrusher Encoder: " + skullcrushEncoder.isConnected());
    System.out.println("Wrist encoder: " + wristEncoder.get());
    System.out.println("skullcrusher Encoder: " + skullcrushEncoder.get());
    System.out.println("Elevator Encoder: " + elevatorEncoder.getDistance());
    double currWristPos = wristEncoder.get();
    double wristError = currWristPos - wTarget;
    double currArmPos = skullcrushEncoder.get();
    double currElevPos = elevatorEncoder.getDistance();
    //torquer
    if (driver.getRawButton(PS4Controller.Button.kCircle.value)){
      Torquer.set(.25);
    }else if (driver.getRawButton(PS4Controller.Button.kSquare.value)){
      Torquer.set(-.25);
    }else {
      Torquer.stopMotor();
    }
//algae buttons
    if (driver.getRawButton(PS4Controller.Button.kR1.value)) {
      Algae.set(.9);
    } else if (driver.getRawButton(PS4Controller.Button.kR2.value)) {
      Algae.set(-.9);
    }else {
      Algae.stopMotor();
    }
    if (operator.getRawButton(PS4Controller.Button.kL1.value)) {
      AlgaeArm.set(.25);
    }else if (operator.getRawButton(PS4Controller.Button.kL2.value)) {
      AlgaeArm.set(-.25); 
    }else {
      AlgaeArm.stopMotor();
    }
    
//coral buttons
    if (driver.getRawButton(PS4Controller.Button.kTriangle.value)) {
      Piranha.set(.50);
    }else if (driver.getRawButton(PS4Controller.Button.kL2.value)) {
      Piranha.set(-.50);
    }else {
      Piranha.stopMotor();
    }

    if (operator.getRawButton(PS4Controller.Button.kTriangle.value)){
      //loading
      wTarget = wristPos[1];
      aTarget = armPos[0];
      eTarget = elevatorPos[0];
    } else if (operator.getRawButton(PS4Controller.Button.kSquare.value)){
      //passive
      wTarget = wristPos[0];
      aTarget = armPos[0];
      eTarget = elevatorPos[0];
    } else if (operator.getRawButton(PS4Controller.Button.kCross.value)){
      //L2
      wTarget = wristPos[3];
      aTarget =  armPos[0];
      eTarget = elevatorPos[1];
    } else if (operator.getRawButton(PS4Controller.Button.kCircle.value)){
      //L1
      wTarget = wristPos[2];
      aTarget = armPos[0];
      eTarget = elevatorPos[0];
    } else if (operator.getRawButton(PS4Controller.Button.kR1.value)){
      //l3
      wTarget = wristPos[3];
      aTarget = armPos[0];
      eTarget = elevatorPos[2];
    }

    if(wTarget - .05 > currWristPos){
      Wrist.set(wristError *kPWrist);
    } else if (wTarget + .05 < currWristPos){
      Wrist.set(-wristError *kPWrist);
    } else {
      Wrist.stopMotor();
    }

    if(eTarget - 50 > currElevPos){
      elevatorMotor.set(-.5); //down
    } else if(eTarget + 50 < currElevPos){
      elevatorMotor.set(.5); //up
    } else{
      elevatorMotor.stopMotor(); 
    }

    if(aTarget - .1 > currArmPos){
      Skullcrusher.set(-.5);
    } else if(aTarget + .1 < currArmPos){
      Skullcrusher.set(.5);
    } else {
      Skullcrusher.set(.15);
    }



    // if (operator.getRawButton(PS4Controller.Button.kSquare.value)){
    // if (wristtTimer.get() < .3){
    //   if (starttimer){
    //     wristlevel = 1;
    //     wristtTimer.start();
    //     starttimer = false;
        
    //    } Wrist.set(.25);
    //   // } else if (wristtTimer.get() >= 1 && wristtTimer.get() < 1.5){
    //   //   Wrist.set(.15);
    //   // }
    // }
    // if (wristlevel == 1){
    //   if (wristtTimer.get() < .21 && starttimer == true){
    //     starttimer = false;
    //     Wrist.set(.25);
    //   } else{
    //     Wrist.set(-.05);
    //   }
    // }

    // if (wTarget - .1 > currWirstPos){
    //   Wrist.set(.3);
    // } else if(wTarget + .1 < currWirstPos){
    //   Wrist.set(-.3);
    // } else {
    //   Wrist.stopMotor();
    // }
      // System.out.println("Wrist encoder: " + wristEncoder.get());

    // if(aTarget - .02 < currArmPos){
    //   Skullcrusher.set(.5);
    // } else if(aTarget + .02 > currArmPos){
    //   Skullcrusher.set(-.5);
    // } else {
    //   Skullcrusher.stopMotor();
    // }

//wrist buttons
    // if (operator.getRawButton(PS4Controller.Button.kSquare.value)) {
    //   Wrist.set(.35);
    //   //   if (wristEncoder.get() < 0.1 ) {
    //   //     Wrist.set(.3);
    //   // } else if (wristEncoder.get() >= 0.1) {
    //   //     Wrist.set(.2);
    //   // } else if (wristEncoder.get() > 0.3 && wristEncoder.get() < 0.5) {
    //   //     Wrist.set(0.05);
    //   // }
    // }else if (operator.getRawButton(PS4Controller.Button.kCircle.value)) {
    //   Wrist.set(-.35);
    // }else {
    //   Wrist.set(.05);
    // }

    //wrist encoder with PID
  //   if (operator.getRawButton(PS4Controller.Button.kSquare.value)) {
  //     Wrist.set(pid.calculate(wristEncoder.get(), 0.15));
  // }
    
   // arm pos 1
    // if (operator.getRawButton(PS4Controller.Button.kTriangle.value)){
    //   if (  < .1 ) {
    //     Wrist.set(.85);
    // } else if (offsetValueEncoderWrist > .13) {
    //     Wrist.set(.2);
    // } else if (offsetValueEncoderWrist > .135 && offsetValueEncoderWrist < .145) {
    //     Wrist.set(0.05);
    // }
    //   if (elevatorEncoder.get() < 1000){
    //     elevatorMotor.set(-.5); //elevator is opposite for whatever reason
    //   }else if (elevatoreEncoder.get() > 1200){
    //     elevatorMotor.set(-.25);
    //   } else if (elevatoreEncoder.get() >1000 && elevatoreEncoder.get() < 1400){
    //     elevatorMotor.set(.05);
    //   }
    //   if (skullcrushEncoder.get() < .3){
    //     Skullcrusher.set(.75);
    //   }else if (skullcrushEncoder.get() > .45){
    //     Skullcrusher.set(.4);
    //   } else if (skullcrushEncoder.get() > .6){
    //     Skullcrusher.set(.18);
    //   }
    // }
//elevator buttons
        
// if (operator.getRawButton(PS4Controller.Button.kL1.value)) {
//   elevatorMotor.set(.50);
// } else if (operator.getRawButton(PS4Controller.Button.kL2.value)) {
//     elevatorMotor.set(-.50);
// } else{
//   elevatorMotor.stopMotor();
// }
//     if (operator.getRawButton(PS4Controller.Button.kL1.value)) {
//         if (elevatorEncoder.get() < 4 ) {
//           elevatorMotor.set(-.7);
//       } else if (elevatorEncoder.get() >= 7) {
//           elevatorMotor.set(-.4);
//       } else if (elevatorEncoder.get() > 0.3 && elevatorEncoder.get() < 0.5) {
//           elevatorMotor.set(-0.01);
//       }
//     }else if (operator.getRawButton(PS4Controller.Button.kCircle.value)) {
//       Wrist.set(-.35);
//     }else {
//       Wrist.set(.05);
//     }

//elevator with PID:
  //   if (operator.getRawButton(PS4Controller.Button.kL1.value)) {
  //     elevatorEncoder.set(pid.calculate(elevatorEncoder.get(), 100));
  // }
// if (operator.getRawButton(PS4Controller.Button.kTriangle.value)) {
//   if (elevator_encoder.get() < 19000 ) {
//     elevatorMotor.set(.85);
// } else if (elevator_encoder.get() > 19001 && elevator_encoder.get() < 19500) {
//     elevatorMotor.set(.5);
// } else if (elevator_encoder.get() > 20080) {
//     elevatorMotor.set(0.0);
// }
// }

//skull crusher buttons
    // if (operator.getRawButton(PS4Controller.Button.kR2.value)) {
      
    //   Skullcrusher.set(.55);
    // }else if (operator.getRawButton(PS4Controller.Button.kR1.value)) {
    //   Skullcrusher.set(-.55);
    // }else {
    //   Skullcrusher.set(0.18);;
    // }
    //   //skullcrusher encoder
    //   if (operator.getRawButton(PS4Controller.Button.kR2.value)) {
    //     if (skullcrushEncoder.get() < 0.1 ) {
    //       Skullcrusher.set(.85);
    //   } else if (skullcrushEncoder.get() > 0.2 && skullcrushEncoder.get() < 0.3) {
    //       Wrist.set(.5);
    //   } else if (skullcrushEncoder.get() > 0.5) {
    //       Wrist.set(0.0);
    //   }
    // }

    //skullcrusher encoder with PID
  //   if (operator.getRawButton(PS4Controller.Button.kR2.value)) {
  //     Skullcrusher.set(pid.calculate(skullcrushEncoder.get(), 0.15));
  // }
    
  }
  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}

}
