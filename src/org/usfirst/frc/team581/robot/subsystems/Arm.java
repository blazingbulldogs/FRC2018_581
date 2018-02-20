package org.usfirst.frc.team581.robot.subsystems;


import org.usfirst.frc.team581.robot.Robot;
import org.usfirst.frc.team581.robot.commands.ArmAngle;
import org.usfirst.frc.team581.robot.commands.ArmDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm extends Subsystem{
	public DifferentialDrive armDrive;
	public TalonSRX tal0 = new TalonSRX(0);
	public TalonSRX tal1 = new TalonSRX(1);
	public int pulseWidthPos;
	public double targetPositionRotations;
	//public SpeedControllerGroup talonGroup;
	//public PIDController pidController;
	//public ArmEncoder encoder;
	//public double zero;

	public Arm() {
		//armDrive = new DifferentialDrive(tal0, tal1);
		tal1.follow(tal0);
		tal1.setInverted(true);
		tal0.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,Constants.kPIDLoopIdx,0);
		tal0.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, 10);
		/* choose to ensure sensor is positive when output is positive */
		tal0.setSensorPhase(Constants.kSensorPhase);
		
		//tal0.configForwardLimitSwitchSource(tal0, LimitSwitchNormal NormallyOpen, Constants.kTimeoutMs);
		//tal0.configForwardSoftLimitThreshold(4400, 0);
		//tal0.configReverseSoftLimitThreshold(2000, 0);
		tal0.configForwardSoftLimitEnable(false, 0);
		tal0.configReverseSoftLimitEnable(false, 0);

		targetPositionRotations = tal0.getSensorCollection().getPulseWidthPosition();
		/*
		 * set the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		tal0.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
		
		//tal0.configOpenloopRamp(1, 0);
		
		/* set closed loop gains in slot0, typically kF stays zero. */
		tal0.config_kF(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		tal0.config_kP(Constants.kPIDLoopIdx, 0.2, Constants.kTimeoutMs);
		tal0.config_kI(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		tal0.config_kD(Constants.kPIDLoopIdx, 0.0, Constants.kTimeoutMs);
		
	}
	
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		setDefaultCommand(new ArmAngle(targetPositionRotations));
	}
	
	double velocity;
	public void testDrive () {
		if (Robot.oi.joy1.getRawButton(1)) {
			tal0.set(ControlMode.PercentOutput, 0.25); /* 25 % output */
			velocity = tal0.getSelectedSensorVelocity(0);
		}
		else 
			tal0.set(ControlMode.PercentOutput, 0.0);
		SmartDashboard.putString("DB/String 3", "" + velocity);
	}
	
	public void driveArm() {
		//armDrive.tankDrive(y, y);
		tal0.set(ControlMode.PercentOutput, Robot.oi.joy1.getY());
		pulseWidthPos = tal0.getSelectedSensorPosition(0);
		SmartDashboard.putString("DB/String 3", "" + pulseWidthPos);
	}
	
	public void setAngle(double Angle) {
		pulseWidthPos = tal0.getSelectedSensorPosition(0);
		SmartDashboard.putString("DB/String 1", "" + pulseWidthPos);
		SmartDashboard.putString("DB/String 2", "" + Angle);
		SmartDashboard.putString("DB/String 3", "" + targetPositionRotations);
		//tal0.set(ControlMode.Position, Angle);
	}

}
