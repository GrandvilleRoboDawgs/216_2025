// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license f
package frc.robot;

import static edu.wpi.first.units.Units.Value;

import javax.lang.model.util.ElementScanner14;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.SparkBase;
// import com.revrobotics.spark.SparkFlex;
// import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;



public class Robot extends TimedRobot {
  private final Joystick operator = new Joystick(0);
  private final Joystick driver = new Joystick(1);
  Spark Piranha = new Spark(8);
  Spark Wrist = new Spark(9);
  Spark Skullcrusher = new Spark(7);
  Spark Algae = new Spark(6);
  TalonFX Torquer = new TalonFX(13);
  TalonFX AlgaeArm = new TalonFX(14);
  SparkMax elevatorMotor;


  // // private CANSparkMax Elevator;
  // SparkMax Elevator = new SparkMax(15, MotorType.kBrushless);
    //Encoders
    Encoder skullcrushEncoder = new Encoder(0,1);
    Encoder elevatorEncoder = new Encoder(2, 3);
    Encoder wristEncoder = new Encoder(4, 5);
  private Command m_autonomousCommand;
  // private final Joystick operator = new Joystick(0);
  // private final Joystick driver = new Joystick(1);
  // private final Drivetrain m_swerve = new Drivetrain();
  private final RobotContainer m_robotContainer;

  public Robot() {
    m_robotContainer = new RobotContainer();
    elevatorMotor = new SparkMax(15,MotorType.kBrushless);
    SparkMaxConfig globalConfig = new SparkMaxConfig();
    globalConfig.smartCurrentLimit(50).idleMode(IdleMode.kBrake);
    elevatorMotor.configure(globalConfig,ResetMode.kResetSafeParameters,PersistMode.kPersistParameters);
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
    if (operator.getRawButton(PS4Controller.Button.kTriangle.value)) {
      AlgaeArm.set(.55);
    }else if (operator.getRawButton(PS4Controller.Button.kCross.value)) {
     AlgaeArm.set(-.55); 
    }else {
      AlgaeArm.stopMotor();
    }
    
//coral buttons
    if (driver.getRawButton(PS4Controller.Button.kL2.value)) {
      Piranha.set(.50);
    }else if (driver.getRawButton(PS4Controller.Button.kL1.value)) {
      Piranha.set(-.50);
    }else {
      Piranha.stopMotor();
    }

//wrist buttons
    if (operator.getRawButton(PS4Controller.Button.kSquare.value)) {
    Wrist.set(.75);
    }else if (operator.getRawButton(PS4Controller.Button.kCircle.value)) {
      Wrist.set(-.75);
    }else {
      Wrist.stopMotor();
    }

//elevator buttons
    if (operator.getRawButton(PS4Controller.Button.kL1.value)) {
      elevatorMotor.set(.75);
          }else if (operator.getRawButton(PS4Controller.Button.kL2.value)) {
      elevatorMotor.set(-.75);
    }// Timer timer2 = new Timer();
      // timer2.start();
      
      // while (timer2.get() < 2) {
      //   Elevator.set(.25);
      //   wait(50);
      // }
      
      // Elevator.stopMotor();
      // timer2.stop();
      // timer2.reset();
else {
      elevatorMotor.stopMotor();
    }

//skull crusher buttons
    if (operator.getRawButton(PS4Controller.Button.kR1.value)) {
      
      Skullcrusher.set(-.55);
      // Timer timer3 = new Timer();
      // timer3.start();
  
      // while (timer3.get() < 2) {
      //   Skullcrusher.set(.25);
      //   wait(50);
      // }
  
      // Skullcrusher.stopMotor();
      // timer3.stop();
      // timer3.reset();
    }else if (operator.getRawButton(PS4Controller.Button.kR2.value)) {
      Skullcrusher.set(.55);
    }else {
      Skullcrusher.stopMotor();
    }

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
