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
    
	public void autoTask(int seconds, String command, double speed) {
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
