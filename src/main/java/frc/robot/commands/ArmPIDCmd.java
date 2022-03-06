package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;

public class ArmPIDCmd extends CommandBase {
    public enum Direction {
        kUp(0),
        kDown(1);

        public final int value;

        Direction(int value) {
            this.value = value;
        }
    }

    private final ArmSubsystem arm;
    private final PIDController pidController;

    public ArmPIDCmd(ArmSubsystem arm, Direction d) {
        this.arm = arm;
        addRequirements(arm);
        this.pidController = new PIDController(0.5, 0, 0);
        pidController.setSetpoint(directionToSetpoint(d));
    }

    private double directionToSetpoint(Direction d) {
        switch(d) {
            case kUp:
                return -0.8;
            case kDown:
                return 0;
        }
        throw new AssertionError("Unexpected direction");
    }

    @Override
    public void execute() {
        double in = arm.getEncoder().getPosition();
        double out = pidController.calculate(in);
        System.out.println("[Arm] In: " + in + ", out: " + out);
        arm.set(out);
    }

    @Override
    public boolean isFinished() {
        return pidController.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        arm.set(0);
    }
}
