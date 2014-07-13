package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class HeadPuller extends MovingPart {
	
	private HeadMagnet hmagnet; // required to define the steps of the magnet
	
	public HeadPuller(String tag, Point p, Dimension d, Color c, 
			String axis, int speed, int pixelRange, 
			String motorName, int motorSpeed, int totalSteps) {
		// calls the constructor of the super class
		super(tag, p, d, c, axis, speed, pixelRange, motorName, motorSpeed, totalSteps);

		this.speed=1;
		this.lowStep=0;
		this.currentStep=0;
		this.highStep=totalSteps;
	} // close HeadPuller constructor method
	
		
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
		// Defines to which direction the subcomponents must move
		defineSubPosition(speed, "y");
		// when the puller changes position, it will affect the motion range of the magnet
		defineMagnetMotion();
	} // close offsetPart method
	
	public void setMagnet(HeadMagnet temp) {
		this.hmagnet=temp;
	} // close assignMagnet method
	
	private void defineMagnetMotion() {
	// The purpose of this method is to prevent the magnet from moving when the puller
	// is at the top position. 
		// The following variable is required to store the normalized steps position of
		// the puller plate, so that we can compare its position to the one of the magnet.
		double pullerNorm = currentStep/(totalSteps/pixelRange);		

		// What the following "if" block says is: if the puller is not at the lowest step then
		// increase the lower step of the magnet plate so it does not collide with the puller.
		// When the puller returns to its lowest position then change the low step of the magnet
		// back to its initial value -> 0
		if (pullerNorm > 0) {
			hmagnet.lowStep=(int) (pullerNorm*(hmagnet.totalSteps/hmagnet.pixelRange));
		} else {
			hmagnet.lowStep=0;
		} // close if
	} // close defineMagnetMotion method
	
} // close HeadPuller class