package first.robot.opmode;

import static org.wpilib.units.Units.Seconds;

import first.robot.Robot;
import org.wpilib.command3.Command;
import org.wpilib.command3.Scheduler;
import org.wpilib.opmode.PeriodicOpMode;

class DriveBackThenShoot extends PeriodicOpMode {
  private final Robot robot;

  /** The Robot instance is passed into the opmode via the constructor. */
  public DriveBackThenShoot(Robot robot) {
    this.robot = robot;
  }

  public Command autoCommand() {
    return Command.noRequirements(
            coroutine -> {
              coroutine.await(
                  robot.drivetrain.arcadeDrive(() -> -0.2, () -> 0).withTimeout(Seconds.of(2)));
              coroutine.awaitAll(robot.intakeLauncher.shoot(), robot.feeder.feed());
            })
        .named("DriveBackThenShoot");
  }

  @Override
  public void start() {
    Scheduler.getDefault().schedule(autoCommand());
  }
}
