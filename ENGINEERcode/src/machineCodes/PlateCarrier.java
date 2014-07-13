package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

class PlateCarrier extends MechPart {
	
	public PlateCarrier(String tag, Point p, Dimension d, Color color) {
		super(tag, p, d, color); // calls the constructor of the super class
	} // close PlateCarrier constructor method
	
	@Override
	public void paint(Graphics g) {
	// calls the draw method of the superclass, else it would only call the commands specified in the subclass
	super.paint(g); 
	int posX=p.x; int posY=p.y; int dimX=d.width; int dimY=d.height;
	g.fill3DRect(posX,posY,dimX/2-15,dimY,true);
	g.fill3DRect(posX+dimX/2+15,posY,dimX/2-15,dimY,true);
	g.drawLine(posX+dimX/2-15,posY,posX+dimX/2+15,posY);
	// observe the -1 in the y-position definition
	g.drawLine(posX+dimX/2-15,posY-1+dimY,posX+dimX/2+15,posY+dimY-1);
	} // close paint method
} // close PlateCarrier class