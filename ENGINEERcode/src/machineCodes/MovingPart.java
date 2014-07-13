package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

// MovingPart is defined as abstract and it must be subclassed
public abstract class MovingPart extends MechPart {
		
	protected boolean moving=false; // is true when the object moves
	
	protected String motionAxis; 	// the axis where the part moves
	protected int speed; 			// +/- 1 step depending on direction
	protected int pixelRange; 	// is the total allowed range that an object can make before it hits another
						// sometimes it is reduced when the object meets an obstacle
	protected double decimalStepLeftover; // because when calculating the steps that correnspond to a single
								// pixel motion we may have a decimal left over we need to store
								// to a variable so it is available the next time we calculate a step
								// other wise there would be a rounding error
	
	// The motion of a part is always defined between the low and the high step.
	// Then through comparison with the currentStep we figure out if the part can/cannot move.
	protected int lowStep; 		// the lower step of the motion (It is not the beginning, it just has smaller 
				 		// coordinates)
	protected int currentStep;	// the current step where the part is
	protected int highStep; 		// the higher step of the motion (It is not the end, it just has bigger 
				  		// coordinates)
	
	protected String motorName; // The name of the motor that moves this part.
					  // This name will be sent through the serial port to the microcontroller.
	protected int motorSpeed;   // The speed of the motor
	protected int totalSteps;	  // The total amount of motor steps
	protected int motorSteps_a; // The number of steps for the acceleration stage
	protected int motorSteps_b; // The number of steps for the constant speed stage
	protected int motorSteps_c; // The number of steps for the deceleration stage
	
	// hardware relevant fields
	protected ArrayList<Sensor> lowSensors = new ArrayList<Sensor>();
	protected ArrayList<Sensor> highSensors = new ArrayList<Sensor>();
	
	public MovingPart(String tag, Point p, Dimension d, Color c, 
					String axis, int speed, int pixelRange, 
					String motorName, int motorSpeed, int totalSteps) {
		super(tag, p, d, c);
		this.motionAxis=axis;
		this.pixelRange=pixelRange;
		this.speed=speed;
		this.motorName=motorName;
		this.motorSpeed=motorSpeed;
		this.totalSteps=totalSteps;
		calculateMotorSteps(0, totalSteps);	
	} // close MechPart constructor method
	
	
	// We use this method to explicitly define a part's motion given a low and a high step
	public void calculateMotorSteps (int lowStep, int highStep) {
		// We firstly assign the low/high steps
		this.lowStep=lowStep;
		this.highStep=highStep;
		// We calculate the acceleration/deceleration/constant speed steps
		motorSteps_a = (int) Math.floor((highStep-lowStep)/4);
		motorSteps_c = motorSteps_a;
		// We calculate constant speed steps this way, so that we avoid any rounding errors
		motorSteps_b = highStep - lowStep - motorSteps_a - motorSteps_c;
	} // close calculateMotorSteps method
	
	
	// We use this method to define a parts subcomponents e.g. if the part has sensors on it.
	protected void defineSubPosition(int speed, String axis) {
		super.defineSubPosition(speed, axis);
		if (axis.equals("x")) {
			for (MechPart i : extraParts) {
				i.specifyPart(new Point(i.p.x+speed, i.p.y), i.d, i.color);
				i.defineSubPosition(speed, "x"); // define the position of the extraParts of the Head 
												 // extraParts e.g. the sensors of the robot and head plates
			} // close loop
		} else if (axis.equals("y")) {
			for (MechPart i : extraParts) {
				i.specifyPart(new Point(i.p.x, i.p.y+speed), i.d, i.color);
				i.defineSubPosition(speed, "y"); // define the position of the extraParts of the Head 
									   			 // extraParts e.g. the sensors of the robot and head plates
			} // close loop
		}// close if
	} // close defineSubPosition method
	
	
	// offsetPart method changes the position and the current step of the object 
	public void offsetPart (){
		if (motionAxis=="x") {
			p.x+=speed;
			currentStep+=calculateSteps()*speed;
		} else if (motionAxis=="y") {
			p.y+=speed;
			currentStep+=calculateSteps()*speed;
		} // close if
		// updates the coordinates of the object when the motion stops
		specifyPart(new Point(p.x,p.y), d, color);
	} // close offsetPart method
	
	
	/* The motor steps and the number of pixels a part must move are different.
	 * What this method does is to scale the pixels with the motor steps. */
	private int calculateSteps() {
		int numOfSteps;
		int stepsPerPixel = (int) Math.floor(totalSteps/pixelRange);
		double doubleSteps = (double) totalSteps/pixelRange;
		decimalStepLeftover = decimalStepLeftover + doubleSteps - stepsPerPixel;
		
		// Every time the decimalStepLeftover becomes >1 we add 1 at the numOfSteps and we 
		// reduce decimalStepLeftover by -1 so that we have always only the decimal part
		numOfSteps=(int) (stepsPerPixel + Math.floor(decimalStepLeftover));
		if (decimalStepLeftover>1) {
			decimalStepLeftover-=1;
		} // close if
		
		// The "if" block is used to correct the steps at the last iteration (before the 
		// part reaches the ending position). This is the reason why it defines that the
		// number of steps for the last time will be the remaining steps (totalSteps - currentStep)
		if (speed>0 && (highStep - currentStep < 2*stepsPerPixel)) {
			numOfSteps=highStep - currentStep;
			decimalStepLeftover=0;
		} else if (speed<0 && (currentStep - lowStep < 2*stepsPerPixel)){
			numOfSteps=currentStep - lowStep;
			decimalStepLeftover=0;
		} // close if

		return numOfSteps;
	} // close calculateSteps method
	
	
	// The next 2 methods define the moving directin through the sign of speed
	public void positiveDirection() {
		speed = Math.abs(speed);
	} // close positiveDirection method
	public void negativeDirection() {
		speed = -Math.abs(speed);
	} // close negativeDirection method
	
	
	
