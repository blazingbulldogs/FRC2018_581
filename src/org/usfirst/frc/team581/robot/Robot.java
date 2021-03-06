/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team581.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team581.robot.commands.AutonEncTest;
import org.usfirst.frc.team581.robot.commands.AutonGroup;
import org.usfirst.frc.team581.robot.commands.AutonLeft;
import org.usfirst.frc.team581.robot.commands.AutonRight;
import org.usfirst.frc.team581.robot.commands.AutonTest;
import org.usfirst.frc.team581.robot.subsystems.Arm;
import org.usfirst.frc.team581.robot.subsystems.AutonDrive;
import org.usfirst.frc.team581.robot.subsystems.Drive;
import org.usfirst.frc.team581.robot.subsystems.Grabber;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	public final static Drive drive = new Drive();
	public static OI oi;
	public static Arm arm = new Arm();
	public static Grabber grabber = new Grabber();
	public static AutonDrive autondrive = new AutonDrive();
	public String gameData;
	Compressor compressor = new Compressor(0);

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		//m_chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		//SmartDashboard.putData("Auto mode", m_chooser);
		CameraServer.getInstance().startAutomaticCapture();
		clearLogs();
		compressor.start();

	}
	
	private void clearLogs() {
		for (int i = 0; i < 10; i++) {
			SmartDashboard.putString("DB/String "+i, "");
		}

	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.length() > 0) {
			if(gameData.charAt(0) == 'L') {
				SmartDashboard.putString("DB/String 3", "Autonomous Left running!");
				m_autonomousCommand = new AutonLeft(0.8, 0, 0, 36, 36);
			}else {
				SmartDashboard.putString("DB/String 7", "Autonomous Right running!");
				m_autonomousCommand = new AutonRight(0.8, 0, 0, 36, 36);		
			}
		}
		//m_autonomousCommand = new AutonLeft(0.8, 0, 0, 36, 36);
		if (m_autonomousCommand != null) m_autonomousCommand.start();
		autondrive.resetEncoders();
		autondrive.start();
		drive.resetEncoders();
		drive.stop();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
		
		autondrive.resetEncoders();
		autondrive.stop();
		drive.resetEncoders();
		drive.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
