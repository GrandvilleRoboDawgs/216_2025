// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license f
package frc.robot;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
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
    DutyCycleEncoder elevatoreEncoder = new DutyCycleEncoder(5);
    DutyCycleEncoder wristEncoder = new DutyCycleEncoder(6);
    DutyCycleEncoder skullcrushEncoder = new DutyCycleEncoder(2);
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
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
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
  }

  @Override
  public void teleopPeriodic() {
    System.out.println("Wrist encoder: " + wristEncoder.get());
    System.out.println("Elevator encoder: " + elevatoreEncoder.get());

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
      Algae.set(.5);
    } else if (driver.getRawButton(PS4Controller.Button.kR2.value)) {
      Algae.set(-.5);
    }else {
      Algae.stopMotor();
    }
    if (operator.getRawButton(PS4Controller.Button.kTriangle.value)) {
      AlgaeArm.set(.25);
    }else if (operator.getRawButton(PS4Controller.Button.kCross.value)) {
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

//wrist buttons
    if (operator.getRawButton(PS4Controller.Button.kSquare.value)) {
    Wrist.set(.35);
    }else if (operator.getRawButton(PS4Controller.Button.kCircle.value)) {
      Wrist.set(-.35);
    }else {
      Wrist.set(.05);
    }

    //wrist encoder with PID
  //   if (operator.getRawButton(PS4Controller.Button.kSquare.value)) {
  //     Wrist.set(pid.calculate(wristEncoder.get(), 0.15));
  // }
    
    
//elevator buttons
        
if (operator.getRawButton(PS4Controller.Button.kL1.value)) {
  elevatorMotor.set(.50);
}
else if (operator.getRawButton(PS4Controller.Button.kL2.value)) {
    elevatorMotor.set(-.50);
} else{
  elevatorMotor.set(0.01);
}
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
    if (operator.getRawButton(PS4Controller.Button.kR2.value)) {
      
      Skullcrusher.set(.55);
    }else if (operator.getRawButton(PS4Controller.Button.kR1.value)) {
      Skullcrusher.set(-.55);
    }else {
      Skullcrusher.set(0.18);;
    }
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