	public boolean isPositiveMoveable() {
		boolean positiveMoveable = false;
		// The first step is to check if the part is inside the proper step range
		if (currentStep>=lowStep && currentStep<highStep) {
			positiveMoveable = true;
		} else {
			positiveMoveable = false; // the method stops if the condition is not met
		} // close if
		return positiveMoveable;
	} // close isPositiveMoveable method
	
	
	public boolean isNegativeMoveable() {
		boolean negativeMoveable=false;
		// The first step is to check if the part is inside the proper step range
		if (currentStep>lowStep && currentStep<=highStep) {
			negativeMoveable=true;
		} else {
			negativeMoveable=false; // the method stops if the condition is not met
		} // close if
		return negativeMoveable;
	} // close isNegativeMoveable


	
	// SETTERS & GETTERS ------------------------------------------------------------------
	public int getLowStep() {
		return lowStep;
	}


	public void setLowStep(int lowStep) {
		this.lowStep = lowStep;
	}


	public int getCurrentStep() {
		return currentStep;
	}


	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}


	public int getHighStep() {
		return highStep;
	}


	public void setHighStep(int highStep) {
		this.highStep = highStep;
	}


	public String getMotorName() {
		return motorName;
	}


	public void setMotorName(String motorName) {
		this.motorName = motorName;
	}


	public int getMotorSpeed() {
		return motorSpeed;
	}


	public void setMotorSpeed(int motorSpeed) {
		this.motorSpeed = motorSpeed;
	}
	
	
	

//	public double getDelay() {
//		double dly = 0;
//		if (currentStep<motorSteps_a) { 						// Acceleration										  
//		    double normalization = 800.0/motorSteps_a*currentStep;
//		    double logisticFcn = (-1+2/(1+Math.exp(-0.007*(normalization + 1))));
//		    double delayFcn = motorSteps_a/(currentStep+1.0)/
//		    					(2*(motorSpeed/1e6));
//		    dly = delayFcn * logisticFcn;
//		    
//		  } else if (currentStep>=motorSteps_a && 				// Constant velocity
//				  currentStep<(motorSteps_a+motorSteps_b)){      
//		    dly = 1.0/(2*(motorSpeed/1e6));
//		    
//		  } else if (currentStep>=(motorSteps_a+motorSteps_b) 
//				  && currentStep<totalRange){   				// Deceleration
//		    double normalization = 800.0/motorSteps_c*
//		    		(currentStep-motorSteps_a-motorSteps_b);
//		    double logisticFcn = (-1)*(-1+2/(1 + Math.exp(-0.007*(normalization - 800))));
//		    double delayFcn = 1.0*motorSteps_c*1e6/2/motorSpeed/
//		    		(totalRange-currentStep);
//		    dly = delayFcn * logisticFcn;
//		  } // close if
//		return dly;
//	} // close getDelay method
	
	
} // close MovingPart class
