package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class HeadBase extends MovingPart {
/* HeadBase differs from the other parts because the motion is not defined between the extreme
 * positions but in the position defined from the user.
 */
	
	private int previousPosition;	// previousPosition is required to check if the robot is in a proper
									// position to hold a carrier
	private int nextPosition=0; 	// nextPosition is required to check if the robot is in a proper
									// position to hold a carrier
	// we also need to have a list of the carriers so that they are assigned to robot when 
	// the head is at the proper Z level
	private ArrayList<PlateCarrier> carriers; 

	
	public HeadBase(String tag, Point p, Dimension d, Color c, 
			String axis, int speed, int pixelRange, 
			String motorName, int motorSpeed, int totalSteps) {
		// calls the constructor of the super class
		super(tag, p, d, c, axis, speed, pixelRange, motorName, motorSpeed, totalSteps);
		// redefines the area of the HeadBase
		// the height is set to 35 so that the base can be selected with the mouse
		// in its central area
		this.b.setBounds(p.x, p.y+35, d.width, 20);
		this.speed=1; 		// The Head moves to the positive direction initially.
		this.lowStep=0; 		// The lower step is the top -> 0.
		this.currentStep=0; 	// The head starts at the top so the current step is 0
		this.highStep=totalSteps; 	// the higherStep in the beginning equals to the totalRange
	} // close HeadBase constructor method
	
	
	@Override
	public void paint(Graphics g) {
		// calls the draw method of the superclass, else it would only call the commands specified in the subclass
		super.paint(g);
		
		// The START and END sensors of the robot and the line that connects them
		Sensor temp=(Sensor) Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "RE");
		g.setColor(temp.getTrueColor()); 
		MechPart temp2=Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "RS");
		// the line where the robot moves
		g.drawLine(temp.p.x+10, temp.p.y+10-1, temp2.p.x, temp2.p.y+10-1);
		
		// paints the SOLID head base parts 
		g.setColor(this.color);
		int posX=p.x; int posY=p.y;
		g.fill3DRect(posX, posY, d.width, 10, true); // the upper part of the head (above the plungers)
		// the coordinates for the creation of the polygon - static part of the HeadBase
		g.fill3DRect(posX, posY+35, d.width, 20, true); // the upper part of the head (above the plungers)
					
		// Draws the HeadPuller-Plunger-Magnet, Robot, Head and Robot Sensors
		for (MechPart i : extraParts) {
			i.paint(g);	
		} // close loop
	} // close paint method
	
	
	// We have to redefine specifyPart method for HeadBase, because the rectangle b
	// boundaries are NOT the same as the whole object
	public void specifyPart(Point p, Dimension d, Color color){
		this.color=color;
		this.p=p; // the parts upper left corner coordinates
		this.d=d; // the parts width and height
		this.b.setBounds(p.x, p.y+35, d.width, 20);
	} // close setBoundary method
	
	
	@Override
	public void offsetPart () {
		super.offsetPart();
		// Defines to which direction the subcomponents must move
		defineSubPosition(speed, "y");
		// checks if the robot is in the right level to "catch" a carrier
		selectCarrier();
	} // close offsetPart method

	
	private void selectCarrier() {
		// The purpose of this method is to define whether or not the robot is allowed to 
		// carry a carrier. 
		Robot robot=(Robot) Investigator.find((ArrayList <MechPart>) 
						extraParts, "tag", "robot");
		robot.setSelectedCarrier(null); // selected carrier turns to null to delete previous selection
		for (PlateCarrier c : carriers) {
			// we check if the carrier is at the same y-level with the sensor and
			// if the robot and the carrier are in the same x axis position
			if ((robot.p.y+robot.d.height/2==c.p.y+c.d.height/2) && 
					((robot.p.x+robot.d.width/2)==(c.p.x+c.d.width/2))) {
				robot.setSelectedCarrier(c);
			} // close if
		} // close for loop
	} // close selectCarrier method
	
		
	public void defineAutoMotionRange() {
	/* Here we define ONLY THE MOTION RANGE (i.e. the low and high step) 
	 * depending on the selected position (0-7)
	 * NO MOTION steps are done in this method.
	 */
		// Initialize the previous step - where the motion will start
		int previousStep = currentStep; 
		// We define the stepUnit, so that when the user changes the speed and stepNumber
		// no problems are created to the motion of the head
		int stepUnit = totalSteps/8;
		
		// if the previous or next position is 0 then there is an extra steps number
		// else we normally calculate the number of steps
		int nextStep=0; // just initialization
		// The following if defines where the head will stop according to the selection 
		// defined by the user in the method selectPositionWithMouse
		if (getPreviousPosition()==0) {
			nextStep = 2*stepUnit + Math.abs(getNextPosition()-1)*stepUnit;
		} else if (getNextPosition()==0){
			nextStep=0;
		} else if (getNextPosition()!=0 || getPreviousPosition()!=0) {
			nextStep=2*stepUnit+Math.abs(getNextPosition()-1)*stepUnit;
		} // close if
		
		// define the low and high step between which the motion will take place
		if (nextStep > previousStep) {
			lowStep = previousStep;
			highStep = nextStep;
		} else {
			lowStep = nextStep;
			highStep = previousStep;
		} // close if
		
	} // close defineAutoMotionRange method
	
	public void selectPositionWithMouse () {
	/* The purpose of this method is to ask the user where he wants the Head to go.
	 * This is done through an INPUTDIALOG. The method also defines the criteria under
	 * which a selection is valid.
	 * The method JUST CONTROLS THE SELECTION in the INPUTDIALOG. NO MOTION IS DEFINED HERE !!!
	 */	
    boolean validInput = false;  // for input validation
    int numberIn=getPreviousPosition();
	String selPosition = JOptionPane.showInputDialog(null,"Enter a number from 0-7",
			"Select Head vertical position", JOptionPane.PLAIN_MESSAGE);
	do {
        try {
           numberIn = Integer.parseInt(selPosition); // turn input into integer
        } catch (NumberFormatException ex) {
        	// The NumberFormatException is thrown when the assignment to a variable is not
        	// of the variable type.
        	numberIn=-2; // set numberIn -2 in case that the we have a non-integer input
        	// This "if" block is used to check if the user has indeed made a selection,
        	// or changed his mind and closed the JOptionPane
        	 if (selPosition==null) {
     			highStep=lowStep; // set this so that the head does not move
     							  // in the methods that follow
     			moving=false; // if moving==true then there can be no other new selection
     						  // (see controller class)
             	break; // break loop when selPos==null-> CANCEL or CLOSE
             } // close if
        } // close try - catch block
        
        if (numberIn < 0 || numberIn > 7) {
    	// in case the integer is out of bounds repaint the INPUTDIALOG with error message
    	selPosition = JOptionPane.showInputDialog(null,"Invalid number! Enter a number from 0-7",
			"Select Head vertical position", JOptionPane.ERROR_MESSAGE);
    	
        } else { // Here is what happens if the selection is valid
           validInput = true; // so that the while-loop stops
           setNextPosition(numberIn); // required in the following method
           defineAutoMotionRange(); // the method that defines the head motion range
           setPreviousPosition(getNextPosition()); 	// required for the previous method in case that
           								  			// the loop does not stop
        } // close if
     } while (!validInput); // repeat if input is not valid
	
	} // close selectPositionWithMouse


	public int getNextPosition() {
		return nextPosition;
	} // close getNextPosition method


	public void setNextPosition(int nextPosition) {
		this.nextPosition = nextPosition;
	} // close setNextPosition method

	
	public int getPreviousPosition() {
		return previousPosition;
	} // close getPreviousPosition method


	public void setPreviousPosition(int previousPosition) {
		this.previousPosition = previousPosition;
	} // close setPreviousPosition method	
	

	public void setCarriers (ArrayList<PlateCarrier> temp) {
		this.carriers=temp;
	} // close assignCarriers method
	
	public ArrayList<PlateCarrier> getCarriers() {
		return this.carriers;
	}
	
	
} // close HeadBase class