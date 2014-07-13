package testBob;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;


public class Case1 extends JComponent {
	// The dimensions of the application's window.
	private static final int winWidth=800;
	private static final int winHeight=600;
	// The standard sensor dimension
	int senDim = 10;
	// The list of all the object-parts that compose the machine.
	private static ArrayList<MechPart> mechparts = new ArrayList<MechPart>();
	private static ArrayList<MechPart> actionparts = new ArrayList<MechPart>();
		
	public static void main(String[] args) {
		// invokeLater takes a Runnable object and queues it to be processed by EventDispatcher Thread.
		// The run method will be called after all queued events are processed.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// creates a new frame named testMech
				JFrame f = new JFrame("TestMech");
				// defines the close operation
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// an instance of Case1 is created
				Case1 application = new Case1();
				// sets the size of the application window
				// setPreferredSize is preferred over setSize when a LayoutManager is used
				application.setPreferredSize(new Dimension(winWidth,winHeight));
				
				// Adds the pane where the components will be drawn to the frame
				f.add(new JScrollPane(application),BorderLayout.CENTER);
				
				// Creates the togglebutton
				JToggleButton button = new JToggleButton("Manual", false);
				// creates a new panel with a Flowlayout where the toggle button will be added
				JPanel buttonPanel = new JPanel(new FlowLayout());
				// creates an item listener for the toggle button
				button.addItemListener(application.new ItemHandler(application,button));
				// adds the togglebutton to the buttonpanel
				buttonPanel.add(button);
				// adds the buttonpanel to the southern area of the scrollpane - main window
				f.add(buttonPanel, BorderLayout.SOUTH);
				
				// Causes this Window to be sized to fit the preferred size and layouts of its subcomponents. 
				f.pack();
				// creates the parts of the machine
				application.createParts();
				// centers the window on the screen
				f.setLocationRelativeTo(null);
				// make the frame visible
				f.setVisible(true);
			} // close run method
		}); // close Runnable

	} // close main method
	
	public Case1() {
		this.setOpaque(true);
		// this line is required so the component can be focused
		this.setFocusable(true);
	} // close Case1 constructor method
	
	
	// CreateParts methods creates the higher level objects in the main method.
	// The subcomponents are created inside the higher level objects.
	public void createParts() {
		// get the window dimensions to specify the parts' position
		int winW=winWidth;
		int winH=winHeight;
		// Carrier Lock Sensor -----------------------------------------
		mechparts.add(new Sensor(new String("CL"),new Point(winW-senDim,winH-senDim),
					  new Dimension(senDim,senDim),new Color(228,114,27)));
		// Carrier Start Sensors ---------------------------------------
		for (int i=0; i<6; i++) {
		mechparts.add(new Sensor(new String("SC"+(i+1)), new Point(winW-senDim,215+50*i),
					      new Dimension(senDim,senDim),new Color(30,137,208)));
		} // close loop
		// Carrier End Sensors ------------------------------------------
		for (int i=0; i<6; i++) {
		mechparts.add(new Sensor(new String("EC"+(i+1)), new Point(100,215+50*i),
						  new Dimension(senDim,senDim),new Color(92,79,172)));
		} // close loop
		// Z Motion Sensors ---------------------------------------------
		MechPart temp = new Sensor(new String("ZT"), new Point(20,0),
					  new Dimension(2*senDim,senDim),new Color(75,148,89));
		mechparts.add(temp); actionparts.add(temp);
		for (int i=1; i<7; i++) {
		temp = new Sensor(new String("Z"+(i)), new Point(20,50+50*i),
						  new Dimension(2*senDim,senDim),new Color(75,148,89));
		mechparts.add(temp); actionparts.add(temp);
		} // close loop
		temp = new Sensor(new String("ZB"), new Point(20,400),
				  new Dimension(2*senDim,senDim),new Color(75,148,89));
		mechparts.add(temp); actionparts.add(temp);
		// Carriers -----------------------------------------------------
		for (int i=0; i<6; i++) {
		mechparts.add(new PlateCarrier(new String("carrier"+(i+1)), new Point(610,215+50*i),
					      new Dimension(180,10),new Color(0,100,150)));
		} // close loop
		// Head  --------------------------------------------------------
		// actually all the parts that are moved through the head motion (head sensors and robot)
		// are create inside the head base object
		mechparts.add(new HeadBase(new String("Head"), new Point(110,0), 
						new Dimension(180,10), new Color(133,144,246)));
		
		// The HeadBase contains the HeadPuller, HeadPlunger and HeadMagnet
		// also the HPD and HPU sensors
		// finally the Robot and the start and end robot sensors
		temp=Investigator.find((ArrayList <MechPart>) mechparts, "tag", "Head");
		MechPart temp2=new HeadPuller(new String("hpuller"), new Point(temp.p.x,temp.p.y+55), 
											new Dimension(180,10), new Color(100,116,255));
		temp.extraParts.add(temp2); actionparts.add(temp2);
		temp2=new HeadPlunger(new String("hplunger"), new Point(temp.p.x,temp.p.y+10), 
											new Dimension(180,10), new Color(100,116,255));
		temp.extraParts.add(temp2); actionparts.add(temp2);
		temp2=new HeadMagnet(new String("hmagnet"), new Point(temp.p.x,temp.p.y+105), 
											new Dimension(180,10), new Color(56,76,252));
		temp.extraParts.add(temp2); actionparts.add(temp2);
		temp2=new Robot(new String("robot"), new Point(temp.p.x+575,temp.p.y+105-1), 
										new Dimension(30,30), new Color(172,36,81));
		temp.extraParts.add(temp2); actionparts.add(temp2);
		temp.extraParts.add(new Sensor(new String("RE"), new Point(temp.p.x+65,temp.p.y+125), 
										new Dimension(senDim,senDim), new Color(172,36,81)));
		temp.extraParts.add(new Sensor(new String("RS"), new Point(temp.p.x+605,temp.p.y+125), 
										new Dimension(senDim,senDim), new Color(172,36,81)));
		temp.extraParts.add(new Sensor(new String("HMD"), new Point(temp.p.x,temp.p.y+35), 
										new Dimension(2*senDim,senDim-5), new Color(24,45,181)));
		temp.extraParts.add(new Sensor(new String("HMP"), new Point(temp.p.x+160,temp.p.y+50), 
										new Dimension(2*senDim,senDim-5), new Color(24,45,181)));
		
		// the HeadPuller has only one sensor
		MechPart tempX=Investigator.find((ArrayList <MechPart>) temp.extraParts, "tag", "hpuller");
		tempX.extraParts.add(new Sensor(new String("HPD"), new Point(tempX.p.x,tempX.p.y+5), 
										new Dimension(2*senDim,senDim-5), new Color(24,45,181)));
		
		// HeadMagnet has 2 sensors on it
		tempX=Investigator.find((ArrayList <MechPart>) temp.extraParts, "tag", "hmagnet");
		tempX.extraParts.add(new Sensor(new String("HPU"), new Point(tempX.p.x+160,tempX.p.y+5), 
										new Dimension(2*senDim,senDim-5), new Color(24,45,181)));
		tempX.extraParts.add(new Sensor(new String("HPM"), new Point(tempX.p.x,tempX.p.y+5), 
										new Dimension(2*senDim,senDim-5), new Color(24,45,181)));
		
		// the robot carries 4 sensors which are place in the extraParts array
		tempX=Investigator.find((ArrayList <MechPart>) temp.extraParts, "tag", "robot");
		Color senColor = new Color(172,36,81);
		Point[] senPoints = new Point[4]; // the array that holds the coordinates of the sensors
		senPoints[0]=new Point(tempX.p.x+10, tempX.p.y);
		senPoints[1]=new Point(tempX.p.x+10, tempX.p.y+20);
		senPoints[2]=new Point(tempX.p.x   , tempX.p.y+10);
		senPoints[3]=new Point(tempX.p.x+20, tempX.p.y+10);
		// The sensors are created here (inside robot object and assigned to extraParts array)
		for (int i=0; i<senPoints.length; i++) {
			tempX.extraParts.add(new Sensor(new String("R"+(i+1)), senPoints[i], 
											new Dimension(senDim,senDim), senColor));
			
		} // close loop
		
	} // close createParts method
	
	// paint is the method called by the repaint() method. 
	@Override
	public void paintComponent(Graphics g) { // inherited from JComponent
		// calls the parent paint method
		super.paintComponent(g);
		// repaints the window in order to delete what has been painted before
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		// paints the green rectangle on the left of the panel
		g.setColor(new Color(75,148,89));
		g.fill3DRect(0, 0, 20, 600,true);
		// draws all the parts of the machine
		for (MechPart m : mechparts) {
			m.paint(g);
		} // close loop
		
	} // close paintComponent method
	
	
	// MechPart is defined as abstract and it must be subclassed
	private abstract class MechPart {
		// instances for the specification of the object
		public String tag; // the name of each part
		Point p; // the upper left corner of the object
		Dimension d; // the width and height
		Rectangle b = new Rectangle(); // the rectangle is used to define the area of the object
		Color color; // the color of the object
		
		// an array to hold the sub-objects of the part (e.g. sensors)
		ArrayList <MechPart> extraParts = new ArrayList<MechPart>(); 
		
		// is the variable which declares if the object is selected
		boolean selected; // is true when the object is selected
		boolean moving; // is true when the object moves
		
		// instances for the specification of the object motion
		int speed; // the pixel speed motion
		int offset; // is the total distance measured in pixels that the object can move 
		Point endPos; // is the position where the object stops the motion
		Point initPos; // is the position where the object starts the motion
		int stopPos; // stopPos is the position in the motion "if" condition
					 // it is required mostly at the magnet plate 
		
		public MechPart(String tag, Point p, Dimension d, Color c) {
			this.tag=tag; // the parts name
			specifyPart(p,d,c); // the method is used to define the coordinates of the objects
							// and the area it occupies through Rectangle class
			// if I define it as this.initPos=p; then the change in initPos will affect p
			// immediately as they are actually different references for the same object
			this.initPos=new Point(p.x,p.y);
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
		
		// this method describes the initial conditions of the motion
		public void prepareMotion(){}
		
		public void autoSpeed (int sp, String axis) {
			// we define the position of the magnet plate in respect to the position of the 
			// puller plate, because if the puller is offset then we cannot overrun it
			if (axis.equals("x")) {
				if (p.x < endPos.x) {
					speed=sp;
				} else {
					speed=-sp;
				} // close if
			} else if (axis.equals("y")) {
				if (p.y < endPos.y) {
					speed=sp;
				} else {
					speed=-sp;
				} // close if
			} // close if
		} // close autoSpeed method
		
		// offsetPart method changes the position of the object based on the speed
		public void offsetPart (String axis){
			
			if (axis=="x") {
				p.x=p.x+speed;
			} else if (axis=="y") {
				p.y=p.y+speed;
			} // close if
			// updates the coordinates of the object when the motion stops
			specifyPart(new Point(p.x,p.y), d, color);
			// the position of the subcomponents must also be defined
			defineSubPosition();
			// this repaint calls the paint method of the JComponent and NOT of this part
			repaint();
			
		} // close offsetPart method
		
		public void changeDirection() {
			// the object has moved to the other extreme position. 
			// with this line we change the end position of the object so that 
			// it can move next time to the other extreme position
			Point temp=endPos;
			endPos=initPos;
			initPos=temp;
		} // close changeDirection method
		
		// this method defines the position of an object's subcomponents if any
		// in respect to the parent object position
		public void defineSubPosition(){}
		
		public void stopMotion (MechPart refPart) {
			// we have to declare that the object stopped moving
			refPart.moving=false;
		} // close stopMotionTimer method
		
		
	} // close MechPart class
	
	
	private class Sensor extends MechPart {
		
		public Sensor(String tag,Point p, Dimension d, Color color) {
			super(tag, p, d, color); // calls the constructor of the super class
		} // close Sensor constructor method
		
		@Override
		public void paint(Graphics g) {
			// calls the draw method of the superclass, else it would only call the commands specified in the subclass
			super.paint(g); 
			g.fill3DRect(p.x,p.y,d.width,d.height,true);
		} // close paint method
		
	} // close Sensor class
	
	
	private class PlateCarrier extends MechPart {
		
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
	
	
	private class Robot extends MechPart {
		MechPart carrier = null;
		
		public Robot(String tag, Point p, Dimension d, Color color) {
			super(tag, p, d, color); // calls the constructor of the super class
		} // close Robot constructor method
		
		@Override
		public void defineSubPosition() {
			super.defineSubPosition();
			// carrier holds the carrier object if the robot has selected one
			carrier=handleCarriers();
			Point[] senPoints = new Point[4]; // the array that holds the coordinates of the sensors
			senPoints[0]=new Point(p.x+10, p.y+1);
			senPoints[1]=new Point(p.x+10, p.y+20+1);
			senPoints[2]=new Point(p.x	 , p.y+10+1);
			senPoints[3]=new Point(p.x+20, p.y+10+1);
			for (int i=0; i<senPoints.length; i++) {
				extraParts.get(i).specifyPart(senPoints[i], new Dimension(senDim,senDim),extraParts.get(i).color);
			} // close loop
			
			if (carrier!=null) {
				Point carrierPos = new Point(p.x-carrier.d.width/2+this.d.width/2,carrier.p.y);
				Dimension d = carrier.d;
				carrier.specifyPart(carrierPos, d, carrier.color);
			} // close if
		} // close defineSubPosition method
		
		public MechPart handleCarriers(){
			MechPart temp2 = null; // required to temporarily store the carrier object
			MechPart carrier = null; // required to store the final output value of the selected carrier object
			HeadBase temp = (HeadBase) Investigator.find((ArrayList <MechPart>) mechparts, "tag", "Head");
			
			// choose a carrier if the head is in the proper vertical sensor position
			if (temp.vertPosition>0 && temp.vertPosition<7) {
				// calls the investigator to find the object with the specified abilities
				temp2 = Investigator.find((ArrayList <MechPart>) mechparts, 
						"tag", "carrier"+temp.vertPosition);
				// this boolean variable is required so that the carrier is only selected when 
				// the robot is inside the carrier, not only when the base is in the respective position
				boolean check = this.b.intersects(temp2.b);
				if (check) {
				carrier = temp2;
				} // close if
			} // close if
			
			return carrier;
		} // close handle Carriers method
		
		@Override
		public void paint(Graphics g) {
			// calls the draw method of the superclass, else it would only call the commands specified in the subclass
			super.paint(g); 
			// drawRect is different from fillRect
			g.draw3DRect(p.x,p.y,d.width,d.height,true);
			
			// we draw the extra parts of the robot (here the sensors)
			for (MechPart i : extraParts) {
			i.paint(g);	
			
			// without this "if" the carrier cannot be painted in the last loop so there is 
			// an offset of 5 pixels
			if (carrier!=null) {
			carrier.paint(g);
			} // close if
			
			} // close loop
			
		} // close paint method
		
		
		@Override
		public void prepareMotion(){
			// the extreme positions of the motion are initialized if they haven't already
			if (offset==0){
				MechPart temp = Investigator.find((ArrayList <MechPart>) mechparts, "tag", "Head");
				MechPart re = Investigator.find((ArrayList <MechPart>) temp.extraParts, "tag", "RE");
				MechPart rs = Investigator.find((ArrayList <MechPart>) temp.extraParts, "tag", "RS");
				offset = rs.p.x-re.p.x-re.d.width-d.width;
				endPos = new Point(p.x-offset,p.y);
			} // close if
						
			stopPos=endPos.x; // defines the position where the object stops
			
		} // close prepareMotion method
		
		public void stopMotion (MechPart refPart) {
			super.stopMotion(refPart);
			// we change the direction, so next time the object knows that it has to move to
			// the opposite position
			changeDirection();
		} // close stopMotion method
		
	} // close Robot class
	
	
	private class HeadPuller extends MechPart {
				
		public HeadPuller(String tag, Point p, Dimension d, Color color) {
			super(tag, p, d, color); // calls the constructor of the super class
			this.offset = 5;
			this.endPos = new Point(p.x,p.y+this.offset);
		} // close HeadPuller constructor method
		
		public void defineSubPosition() {
			super.defineSubPosition();
			MechPart temp = Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "HPD");
			temp.specifyPart(new Point(p.x,p.y+5), temp.d, temp.color);
		} // close definePosition method
		
		@Override
		public void paint(Graphics g) {
			// calls the paint method of the superclass, else it would only call the commands specified in the subclass
			super.paint(g); 
			
			// the following point-arrays are required to draw a polygon shaped object
			int posX=p.x; int posY=p.y;
			int[] xPos = new int[6];
			int[] yPos = new int[6];
			xPos[0]=posX; xPos[1]=posX+180; xPos[2]=posX+180; xPos[3]=posX+20; xPos[4]=posX+20; xPos[5]=posX; 
			yPos[0]=posY; yPos[1]=posY; yPos[2]=posY+10; yPos[3]=posY+10; yPos[4]=posY+5; yPos[5]=posY+5; 
			g.fillPolygon(xPos,yPos,xPos.length);
			
			// the extra parts are drawn (sensor)
			for (MechPart i : extraParts) {
				i.paint(g);	
			} // close loop
		} // close paint method
		
		public void prepareMotion() {
			stopPos=endPos.y; // defines the position where the object stop
		} // close prepareMotion method
		
		public void stopMotion (MechPart refPart) {
			super.stopMotion(refPart);
			// we change the direction, so next time the object knows that it has to move to
			// the opposite position
			changeDirection();
		} // close stopMotion method
		
	} // close HeadPuller class
	
	private class HeadPlunger extends MechPart {
				
		public HeadPlunger(String tag, Point p, Dimension d, Color color) {
			super(tag, p, d, color); // calls the constructor of the super class
			this.offset=15;
			this.endPos= new Point(p.x,p.y+this.offset);
		} // close HeadPlunger constructor method
		
		@Override
		public void paint(Graphics g) {
			// calls the draw method of the superclass, else it would only call the commands specified in the subclass
			super.paint(g); 
			g.fillRect(p.x,p.y,d.width,d.height);
		} // close draw method
		
		public void prepareMotion () {
			stopPos=endPos.y; // defines the position where the object stop
		} // close prepareMotion method
		
		public void stopMotion (MechPart refPart) {
			super.stopMotion(refPart);
			// we change the direction, so next time the object knows that it has to move to
			// the opposite position
			changeDirection();
		} // close stopMotion method
		
	} // close HeadPlunger class
	
	
	private class HeadMagnet extends MechPart {
		
		public HeadMagnet(String tag, Point p, Dimension d, Color color) {
			super(tag, p, d, color); // calls the constructor of the super class
			offset=0;
			endPos= new Point(p.x,p.y-this.offset);
		} // close HeadMagnet constructor method
		
		@Override
		public void defineSubPosition() {
			MechPart temp=Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "HPU");
			temp.specifyPart(new Point(p.x+160,p.y+5), temp.d,temp.color);
			temp=Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "HPM");
			temp.specifyPart(new Point(p.x,p.y+5), temp.d,temp.color);
		} // close definePosition method
		
		@Override
		public void paint(Graphics g) {
			// calls the draw method of the superclass, else it would only call the commands specified in the subclass
			super.paint(g); 
			// the following point-arrays are required to draw a polygon shaped object
			int posX=this.p.x; int posY=this.p.y;
			int[] xPos = new int[8];
			int[] yPos = new int[8];
			xPos[0]=posX; xPos[1]=posX+180; xPos[2]=posX+180; xPos[3]=posX+160; xPos[4]=posX+160; xPos[5]=posX+20;
			xPos[6]=posX+20; xPos[7]=posX; 
			yPos[0]=posY; yPos[1]=posY; yPos[2]=posY+5; yPos[3]=posY+5; yPos[4]=posY+10; yPos[5]=posY+10; 
			yPos[6]=posY+5; yPos[7]=posY+5; 
			g.fillPolygon(xPos,yPos,xPos.length);

			// the extra parts are drawn (sensors)
			for (MechPart i : extraParts) {
				i.paint(g);	
			} // close loop
			
		} // close draw method
		
		public void prepareMotion() {
			MechPart temp = null; // temp variable where the reference of the HeadPuller will be stored
			
		// The following "if" assigns the hpuller to the temp variables
			if (temp==null){ // checks if any object is assigned to variable temp
				for (MechPart e: mechparts) { // this "for" finds the head
					if (e.tag.equals("Head")) {
						temp=e;
						break;
					} // close if
				} // close loop
				
				for (MechPart e: temp.extraParts) { // this "for" finds the hpuller from the head extraparts
					if (e.tag.equals("hpuller")) {
						temp=e;
						break;
					} // close if
				} // close loop
			} // close if
			
		// the extreme positions of the motion are initialized if they haven't already
			if (offset==0){
				// offset is defined in respect to hpuller
				offset = p.y-temp.p.y-temp.d.height;
				endPos = new Point(p.x,p.y-offset);
			} // end if
			
		// we define the position of the magnet plate in respect to the position of the 
			// puller plate, because if the puller is offset then we cannot overrun it
			if (p.y < endPos.y) {
				stopPos=endPos.y;
			} else {
				offset=p.y-temp.p.y-temp.d.height;
				stopPos=p.y-offset;
			} // close if
		} // close prepareMotion method
		
		public void stopMotion (MechPart refPart) {
			super.stopMotion(refPart);
			// we change the direction, so next time the object knows that it has to move to
			// the opposite position
			changeDirection();
		} // close stopMotion method
		
	} // close HeadMagnet class
	
	private class HeadBase extends MechPart {
		
		int vertPosition=0; // vertPosition is required to check if the robot is in a proper
							// position to hold a carrier
		
		public HeadBase(String tag, Point p, Dimension d, Color color) {
			super(tag, p, d, color); // calls the constructor of the super class	
		} // close HeadBase constructor method

		public void defineSubPosition() {
			// Define the endPositions of the head plates.
			MechPart temp=Investigator.find((ArrayList <MechPart>) extraParts, "tag", "hpuller");
			temp.endPos.y=temp.endPos.y+speed;
			temp.initPos.y=temp.initPos.y+speed;
			temp=Investigator.find((ArrayList <MechPart>) extraParts, "tag", "hplunger");
			temp.endPos.y=temp.endPos.y+speed;
			temp.initPos.y=temp.initPos.y+speed;
			temp=Investigator.find((ArrayList <MechPart>) extraParts, "tag", "hmagnet");
			temp.endPos.y=temp.endPos.y+speed;
			temp.initPos.y=temp.initPos.y+speed;
			
			for (MechPart i : extraParts) {
				i.specifyPart(new Point(i.p.x, i.p.y+speed), i.d, i.color);
			} // close loop
			
		} // close definePosition method
		
		@Override
		public void paint(Graphics g) {
			// calls the draw method of the superclass, else it would only call the commands specified in the subclass
			super.paint(g);
			
			// extraParts 4 and 5 are the start and end sensors of the robot
			MechPart temp=Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "RE");
			g.setColor(temp.color); 
			MechPart temp2=Investigator.find((ArrayList <MechPart>) this.extraParts, "tag", "RS");
			// the line where the robot moves
			g.drawLine(temp.p.x+10, temp.p.y+10-1, temp2.p.x, temp2.p.y+10-1);
			
			// paints the SOLID head base parts 
			g.setColor(this.color);
			int posX=p.x; int posY=p.y;
			g.fillRect(posX, posY, d.width, d.height); // the upper part of the head (above the plungers)
			// the coordinates for the creation of the polygon - static part of the HeadBase
			int[] xPos = new int[8];
			int[] yPos = new int[8];
			xPos[0]=posX; xPos[1]=posX+20; xPos[2]=posX+20; xPos[3]=posX+180; 
			xPos[4]=posX+180; xPos[5]=posX+160; xPos[6]=posX+160; xPos[7]=posX; 
			yPos[0]=posY+40; yPos[1]=posY+40; yPos[2]=posY+35; yPos[3]=posY+35; 
			yPos[4]=posY+50; yPos[5]=posY+50;  yPos[6]=posY+55; yPos[7]=posY+55; 
			g.fillPolygon(xPos,yPos,xPos.length);
						
			// Draws the HeadPuller-Plunger-Magnet, Robot, Head and Robot Sensors
			for (MechPart i : extraParts) {
				i.defineSubPosition();
				i.paint(g);	
			} // close loop
			
		} // close paint method
		
		public void prepareMotion(MechPart Part) {
			final MechPart selPart=Part;
			stopPos=selPart.p.y; // defines the position where the object stop
		} // close prepareMotion method
		
		public void autoSpeed (MechPart Part, int sp) {
			final MechPart selPart=Part;
			if (p.y > selPart.p.y) {
				speed=-sp;
			} else {
				speed=sp;
			} // close if
		} // close prepareMotion method
		
		public void stopMotion (MechPart refPart) {
			super.stopMotion(refPart);
			// tempX is the top Z sensor
			MechPart tempX = Investigator.find((ArrayList <MechPart>) mechparts, "tag", "ZT");
			// vertPosition is the place of the head in respect to Zsensors (1-6)
			// it is required for the handling of the plate carriers
			vertPosition = mechparts.indexOf(refPart) - mechparts.indexOf(tempX);
		} // close stopMotion method
		
	} // close HeadBase class
	
	
	// ItemHandler defines the actions when the togglebutton is pushed
	public class ItemHandler implements ItemListener {
		JToggleButton button; // the button that triggers the itemEvent
		KeyHandler keySel;
		MouseHandler mouseSel;
		Case1 parent; // the parent is required to add the mouselistener
		
		public ItemHandler(Case1 parent, JToggleButton button) {
			// initially the conditions are "auto", so we add the mouselistener
			this.button=button;
			this.parent=parent;
			mouseSel=new MouseHandler();
			parent.addMouseListener(mouseSel);
		} // close ItemHandler constructor
		
		// if the button is selected we are in "manual" mode else in "auto" mode
		public void itemStateChanged(ItemEvent ie) {
			if (this.button.isSelected()) { 	// MANUAL MODE
				button.setText("Auto");
				keySel=new KeyHandler(mouseSel.selection);
				requestFocus();
				parent.addKeyListener(keySel);
				mouseSel.mode="Manual";
				mouseSel.selection.deselect();
			} else { 							// AUTO MODE	
				button.setText("Manual");
				mouseSel.mode="Auto";
				mouseSel.selection.deselect();
				parent.removeKeyListener(keySel);
			} // close if
		} // close itemStateChanged method
		
	} // close ItemListener class
	
	
	public class MouseHandler extends MouseAdapter {
		Selection selection = new Selection(); // selection is the instance that does the selection of the objects 
		String mode="Auto";
		
		public void mousePressed(MouseEvent e) {
		
		if (mode=="Auto") {
			autoMode(e);
		} else if (mode=="Manual") {
			manualMode(e);
		} // close if
			
		} // close mousePressed method
		
		public void autoMode (MouseEvent e) {
			// we need an oldPart to assign the previous selection made by the user.
			MechPart oldPart=selection.selPart;
			
			if (oldPart==null){ // if there is no part selected before
				selection.select(e.getPoint()); // select a new part
				if (selection.selPart != null) { // if a new part is indeed selected (we may have clicked in empty space)
					selection.flash(); // make the part flash
					selection.selPart.moving=true; // set the part moving field to true
					Motion motion = new Motion(selection); // make the part move through the initialization of motion class
					motion.autoMode();
				} // close if
			} else { // if there is a part selected previously
				if (oldPart.moving==false) { // if the previous part is not moving then
					selection.select(e.getPoint()); // we are allowed to make a new selection
						if (selection.selPart != null) { // if a new part is indeed selected (we may have clicked in empty space)
						selection.flash(); // make the part flash
						selection.selPart.moving=true; // set the part moving field to true
						Motion motion = new Motion(selection); // make the part move through the initialization of motion class
						motion.autoMode();
						} //close if
				} else { // if we have clicked in empty space
					selection.selPart=oldPart;
				} // close if
			} // close if
		} // close autoMode method
		
		public void manualMode (MouseEvent e) {
			// we need an oldPart to assign the previous selection made by the user.
			MechPart oldPart=selection.selPart;
			if (oldPart==null){ // if there is no part selected before
				selection.select(e.getPoint()); // select a new part
				if (selection.selPart != null) { // if a new part is indeed selected (we may have clicked in empty space)
					selection.flash(); // make the part flash
					} // close if
			} else { // if there is a part selected previously
				selection.select(e.getPoint()); // we are allowed to make a new selection
					if (selection.selPart != null) { // if a new part is indeed selected (we may have clicked in empty space)
					selection.flash(); // make the part flash
					} //close if
			} // close if
		} // close manualMode method
		
	} // close MouseHandler class


	public class KeyHandler extends KeyAdapter {
		Selection selection;
		
		public KeyHandler(Selection selection) {
			this.selection=selection;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			MechPart selPart=selection.selPart;
		if (selPart!=null) {	
			if (selPart.tag.charAt(0)=='Z') {
				final HeadBase temp = (HeadBase) Investigator.find((ArrayList <MechPart>) mechparts, "tag", "Head");
				if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) {
					manualMotion(temp, selPart, -2, "y");
				} // close if
				if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) {
					manualMotion(temp, selPart, 2, "y");
				} // close if
			} else if (selPart.tag.equals("hpuller")) {
				if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) {
					manualMotion(selPart, selPart, -1, "y");
				} // close if
				if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) {
					manualMotion(selPart, selPart, 1, "y");
				} // close if
			} else if (selPart.tag.equals("hplunger")) {
				if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) {
					manualMotion(selPart, selPart, -1, "y");
				} // close if
				if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) {
					manualMotion(selPart, selPart, 1, "y");
				} // close if
			} else if (selPart.tag.equals("hmagnet")) {
				if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) {
					manualMotion(selPart, selPart, -1, "y");
				} // close if
				if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) {
					manualMotion(selPart, selPart, 1, "y");
				} // close if
			} else if (selPart.tag.charAt(0)=='r') {
				if (e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT) {
					manualMotion(selPart, selPart, -5, "x");
				} else if (e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT) {
					manualMotion(selPart, selPart, 5, "x");
				} // close if
			} // close if
		} // close if
			
		} // close keyPressed method
		
		public void manualMotion(MechPart base, MechPart ref, int sp, String axis) {
			int lowLim = 0; int upLim = 0;
			if (base.equals(ref)) {
				base.prepareMotion();
			} else {
				HeadBase temp=(HeadBase) base;
				temp.prepareMotion(ref);
			} // close if
			
			if (axis.equals("x")) {
				if (base.initPos.x<base.stopPos) {
					lowLim=base.initPos.x;
					upLim=base.stopPos;
				} else {
					lowLim=base.stopPos;
					upLim=base.initPos.x;
				} // close if
				
				if (sp>0) {
					if (base.p.x<upLim && base.p.x>=lowLim) {
					base.speed=sp;
					base.offsetPart(axis);
					base.stopMotion(ref);
					} // close if
				} else {
					if (base.p.x<=upLim && base.p.x>lowLim) {
					base.speed=sp;
					base.offsetPart(axis);
					base.stopMotion(ref);
					} // close if
				} // close if
				
			} else if (axis.equals("y")) {

				if (base.initPos.y<base.stopPos) {
					lowLim=base.initPos.y;
					upLim=base.stopPos;
				} else {
					lowLim=base.stopPos;
					upLim=base.initPos.y;
				} // close if
				
				if (sp>0) {
					if (base.p.y<upLim && base.p.y>=lowLim) {
					base.speed=sp;
					base.offsetPart(axis);
					base.stopMotion(ref);
					} // close if
				} else {
					if (!base.equals(ref)) {
						MechPart temp = Investigator.find((ArrayList <MechPart>) mechparts, "tag", "ZB");
						upLim=temp.p.y;
					} // close if
					if (base.p.y<=upLim && base.p.y>lowLim) {
					base.speed=sp;
					base.offsetPart(axis);
					base.stopMotion(ref);
					} // close if
				} // close if
			} // close if
		} // close manualMotion method
		
	} // close KeyHandler class
	
	
	public class Selection {
		MechPart selPart; // the part selected
		ActionListener flashTask; // the listener that makes the objects to flash
		Timer flashtimer; // the timer that schedules the flashing
		Color trueColor; // the initial object color
		
		public void deselect() {
		// initially the previous selection must be nullified so that the data of the new
		// element are assigned
		if (selPart!=null) {
			flashtimer.stop(); // the flashtimer must stop
			selPart.color=trueColor; // the color of the previously selected part must be set to the initial value
			repaint(); // this repaint is required to return the part in the original color
			selPart.selected=false; // the part selected turns to false
			selPart=null; // the selPart is nullified
		} // close if
		} // close deselect method
		
		// select method checks if the mouse is pressed inside the borders of an object
		// and assigns it to selPart field
		public void select(Point chosenP) {
		
		deselect();
		
		// the code iterates through all the actionparts and if the mouse clicked in one of them
		for (MechPart part : actionparts) {
			if (part.b.contains(chosenP)) {
			part.selected=true; // the "selected" field becomes true
			selPart=part; // the part is assigned into "selPart" field
			trueColor=part.color; // trueColor field takes the initial color for the part selected
			} // close if	
		} // close loop
			
		} // close select method
		
		// flash method makes the selected objects flash
		public void flash() {
			
		if (flashTask==null) { // If there is no actionlistener assigned
		flashTask = new ActionListener() { 
		boolean state; // state variable is used to check whether the object color becomes brighter or not
		
		public void actionPerformed(ActionEvent e) {
		if (state==false) {
			selPart.color=selPart.color.brighter(); // make the color brighter
			repaint();
			state=true; 
		} else {
			selPart.color=trueColor;
			repaint();
			state=false;
		} // close if
		} // close ActionPerformed
		}; // close taskPerformer
		} // close if
		
		// start the timer
		flashtimer = new Timer(200,flashTask);
		flashtimer.start();
		} // close flash method
		
	} // close Selection class
	
	
	public class Motion {
		MechPart selPart;
		Timer motiontimer;
		ActionListener motionTask;
		
		public Motion (Selection selection){
			this.selPart=selection.selPart;
		} // close Motion constructor
		
			
		public void autoMode () {
			if (selPart.tag.charAt(0)=='Z') {
				final HeadBase temp = (HeadBase) Investigator.find((ArrayList <MechPart>) mechparts, "tag", "Head");
				// temp is required to find the position where the headBase will stop
				temp.prepareMotion(selPart);
				temp.autoSpeed(selPart, 2);
				autoTask(temp, selPart, "y"); 
			} else if (selPart.tag.equals("hpuller")) {
				selPart.prepareMotion();
				selPart.autoSpeed(1, "y");
				autoTask(selPart, selPart, "y");
			} else if (selPart.tag.equals("hplunger")) {
				selPart.prepareMotion();
				selPart.autoSpeed(1, "y");
				autoTask(selPart, selPart, "y");
			} else if (selPart.tag.equals("hmagnet")) {
				selPart.prepareMotion();
				selPart.autoSpeed(1, "y");
				autoTask(selPart, selPart, "y");
			} else if (selPart.tag.charAt(0)=='r') {
				selPart.prepareMotion();
				selPart.autoSpeed(5, "x");
				autoTask(selPart, selPart, "x");
			} // close if
		} // close autoMode method
			
		public void autoTask(MechPart basePart, MechPart refPart, String axis) {
			final MechPart ref = refPart;
			final MechPart base = basePart;
			final String axisF = axis;
			
			motionTask = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			int startPos=0;
			if (axisF=="x") {
				startPos=base.p.x;
			} else if (axisF=="y") {
				startPos=base.p.y;
			} // close if
				
			if (startPos!=base.stopPos){
				base.offsetPart(axisF);
			} else {
				base.stopMotion(ref);
				motiontimer.stop();
			} // close if
			} // close ActionPerformed
			}; // close taskPerformer
			
			motiontimer = new Timer(20,motionTask);
			motiontimer.start();
		} // close autoMode method
		
	} // close Motion class
	
	
	/* Investigator class is used to find elements in Lists with specific attributes
	 * to achieve that:
	 *  it iterates through an ArrayList
	 *  it gets the class of the specific item in the list
	 *  it gets the field specified with the name given
	 *  it compares it with the value given 
	 *  The find method is overloaded for each type of value
	 *  The above described technique is called reflection 
	 *  
	 *  It is important to state that in getField method the specified field MUST be PUBLIC 
	 */
	public static class Investigator {
		
		public static <Kind> Kind find (ArrayList<Kind> coll, String field, String value) {
			Kind answer = null;
			for (Kind obj: coll){
				try {
					if (obj.getClass().getField(field).get(obj).equals(value)) {
						answer = obj;
					} // close if
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			} // close loop
			return answer;
		} // close find method
		
		public static <Kind> Kind find (ArrayList<Kind> coll, String field, char value) {
			Kind answer = null;
			for (Kind obj: coll){
				String temp = null;
				try {
					temp = (String) obj.getClass().getField(field).get(obj);
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					if (temp.charAt(0)==value) {
						answer = obj;
					} // close if
				
			} // close loop
			return answer;
		} // close find method
		
		public static <Kind> Kind find (ArrayList<Kind> coll, String field, int value) {
			Kind answer = null;
			for (Kind obj: coll){
				int temp = 0;
				try {
					temp = obj.getClass().getField(field).getInt(obj);
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (temp==value) {
					answer = obj;
				} // close if
				
			} // close loop
			return answer;
		} // close find method
		
	} // close Investigator class
	
} // close Case1 class

