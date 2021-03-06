package org.usfirst.frc.team581.robot.subsystems;

import org.usfirst.frc.team581.robot.RobotMap;
import org.usfirst.frc.team581.robot.commands.CompressorLoop;
import org.usfirst.frc.team581.robot.commands.GrabEject;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grabber extends Subsystem{
	Compressor compressor = new Compressor(0);
	Solenoid solenoid1 = new Solenoid(RobotMap.solenoid1);
	Solenoid solenoid2 = new Solenoid(RobotMap.solenoid2);
	boolean compressorEnable = false;
	
	Spark grabberLeftMotor = new Spark(RobotMap.grabLeftMotor);
	Spark grabberRightMotor = new Spark(RobotMap.grabRightMotor);
	SpeedControllerGroup grabberMotors;
	
	public Grabber() {
		grabberRightMotor.setInverted(true);
		grabberMotors = new SpeedControllerGroup(grabberLeftMotor, grabberRightMotor);
	}
	
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		//setDefaultCommand(new CompressorLoop());
		setDefaultCommand(new GrabEject());
	}
	public void solenoidOn(boolean sol) {
		if(sol) {
			solenoid1.set(true);
		}else {
			solenoid2.set(true);
		}
		
	}
	public void solenoidOff(boolean sol) {
		if(sol) {
			solenoid1.set(false);
		}else {
			solenoid2.set(false);
		}
	}
	public void toggleCompressor(){
		//System.out.println("--> Toggle boolean");
		compressorEnable = !compressorEnable;
	}
	
	public void compressorLoop(){
		if(compressorEnable) {
			compressor.start();
		}
		else {
			compressor.stop();
		}
	}
	
	//final public double GRABBER_POWER = 0.65;
	
	public void grab(double GRABBER_POWER){
		grabberMotors.set(GRABBER_POWER);
	}
	public void eject(double GRABBER_POWER) {
		grabberMotors.set(-GRABBER_POWER);
	}
	public double limit(double POWER) {
		final double minPower = 0.4;
		if (POWER < 0) {
			return clamp(-1.0, POWER, -minPower);
		}else {
			return clamp(minPower, POWER, 1.0);
		}
	}
	public double clamp(double lo, double x, double hi) {
		return Math.min(Math.max(lo, x), hi);
	}
	public void grabPower(double GPOWER, double TPOWER) {
		double leftPower = limit(GPOWER/2 + TPOWER/4);
		double rightPower = limit(GPOWER/2 + TPOWER/4);
		grabberLeftMotor.set(leftPower);
		grabberLeftMotor.set(rightPower);
	}
	public void stopMotor() {
		grabberMotors.set(0);
	}

}
