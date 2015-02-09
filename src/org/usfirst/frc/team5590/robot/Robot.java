
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
	boolean slideExtend = false, slideRetract = false;
	boolean liftUp = false, liftDown = false;
	public int pos = -1;
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
    	slider.init();
    	vertLift.init();
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	//In theory this should work....if not...well then shit...good luck
    	//If no speed value, then put -1
    	AutonomousMode autoCommand = new AutonomousMode(this);
    	autoCommand.autoTask(5, "speed", .5);
    	autoCommand.autoTask(3, "stop", 0);
    	
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
    		drivetrain.takeJoystickInput(deadzone(trigger), deadzone(-trigger));
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
    	}
    	//END SHHH
    	
    	//DRIVE ROBOT END
    	
    	//DRAWER BEGIN
    	//Figure out Y Axis of Logitech
    	double drawAxis;
    	drawAxis = oi.logitech.getRawAxis(6);
    	
    	//USED FOR TESTING SLIDER MANUALLY
    	if (deadzone(drawAxis) > 0) {
    		slider.setX(drawAxis);
    	} else if (deadzone(drawAxis) < 0) {
    		slider.setX(drawAxis);
    	}
    	//END MANUAL TEST
    	
    	if (oi.three.get()) {
    		slideExtend = true;
    	}
    	if (oi.four.get()) {
    		slideRetract = true;
    	}
    	
    	if (slideExtend) {
    		slideExtend = slider.extend();
    	} else {
    		slider.setX(0);
    	}
    	
    	if (slideRetract) {
    		slideRetract = slider.retract();
    	} else {
    		slider.setX(0);
    	}
    	
    	
    	//DRAWER END
    	
    	//VERT LIFT START
    	
    	double liftAx = oi.logitech.getRawAxis(2);
    	if (deadzone(liftAx) != 0) {
    		vertLift.setHeight(deadzone(liftAx));
    	}
    	
    	
    	if (oi.eleven.get()) { pos = 0;}
    	
    	if (oi.twelve.get()) {pos = 1;}
    	
    	if (oi.nine.get()) { pos = 2;}
    	
    	if (oi.ten.get()) {	pos = 3; }
    	
    	if (oi.seven.get()) { pos = 4; }
    	
    	if (pos != -1) { //Moves lift mech
    		pos = vertLift.mover(pos);
    	}
    	
    	
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
