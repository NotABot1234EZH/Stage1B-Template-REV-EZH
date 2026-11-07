 package first.robot.Mechanisms;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import first.robot.simulation.SingleFlywheelSim;
import static org.wpilib.units.Units.Seconds;

 public class IntakeLauncher implements Mechanism {
      private SparkMax motor = new SparkMax(CANPort.CAN_S0, 4, MotorType.kBrushless);

      private final SingleFlywheelSim sim = SingleFlywheelSim.forIntakeLauncher(motor);

      public void periodic() { // Update the simulation
       if (RobotBase.isSimulation()) {
       sim.periodic();
      }
    }

      public Command shoot() {
        return run (coroutine-> {
          while (true) {
            coroutine.wait(Seconds.of(2.0)); // Wait for 2 seconds before shooting
            motor.setThrottle(0.9); // Set the motor to full power in the forward direction
            coroutine.yield(); // Yield to allow other commands to run
          }
        }).named("shoot");
      }

      public Command intake() {
        return run (coroutine-> {
          while (true) {
            motor.setThrottle(0.8); // Set the motor to 80% power backwards
            coroutine.yield(); // Yield to allow other commands to run
          }
        }).named("intake");
      }

      public Command outtake() {
        return run (coroutine-> {
          while (true) {
            motor.setThrottle(-0.8); // Set the motor to 80% power forwards
            setDefaultCommand(idle());
            coroutine.yield(); // Yield to allow other commands to run
          }
        }).named("idle");
     }
      public Command idle() {
        return run (coroutine-> {
          while (true) {
            motor.setThrottle(0); // Set the motor to idle
            coroutine.yield(); // Yield to allow other commands to run
          }
        }).named("idle");
      }

      public IntakeLauncher() {
        setDefaultCommand(idle());
      }
  }