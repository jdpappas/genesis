package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class Robot extends MovingPart {
	private MechPart selectedCarrier = null;
	
	public Robot(String tag, Point p, Dimension d, Color c, 
			String axis, int speed, int pixelRange, 
			String motorName, int motorSpeed, int totalSteps) {
		// calls the constructor of the super class
		super(tag, p, d, c, axis, speed, pixelRange, motorName, motorSpeed, totalSteps);
		// if I define it as this.initPos=p; then the change in initPos will affect p
		// immediately as they are actually different references for the same object
		this.speed=-1;
		this.lowStep=0;
		this.currentStep=totalSteps;
		this.highStep=totalSteps;
	} // close Robot constructor method
	
	
	@Override
	public void paint(Graphics g) {
		// calls the draw method of the superclass, else it would only call the commands specified in the subclass
		super.paint(g); 
		g.fillRect(p.x,p.y,d.width,d.height);
		
		// we draw the extra parts of the robot (here the sensors)
		for (MechPart i : extraParts) {
			i.paint(g);	
			// without this "if" the carrier cannot be painted in the last loop so there is 
			// an offset of 5 pixels
			if (selectedCarrier!=null) {
			selectedCarrier.paint(g);
			} // close if
		} // close loop
	} // close paint method

		
	@Override
	public void offsetPart() {
		super.offsetPart();
		// Defines to which direction the subcomponents must move
		defineSubPosition(speed, "x");
	} // close offsetPart method
	
	
	@Override
	public void defineSubPosition(int speed, String axis) {
		super.defineSubPosition(speed, axis);
		// if the robot is attached to a carrier we must redraw it too
		if (selectedCarrier!=null) {
			Point carrierPos = new Point(p.x-selectedCarrier.d.width/2+this.d.width/2,selectedCarrier.p.y);
			selectedCarrier.specifyPart(carrierPos, selectedCarrier.d, selectedCarrier.color);
		} // close if
	} // close defineSubPosition method


	
	
	// SETTERS & GETTERS -------------------------------------------------------------------------
	public MechPart getSelectedCarrier() {
		return selectedCarrier;
	}


	public void setSelectedCarrier(MechPart selectedCarrier) {
		this.selectedCarrier = selectedCarrier;
	}
	
} // close Robot class