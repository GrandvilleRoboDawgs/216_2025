// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    
    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandPS4Controller joystick = new CommandPS4Controller(1);
    // private final CommandPS4Controller operator = new CommandPS4Controller(1);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        joystick.R3().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.L3().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.share().and(joystick.triangle()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.share().and(joystick.square()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.options().and(joystick.triangle()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.options().and(joystick.square()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.L1().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command redCommand() {
        //return Commands.print("No autonomous command configured");
        return new SequentialCommandGroup(
            drivetrain.applyRequest(()->
            // drive.withVelocityX(.25*MaxSpeed) //blue
            drive.withVelocityX(-.25*MaxSpeed) //red
            .withVelocityY(0)
            .withRotationalRate(0)).withTimeout(1.225),
            drivetrain.applyRequest(()->
            drive.withVelocityX(0)
            .withVelocityY(0)
            .withRotationalRate(0)).withTimeout(9.5),
            drivetrain.applyRequest(()->
            drive.withVelocityX(.1*MaxSpeed)
            .withVelocityY(0)
            .withRotationalRate(.15*MaxAngularRate)).withTimeout(2));

    }
    
    public Command blueCommand() {
        //return Commands.print("No autonomous command configured");
        return new SequentialCommandGroup(
            drivetrain.applyRequest(()->
            // drive.withVelocityX(.25*MaxSpeed) //blue
            drive.withVelocityX(.25*MaxSpeed) //red
            .withVelocityY(0)
            .withRotationalRate(0)).withTimeout(1.225),
            drivetrain.applyRequest(()->
            drive.withVelocityX(0)
            .withVelocityY(0)
            .withRotationalRate(0)).withTimeout(9.5));    }
    public Command redLeftAuto() {
        //return Commands.print("No autonomous command configured");
        return new SequentialCommandGroup(
            drivetrain.applyRequest(()->
            // drive.withVelocityX(.25*MaxSpeed) //blue
            drive.withVelocityX(-.35*MaxSpeed) //red
            .withVelocityY(0)
            .withRotationalRate(0.25 *MaxAngularRate)).withTimeout(1.225),
            drivetrain.applyRequest(()->
            drive.withVelocityX(-0.2*MaxSpeed)
            .withVelocityY(0)
            .withRotationalRate(-.1*MaxAngularRate)).withTimeout(0.6));    }
    
}
