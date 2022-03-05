package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.commands.IntakeCommand.IntakeSubsytem;

public class OuterIntakeSubsystem extends SubsystemBase implements IntakeSubsytem {
    private final WPI_TalonSRX talon = new WPI_TalonSRX(IntakeConstants.OUTER_TALON_ID);

    public OuterIntakeSubsystem() {}

    public void intakeSet(double val) {
        System.out.println("Set outer intake " + val);
        talon.set(val * IntakeConstants.OUTER_POWER);
    }
}