/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.Scheduler;
import org.wpilib.drive.DifferentialDrive;
import org.wpilib.framework.OpModeRobot;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;
import org.wpilib.math.util.Units;
import org.wpilib.math.util.Units.*;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import static org.wpilib.units.Units.Seconds;

import first.robot.Mechanisms.Drivetrain;
import first.robot.Mechanisms.Feeder;
import first.robot.Mechanisms.IntakeLauncher;
import first.robot.simulation.DrivetrainSim;
import first.robot.simulation.SingleFlywheelSim;

/**
 * The methods in this class are called automatically as described in the OpModeRobot documentation.
 * OpMode classes anywhere in the package (or sub-packages) where this class is located are
 * automatically registered to display in the Driver Station. If you change the name of this class
 * or the package after creating this project, you must also update the Main.java file in the
 * project.
 */
public class Robot extends OpModeRobot {
    public final Drivetrain drivetrain = new Drivetrain();
    public final IntakeLauncher intakeLauncher = new IntakeLauncher();
    public final Feeder feeder = new Feeder();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {}


  @Override
  public void simulationPeriodic() {
    Scheduler.getDefault().run();
    drivetrain.periodic();
    intakeLauncher.periodic();
    feeder.periodic();
  }
}
