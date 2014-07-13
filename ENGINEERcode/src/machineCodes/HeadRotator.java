package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class HeadRotator extends MovingPart {

	public HeadRotator (String tag, Point p, Dimension d, Color c, 
			String axis, int speed, int pixelRange, 
			String motorName, int motorSpeed, int totalSteps) {
		// calls the constructor of the super class
		super(tag, p, d, c, axis, speed, pixelRange, motorName, motorSpeed, totalSteps);
		this.speed=1;
		this.lowStep=0;
		this.currentStep=0;
		this.highStep=totalSteps;
	} // close HeadRotator constructor method

	
	@Override
	public void paint(Graphics g) {
		// calls the paint method of the superclass, else it would only call the commands specified in the subclass
		super.paint(g); 
		g.fill3DRect(p.x,p.y,d.width,d.height,true);
		
		// the extra parts are drawn (sensor)
		for (MechPart i : extraParts) {
			i.paint(g);	
		} // close loop
	} // close paint method
	
	
	@Override
	public void offsetPart() {
		super.offsetPart();
		// The sensor does not move when the rotator moves, so there is no
		// defineSubPosition method required
	} // close offsetPart method
	
} // close HeadRotator class
