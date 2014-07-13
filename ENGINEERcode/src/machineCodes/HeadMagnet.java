package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class HeadMagnet extends MovingPart {
	
	private HeadPuller hpuller; // The puller instance is required so that the magnet tells the puller
								// not to move when it is at the top position
	
	public HeadMagnet(String tag, Point p, Dimension d, Color c, 
			String axis, int speed, int pixelRange, 
			String motorName, int motorSpeed, int totalSteps) {
		// calls the constructor of the super class
		super(tag, p, d, c, axis, speed, pixelRange, motorName, motorSpeed, totalSteps); 
		this.totalSteps=totalSteps;
		this.speed=-1; // the magnet initially moves to the NEGATIVE direction
		this.lowStep=0;
		this.currentStep=totalSteps; // the current step == higher step (BOTTOM POSITION)
		this.highStep=totalSteps;
	} // close HeadMagnet constructor method
	
		
	@Override
	public void paint(Graphics g) {
		// calls the paint method of the superclass, else it would only call the commands specified in the subclass
		super.paint(g); 
		g.fill3DRect(p.x,p.y,d.width,d.height,true);
		// Draws the rectangle for the motion range of the hrotator
		g.setColor(new Color(255,163,92));
		g.fill3DRect(p.x+140, p.y, 40, 10, true);
		// the extra parts are drawn (sensors)
		for (MechPart i : extraParts) {
			i.paint(g);	
		} // close loop
	} // close paint method
	
	
	@Override
	public void offsetPart() {
		super.offsetPart();
		// Defines to which direction the subcomponents must move
		defineSubPosition(speed, "y");
		definePullerMotion();
	} // close offsetPart method
	
	
	public void definePullerMotion() {
	// The purpose of this method is to prevent the puller from moving when the magnet
	// is at the top position. 
		// The following 2 variables are required to store the normalized steps position of
		// the magnet and the puller plate, so that we can compare their positions.
		double magnetNorm = currentStep/(totalSteps/pixelRange);
		double pullerNorm = hpuller.totalSteps/(hpuller.totalSteps/hpuller.pixelRange);
		
		// What the following "if" block says is: if the magnet plate is inside the range of 
		// motion of the puller plate then lower the highStep of the puller so that it does not
		// collide with the magnet. If the magnet gets out of the puller motion range, then the
		// puller can again move to its max range.
		if (magnetNorm <= pullerNorm) {
			hpuller.highStep = (int) (magnetNorm*(hpuller.totalSteps/hpuller.pixelRange));
		} else {
			hpuller.highStep=hpuller.totalSteps;
		} // close if
	} // close definePullerMotion method

	public void setPuller(HeadPuller temp) {
		this.hpuller=temp;
	} // close assignMagnet method
	
} // close HeadMagnet class