/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.Scheduler;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.math.util.Units;
import org.wpilib.math.util.Units.*;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import static org.wpilib.units.Units.Seconds;

import first.robot.simulation.SingleFlywheelSim;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot {
  public class Feeder implements Mechanism {
    private SparkMax motor = new SparkMax(CANPort.CAN_S0, 5, MotorType.kBrushless);

    private final SingleFlywheelSim sim = SingleFlywheelSim.forFeeder(motor);

    public void periodic() { // Update the simulation
      if (RobotBase.isSimulation()) {
      sim.periodic();
  }
}
    
    public Command feed() {
      return run (coroutine-> {
        while (true) {
          motor.setThrottle(0.75); // Set the motor to full power
          coroutine.yield(); // Yield to allow other commands to run
        }
      }).named("feed");
    }

    public Command intake() {
      return run (coroutine-> {
        while (true) {
          motor.setThrottle(-1); // Set the motor to full power in reverse
          coroutine.yield(); // Yield to allow other commands to run
        }
      }).named("intake");
    }

    public Command outtake() {
      return run (coroutine-> {
        while (true) {
          motor.setThrottle(1); // Set the motor to full power in the forward direction
          coroutine.yield(); // Yield to allow other commands to run
        }
      }).named("outtake");
    }

    public Command idle() {
      return run (coroutine-> {
        while (true) {
          motor.setThrottle(0); // Set the motor to idle
          setDefaultCommand(idle());
          coroutine.yield(); // Yield to allow other commands to run
        }
      }).named("idle");
    }
  }


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
            setDefaultCommand(idle());
            coroutine.yield(); // Yield to allow other commands to run
          }
        }).named("idle");
      }
  }


  public Feeder feeder = new Feeder();
  public IntakeLauncher intakeLauncher = new IntakeLauncher();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {}


  @Override
  public void simulationPeriodic() {
    Scheduler.getDefault().run();
    intakeLauncher.periodic();
    feeder.periodic();
  }
}
