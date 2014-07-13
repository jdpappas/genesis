package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class HeadPlunger extends MovingPart {
	
	public HeadPlunger(String tag, Point p, Dimension d, Color c, 
			String axis, int speed, int pixelRange, 
			String motorName, int motorSpeed, int totalSteps) {
		// calls the constructor of the super class
		super(tag, p, d, c, axis, speed, pixelRange, motorName, motorSpeed, totalSteps); 
		this.speed=1;
		this.lowStep=0;
		this.currentStep=0;
		this.highStep=totalSteps;
	} // close HeadPlunger constructor method
	
	@Override
	public void paint(Graphics g) {
		// calls the draw method of the superclass, else it would only call the commands specified in the subclass
		super.paint(g); 
		g.fill3DRect(p.x,p.y,d.width,d.height,true);
	} // close paint method

	
	
} // close HeadPlunger class