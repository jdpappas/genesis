package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

	// MechPart is defined as abstract and it must be subclassed
	public abstract class MechPart {
	/* MechPart is the class where the object geometric and structural characteristics are 
	 * defined. NO MOTION characteristics are defined her. 
	 */
		// instances for the specification of the object
		protected String tag; // the name of each part
		protected Point p; // the upper left corner of the object
		protected Dimension d; // the width and height
		protected Rectangle b = new Rectangle(); // the rectangle is used to define the area of the object
									   //the main purpose of "b" is to be able to select the object with the mouse
		protected Color color; // the color of the object
		
		// an array to hold the sub-objects of the part (e.g. sensors)
		protected ArrayList <MechPart> extraParts = new ArrayList<MechPart>(); 
		
		protected boolean selected; // is true when the object is selected
		
		public MechPart (String tag, Point p, Dimension d, Color c) {
			this.tag=tag; // the parts name
			specifyPart(p,d,c); // the method is used to define the coordinates of the objects
								// and the area it occupies through Rectangle class
		} // close MechPart constructor method
		
		public void specifyPart(Point p, Dimension d, Color color){
			this.color=color;
			this.p=p; // the parts upper left corner coordinates
			this.d=d; // the parts width and height
			this.b.setBounds(p.x,p.y,d.width,d.height);
		} // close setBoundary method
		
		// the paint method will be overridden from the subclasses
		// this paint method is NOT called by the repaint method
		public void paint(Graphics g) {
			g.setColor(this.color);
		} // close paint method
		
		// this method defines the position of an object's subcomponents if any
		// in respect to the parent object position
		protected void defineSubPosition(int speed, String axis){}

		
		
		// GETTERS & SETTERS ----------------------------------------------------------------------
		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

	

		
		
		
		
	} // close MechPart class
