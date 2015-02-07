
package org.usfirst.frc.team5590.robot;


import java.util.Timer;
import java.util.TimerTask;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Victor;

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
    	AutonomousMode autoCommand = new AutonomousMode(this);
    	autoCommand.autonomousTask(5, "speed", .5);
    	autoCommand.autonomousTask(3, "stop", 0);
    	
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
        
        if (oi.trigger.get() == true){
        	
        }
        //Rotate
        //Left trigger == forward...Right trigger is backwards
    	if (oi.rightB.get() && !oi.a.get()) {
    		drivetrain.rotateRight(.6);
    	}
    	if (oi.leftB.get() && !oi.a.get()) {
    		drivetrain.rotateLeft(.6);
    	}
    	
    	//Lift Control using Throttle
    	double throttle;
    	throttle = oi.logitech.getRawAxis(3);
    	throttle=((-throttle+1)/2);
    	
    	if (oi.trigger.get()){
    		vertLift.setHeight(throttle);
    	}
    	else if(oi.thumb.get()){
    		vertLift.setHeight(-throttle);
    	}
    	else{vertLift.setHeight(0);
    	}
    	
    	//SHHHH (Slalom)
    	if (oi.stop.get()) {
    		drivetrain.takeJoystickInput(left / 2, left);
    	}
    	if (oi.start.get()) {
    		drivetrain.takeJoystickInput(left, left / 2);
    	}
    	//END SHHH
    	
    	//DRIVE ROBOT END
    	
    	//DRAWER BEGIN
    	//Figure out Y Axis of Logitech
    	double drawAxis;
    	drawAxis = oi.logitech.getRawAxis(1);
    	
    	if (deadzone(drawAxis) > 0) {
    		slider.extend();
    	} else if (deadzone(drawAxis) < 0) {
    		slider.retract();
    	}
    	//DRAWER END
    	
    	//VERT LIFT START
    	
    	//VERT LIFT END
    	
    	System.out.println(RobotMap.liftLeft.getDistance());
    	
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
    
    	
}
