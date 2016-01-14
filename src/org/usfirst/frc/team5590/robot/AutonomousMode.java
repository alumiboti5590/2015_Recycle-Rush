package org.usfirst.frc.team5590.robot;

import java.util.Timer;
import java.util.TimerTask;

import org.usfirst.frc.team5590.robot.Robot;

public class AutonomousMode {
	Timer timer;
	Robot robot;
	private final int EXTEND_COUNT = 80;
	private final int LIFT_COUNT = EXTEND_COUNT + 60;
	private final int ROTATE_COUNT = LIFT_COUNT + 55;
	private final int DRIVE_COUNT = ROTATE_COUNT + 240;
	private final int LOWER_COUNT = DRIVE_COUNT + 40;

	public AutonomousMode(Robot robo) {
		robot = robo;
	}

	public boolean autoTask(int type, int counter) {
		switch (type) {
		//Pick Up tote but dont move
		case 0:
			if (counter <= 750) {
				if (counter <= 0) {
					// Slide out drawer
					//robot.slider.setX(1);
				} else if (counter <= LIFT_COUNT - 110) {
					// lift tote
					robot.slider.setX(0);
					RobotMap.liftController.set(-1);
				} else {
					//drive backwards
					RobotMap.liftController.set(0);
				}
				return true;
			} else {
				return false;
			}
		//Drive Back straight over bump w/ or w/out can
		case 1:
			if (counter <= 750) {
				if (counter <= EXTEND_COUNT) {
					// Slide out drawer
					robot.slider.setX(1);
				} else if (counter <= LIFT_COUNT) {
					// lift tote
					robot.slider.setX(0);
					RobotMap.liftController.set(-1);
				} else if (counter <= LIFT_COUNT + 100) {
					//drive backwards
					RobotMap.liftController.set(0);
					robot.drivetrain.setSpeed(-.91);
				} else if (counter <= LIFT_COUNT + 250) {
					robot.drivetrain.stop();
					//RobotMap.liftController.set(1);
				} else {
					robot.drivetrain.stop();
					RobotMap.liftController.set(0);
				}
				return true;
			} else {
				return false;
			}
		case 2: //Do nothing
			if (counter < 750){
				robot.drivetrain.setSpeed(0);
			}
		//Just drive itself in
		default:
			if (counter <= 750) {
				if (counter <= 250) {
					// Slide out drawer
					robot.drivetrain.setSpeed(.5);
				} else {
					robot.drivetrain.stop();
					RobotMap.liftController.set(0);
				}
				return true;
			} else {
				return false;
			}

		}
	}

	public void auto(int seconds, String command, double speed) {
		timer = new Timer();
		// Creates task for set number of seconds
		timer.schedule(new TaskFinisher(), seconds * 1000);
		// Put run code in here
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

	// Don't touch this!!!!!!!!!!
	class TaskFinisher extends TimerTask {
		public void run() {
			System.out.format("Timer Task Finished..!%n");
			timer.cancel(); // Terminate the timer thread
		}
	}

}
