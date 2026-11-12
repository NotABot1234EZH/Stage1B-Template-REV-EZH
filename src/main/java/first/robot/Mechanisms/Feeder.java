package first.robot.Mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import first.robot.simulation.SingleFlywheelSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

public class Feeder implements Mechanism {
  private SparkMax motor = new SparkMax(CANPort.CAN_S0, 5, MotorType.kBrushless);

  private final SingleFlywheelSim sim = SingleFlywheelSim.forFeeder(motor);

  public void periodic() { // Update the simulation
    if (RobotBase.isSimulation()) {
      sim.periodic();
    }
  }

  public Command feed() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(0.75); // Set the motor to full power
            coroutine.yield(); // Yield to allow other commands to run
          }
        })
        .named("feed");
  }

  public Command intake() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(-1); // Set the motor to full power in reverse
            coroutine.yield(); // Yield to allow other commands to run
          }
        })
        .named("intake");
  }

  public Command outtake() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(1); // Set the motor to full power in the forward direction
            coroutine.yield(); // Yield to allow other commands to run
          }
        })
        .named("outtake");
  }

  public Command idle() {
    return run(coroutine -> {
          while (true) {
            motor.setThrottle(0); // Set the motor to idle
            coroutine.yield(); // Yield to allow other commands to run
          }
        })
        .named("idle");
  }

  public Feeder() {
    setDefaultCommand(idle());
  }
}
