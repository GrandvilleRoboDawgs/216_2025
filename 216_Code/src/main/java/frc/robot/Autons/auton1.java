// package frc.robot.Autons;

// import frc.robot.Constants;
// import frc.robot.Constants.Swerve;
// import frc.robot.subsystems.CommandSwerveDrivetrain;

// import java.util.List;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.controller.ProfiledPIDController;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.math.trajectory.Trajectory;
// import edu.wpi.first.math.trajectory.TrajectoryConfig;
// import edu.wpi.first.math.trajectory.TrajectoryGenerator;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;

// public class auton1 extends SequentialCommandGroup {
//     public auton1(Swerve s_Swerve){
//         TrajectoryConfig config =
//             new TrajectoryConfig(
//                     Constants.AutoConstants.kMaxSpeedMetersPerSecond,
//                     Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
//                 .setKinematics(Constants.Swerve.swerveKinematics);

//         // An example trajectory to follow.  All units in meters.
//         Trajectory exampleTrajectory =
//             TrajectoryGenerator.generateTrajectory(
//                 // Start at the origin facing the +X direction
//                 new Pose2d(0, 0, new Rotation2d(0)), // 2.2, -.6
//                 // Pass through these two interior waypoints, making an 's' curve path
//                 List.of(new Translation2d(6.5, 3.85), new Translation2d(7.24, 4.9)),
//                 // End 3 meters straight ahead of where we started, facing forward
//                 new Pose2d(5, 5, new Rotation2d(45)),
//                 config);
//         Trajectory traj1 = TrajectoryGenerator.generateTrajectory(
//             new Pose2d(0,0, new Rotation2d(0)), 
//             List.of(new Translation2d(1,-1.5), 
//             new Translation2d(1,-1.5)),
//             new Pose2d(1,-1.5, new Rotation2d(0)), config);
//         var thetaController =
//             new ProfiledPIDController(
//                 Constants.AutoConstants.kPThetaController, 0, 0, Constants.AutoConstants.kThetaControllerConstraints);
//         thetaController.enableContinuousInput(-Math.PI, Math.PI);

//         SwerveControllerCommand swerveControllerCommand =
//             new SwerveControllerCommand(
//                 exampleTrajectory,
//                 s_Swerve::getPose,
//                 Constants.Swerve.swerveKinematics,
//                 new PIDController(Constants.AutoConstants.kPXController, 0, 0),
//                 new PIDController(Constants.AutoConstants.kPYController, 0, 0),
//                 thetaController,
//                 s_Swerve::setModuleStates,
//                 s_Swerve);
// SwerveControllerCommand swerveControlCommand1 =
//             new SwerveControllerCommand(
//                 traj1,
//                 s_Swerve::getPose,
//                 Constants.Swerve.swerveKinematics,
//                 new PIDController(Constants.AutoConstants.kPXController, 0, 0),
//                 new PIDController(Constants.AutoConstants.kPYController, 0, 0),
//                 thetaController,
//                 s_Swerve::setModuleStates,
//                 s_Swerve);

//         addCommands(
//             new InstantCommand(() -> s_Swerve.setPose(exampleTrajectory.getInitialPose())),
//             swerveControllerCommand
//         );

        
//     }
// }