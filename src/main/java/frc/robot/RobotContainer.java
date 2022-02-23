// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.ControllerConstants;
import frc.robot.commands.ArcadeDriveCmd;
import frc.robot.commands.BallTurnCmd;
import frc.robot.commands.ClimberControlCmd;
import frc.robot.commands.DriveCmd;
import frc.robot.commands.ReplayPosesCmd;
import frc.robot.commands.Turn180Command;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final Climber climber = new Climber();
  private final RelativeEncoder leftEncoder = climber.getLeftEncoder();
  private final RelativeEncoder rightEncoder = climber.getRightEncoder();
  private final XboxController stick = new XboxController(
    ControllerConstants.CONTROLLER_PORT
  );

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure driving command
    driveSubsystem.setDefaultCommand(
      new ArcadeDriveCmd(
        driveSubsystem, 
        () -> -stick.getLeftY(), 
        () -> stick.getLeftX(), 
        () -> stick.getRightX(),
        () -> stick.getRightTriggerAxis() > 0.1
      )
    );

    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(stick, XboxController.Button.kRightBumper.value)
      .whenPressed(new InstantCommand(driveSubsystem::resetGyroAngle));
    /*new JoystickButton(stick, XboxController.Button.kA.value)
      .whenPressed(new Turn180Command(driveSubsystem).withTimeout(5));
    new JoystickButton(stick, XboxController.Button.kB.value)
      .whileActiveOnce(new BallTurnCmd(driveSubsystem), true);*/
    new JoystickButton(stick, XboxController.Button.kY.value)
      .whileActiveOnce(new ClimberControlCmd(climber, 1));
    new JoystickButton(stick, XboxController.Button.kA.value)
      .whileActiveOnce(new ClimberControlCmd(climber, -1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      new DriveCmd(driveSubsystem, 0.5, 0, 0).withTimeout(1),
      new DriveCmd(driveSubsystem, 0, 0, 0.5).withTimeout(0.5),
      new DriveCmd(driveSubsystem, 0, 0, 0).withTimeout(0.5),
      new ReplayPosesCmd(driveSubsystem, true)
    );
  }
}
