package org.usfirst.frc.team5590.robot;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team5590.robot.Robot;


public class AutonomousMode {
	Timer timer;
	Robot robot;
	
	public AutonomousMode (Robot robo) {
		robot = robo;
	}
    
	public boolean autoTask(int counter) {
		if (counter <= 750) {
			if (counter <= 50) {
				//Slide out drawer
				//robot.slider.setX(1);
			} else if (counter<=90){
				//lift tote
				RobotMap.liftController.set(-1);
			} else if (counter <= 123){
				//rotate right
				RobotMap.liftController.set(0);
				robot.drivetrain.rotateRight(-.58);
			} else if(counter <= 373) {
				//drive into zone 3 seconds
				robot.drivetrain.setSpeed(.5);
			} else if (counter <= 413){
				//lower
				robot.drivetrain.stop();
				RobotMap.liftController.set(1);
			} else {
				robot.drivetrain.stop();
				RobotMap.liftController.set(0);
			}
			return true;
		} else {
			return false;
		}
	}
	
	public void auto(int seconds, String command, double speed) {
		timer = new Timer();
		//Creates task for set number of seconds
		timer.schedule(new TaskFinisher(), seconds * 1000);
		//Put run code in here
		switch (command) {
			case "speed": 
				robot.drivetrain.setSpeed(speed);
				break;
			case "turnRight":
				robot.drivetrain.rotateRight(speed);
				break;
			case "turnLeft":
				robot.drivetrain.rotateLeft(speed);
			case "stop":
				robot.drivetrain.stop();
		}
	}
	//Don't touch this!!!!!!!!!! 
	class TaskFinisher extends TimerTask {
		public void run() {
			System.out.format("Timer Task Finished..!%n");
			timer.cancel(); // Terminate the timer thread
		}
	}
 
}
