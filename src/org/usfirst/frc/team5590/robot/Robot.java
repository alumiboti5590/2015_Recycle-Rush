
package org.usfirst.frc.team5590.robot;


import java.util.Timer;
import java.util.TimerTask;


import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. This is the Main Robot class
 */
public class Robot extends IterativeRobot {
	public OI oi;
	//Set a run count to see how many times autonomous runs
	static int runCount = 0;
	//Initialze subsystems
	public VerticalLift vertLift;
	public Drivetrain drivetrain;
	public Slides slider;
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	RobotMap.init();
    	oi = new OI();
    	drivetrain = new Drivetrain();
    	vertLift = new VerticalLift();
    	slider = new Slides();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	//In theory this should work....if not...well then shit...good luck
    	//If no speed value, then put -1
    	new AutonomousTask(5, "speed", .5);
    	
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//DRIVE ROBOT
    	//Figure out speed of tracks from YAxis inputs
    	double left, right, trigger;
    	left = (Math.pow(oi.xbox.getRawAxis(1), 3) * -1.0);
        right = (Math.pow(oi.xbox.getRawAxis(5), 3) * -1.0);
        //used to be 5 
        trigger = (Math.pow(oi.xbox.getRawAxis(3), 3) * -1.0);
    	
        
        // Basic drive commands....A makes tracks go equal AKA Straight
        if (oi.a.get() == true) {
    		drivetrain.takeJoystickInput(deadzone(left), deadzone(left));
    	} else {
    		drivetrain.takeJoystickInput(deadzone(left), deadzone(right));
    	}
        
        
        //Rotate
        //Left trigger == forward...Right trigger is backwards
    	if (oi.rightB.get() && !oi.a.get()) {
    		drivetrain.rotateRight(.6);
    	}
    	if (oi.leftB.get() && !oi.a.get()) {
    		drivetrain.rotateLeft(.6);
    	}
    	
    	
    	//SHHHH (Slalom)
    	if (oi.stop.get()) {
    		drivetrain.takeJoystickInput(left / 2, left);
    	}
    	if (oi.start.get()) {
    		drivetrain.takeJoystickInput(left, left / 2);
    	} //END SHHH
    	
    	//DRIVE ROBOT END
    	
    	//DRAWER BEGIN
    	//Figure out Y Axis of Logitech
    	double drawAxis;
    	drawAxis = oi.logitech.getRawAxis(2);
    	
    	if (deadzone(drawAxis) > 0) {
    		slider.extend();
    	} else if (deadzone(drawAxis) < 0) {
    		slider.retract();
    	}
    	//DRAWER END
    	
    	//VERT LIFT START
    	
    	//VERT LIFT END
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    //Check for deadband
    public double deadzone(double axisAmount) {
    	if (axisAmount < .1 && axisAmount > -.1) {
    		axisAmount = 0;
    	} 
    	return axisAmount;
    }
    
    
    /*
     * Use this for scheduling autonomous commands with time durations 
     * (See autoPeriodic)
     */
    public class AutonomousTask {
    	Timer timer;
     
    	public AutonomousTask(int seconds, String command, double speed) {
    		timer = new Timer();
    		timer.schedule(new TaskFinisher(), seconds * 1000);
    		//Put run code in here
    		switch (command) {
    			case "speed": 
    				drivetrain.setSpeed(speed);
    				break;
    			case "turnRight":
    				drivetrain.rotateRight(speed);
    				break;
    			case "turnLeft":
    				drivetrain.rotateLeft(speed);
    			case "stop":
    				drivetrain.stop();
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
}
