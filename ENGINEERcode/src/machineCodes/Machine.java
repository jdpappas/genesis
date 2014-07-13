package machineCodes;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;


public class Machine extends JFrame {

	private static InteractionPanel drawPanel; // The panel where the application is drawn.
	private static ControlPanel controlPanel; // The panel where the control buttons are gathered.
	private static SerialController serialController;
	private static SerialOutputController soc;
	
	// The list of all the object-parts that compose the machine.
	private static ArrayList<MechPart> mechparts = new ArrayList<MechPart>();
	private static ArrayList<MovingPart> actionparts = new ArrayList<MovingPart>();
	private static ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	
	
	public static void main(String[] args) {
		// invokeLater takes a Runnable object and queues it to be processed by EventDispatcher Thread.
		// The run method will be called after all queued events are processed.
		// This is standard for the GUI applications.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(
                    		new SyntheticaSimple2DLookAndFeel());
                                // "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                                // UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // close try-catch block
				// creates a new frame named 
				new Machine("SampleX");
			} // close run method
		}); // close Runnable
		
	} // close main method
	
	
	public Machine (String name) {
	super(name);
	// Creates the parts of the machine
	createParts();
	
	// Creates the GUI
	visualize();
	
	// Initializes the serialController required for the communication with the serial port
	serialController = new SerialController(this);
	serialController.initialize();
	
	// With this class a new Thread is created to ONLY send data to the serial port
	soc=new SerialOutputController(serialController);
	soc.start();
	soc.setSelection(getDrawPanel().controller.getSelection());
	
	} // close Machine constructor method
	
	private void visualize() {
		// defines the close operation of the window.
		// DISPOSE_ON_CLOSE just closes the window. This is needed when this interface opens
		// from the BIOcode. If it was set to EXIT_ON_CLOSE then the entire application would close.
		// What we need in BIOcode is to close the window and NOT the application.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		
		// =========================================================================================
		// Create frame components -----------------------------------------------------------------
		setDrawPanel(new InteractionPanel(this));
		// Adds the panel where the components will be drawn to the frame
		add(getDrawPanel(),BorderLayout.CENTER);
		// This method is responsible for creating a Controller instance
		getDrawPanel().initializeController();
		// creates the ControlPanel required for the control buttons that may be added
		controlPanel = new ControlPanel(this);
		// adds the buttonpanel to the southern area of the scrollpane - main window
		add(controlPanel, BorderLayout.SOUTH);
		// End of component creation and assignment ------------------------------------------------
		
		// Causes this Window to be sized to fit the preferred size and layouts of its subcomponents. 
		pack();
		// centers the window on the screen
		setLocationRelativeTo(null);
		/* the frame visibility can be handled in the following ways:
		 * 1. made visible here (useful in the developing period)
		 * 2. made visible after the port is selected (in the serial controller)
		 * 3. made visible from the commercial interface when the user selects it
		 * 	  (this will be the final way of selecting visibility of this frame)
		 */
		setVisible(true);
	} // close visualize method
	
	
	// createParts methods creates the higher level parts.
	private void createParts() {
	// get the window dimensions to specify the parts' position
	int winW=InteractionPanel.getWinwidth();
	int winH=InteractionPanel.getWinheight();
	int senDim = 10; // The standard sensor dimension
	HeadBase base; PlateCarrier carrier; Sensor sensor;
	
	
	// Carriers ---------------------------------------------------------------------------------
	ArrayList<PlateCarrier> carriers = new ArrayList<PlateCarrier>();
	for (int i=0; i<6; i++) {
	// create i-th the carrier
	carrier=new PlateCarrier(new String("carrier"+(i+1)), new Point(610,215+50*i),
				      new Dimension(180,10),new Color(0,100,150));
	mechparts.add(carrier); 
	carriers.add(carrier);
	} // close loop
	
	// Carrier Lock Sensor --------------------------------------------------------------------
	sensor=new Sensor(new String("CL"),new Point(winW-senDim,winH-senDim),
				  new Dimension(senDim,senDim),new Color(228,114,27));
	mechparts.add(sensor);
	sensors.add(sensor);
	
	
	// Head  ----------------------------------------------------------------------------------
	// actually all the parts that are moved through the head motion (head sensors and robot)
	// are assigned in the HeadBase object so that they can move through it
	base=new HeadBase(new String("Head"), new Point(110,0), 
			new Dimension(180,105), new Color(133,144,246), 
			"y", 1, 400, "Z", 1600, 3200); 

	mechparts.add(base); actionparts.add((HeadBase) base);
	// assigns the carriers with the setCarriers method in order to determine if one is selected 
	base.setCarriers(carriers);
	
	// HeadPuller, HeadPlunger, HeadMagnet and HeadRotator -----------------------------------
	HeadPuller hpuller=new HeadPuller(new String("hpuller"), new Point(base.p.x,base.p.y+55), 
										new Dimension(180,10), new Color(100,116,255),
										"y", 1, 5, "HPu", 1600, 3200); 
	base.extraParts.add(hpuller); actionparts.add(hpuller);
	
	HeadPlunger hplunger=new HeadPlunger(new String("hplunger"), new Point(base.p.x,base.p.y+10), 
										new Dimension(180,10), new Color(100,116,255), 
										"y", 1, 15, "HPl", 1600, 3200); 
	base.extraParts.add(hplunger); actionparts.add(hplunger);
	
	HeadMagnet hmagnet=new HeadMagnet(new String("hmagnet"), new Point(base.p.x,base.p.y+105), 
										new Dimension(140,10), new Color(56,76,252), 
										"y", 1, 40, "HM", 1600, 3200); 
	base.extraParts.add(hmagnet); actionparts.add(hmagnet);
	
	HeadRotator hrotator=new HeadRotator(new String("hrotator"), new Point(hmagnet.p.x+140,hmagnet.p.y), 
										new Dimension(10, 10), new Color(206,88,52), 
										"x", 1, 20, "HMR", 1600, 3200); 
	hmagnet.extraParts.add(hrotator); actionparts.add(hrotator);
	
	// connects the puller and the magnet so that they configure their steps 
	// and do not collide
	hpuller.setMagnet((HeadMagnet) hmagnet);
	hmagnet.setPuller((HeadPuller) hpuller);
	
	
	// Robot ----------------------------------------------------------------------------------
	Robot robot=new Robot(new String("robot"), new Point(base.p.x+575,base.p.y+105), 
									new Dimension(30,30), new Color(223,178,178), 
									"x", 1, 500, "X", 1600, 3200); // The last 2 are speed and steps
									// initially the steps where 100
	base.extraParts.add(robot); actionparts.add(robot);
	
	// The START and END robot sensors
	sensor=new Sensor(new String("RE"), new Point(base.p.x+65,base.p.y+125), 
									new Dimension(senDim,senDim), new Color(172,36,81));
	base.extraParts.add(sensor);
	sensors.add(sensor);
	robot.lowSensors.add(sensor);
	sensor=new Sensor(new String("RS"), new Point(base.p.x+605,base.p.y+125), 
									new Dimension(senDim,senDim), new Color(172,36,81));
	base.extraParts.add(sensor);
	sensors.add(sensor);
	robot.highSensors.add(sensor);
	
	// HPD is the HeadPlungerDown sensor
	sensor=new Sensor(new String("HPD"), new Point(base.p.x,base.p.y+35), 
									new Dimension(2*senDim,senDim-5), new Color(24,45,181));
	hplunger.highSensors.add(sensor);
	base.extraParts.add(sensor);
	sensors.add(sensor);
	
	// HPU is the HeadPullerUp sensor
	sensor=new Sensor(new String("HPU"), new Point(base.p.x+160,base.p.y+50), 
									new Dimension(2*senDim,senDim-5), new Color(24,45,181));
	base.extraParts.add(sensor);
	sensors.add(sensor);
	hpuller.lowSensors.add(sensor);
	
	// the HeadPuller has only one sensor (HPM:HeadPullerMagnet sensor)
	sensor = new Sensor(new String("HPM"), new Point(hpuller.p.x,hpuller.p.y+5), 
									new Dimension(2*senDim,senDim-5), new Color(24,45,181));
	hpuller.extraParts.add(sensor);
	sensors.add(sensor);
	hpuller.highSensors.add(sensor);
	hmagnet.lowSensors.add(sensor);
	
	// HeadMagnet has 2 sensors on it 
	// HMP:HeadMagnetPhotoresistor sensor
	sensor=new Sensor(new String("HMP"), new Point(hrotator.p.x+30,hmagnet.p.y), 
									new Dimension(senDim,senDim), new Color(168,73,0));
	hrotator.extraParts.add(sensor);
	sensors.add(sensor);
	
	// HMD: HeadMagnetDown sensor
	sensor=new Sensor(new String("HMD"), new Point(hmagnet.p.x,hmagnet.p.y+5), 
									new Dimension(2*senDim,senDim-5), new Color(24,45,181));
	hmagnet.extraParts.add(sensor);
	sensors.add(sensor);
	base.lowSensors.add(sensor);
	
	
	// the robot carries 4 sensors which are place in the extraParts array
	Color senColor = new Color(172,36,81);
	Point[] senPoints = new Point[4]; // the array that holds the coordinates of the sensors
	senPoints[0]=new Point(robot.p.x+10, robot.p.y);    // Upper robot sensor
	senPoints[1]=new Point(robot.p.x+10, robot.p.y+20); // Lower robot sensor
	senPoints[2]=new Point(robot.p.x,    robot.p.y+10); // Left  robot sensor
	senPoints[3]=new Point(robot.p.x+20, robot.p.y+10); // Right robot sensor
	// The sensors are created here (inside robot object and assigned to extraParts array)
	for (int i=0; i<senPoints.length; i++) {
		sensor=new Sensor(new String("R"+(i+1)), senPoints[i], 
										new Dimension(senDim,senDim), senColor);
		robot.extraParts.add(sensor); sensors.add(sensor);
		if (i==0) {
			base.lowSensors.add(sensor);
		} else if (i==1) {
			base.highSensors.add(sensor);
		} else if (i==2) {
			robot.highSensors.add(sensor);
		} else if (i==3) {
			robot.lowSensors.add(sensor);
		} // close if
	} // close loop

	// Start and Stop sensors -------------------------------------------------------
	for (int i=0; i<6; i++) {
	// create the i-th start sensor
	sensor=new Sensor(new String("SC"+(i+1)), new Point(winW-senDim,215+50*i),
		      new Dimension(senDim,senDim),new Color(30,137,208));	
	mechparts.add(sensor);
	sensors.add(sensor);
	robot.highSensors.add(sensor);
	// create the i-th end sensor
	sensor=new Sensor(new String("EC"+(i+1)), new Point(100,215+50*i),
			  new Dimension(senDim,senDim),new Color(92,79,172));	
	mechparts.add(sensor);
	sensors.add(sensor);
	robot.lowSensors.add(sensor);
	} // close loop
	
	// Z Motion Sensors ---------------------------------------------
	sensor = new Sensor(new String("ZT"), new Point(20,0),
				  new Dimension(2*senDim,senDim),new Color(75,148,89));
	mechparts.add(sensor); 
	sensors.add(sensor);
	base.lowSensors.add(sensor);
	for (int i=1; i<7; i++) {
	sensor = new Sensor(new String("Z"+(i)), new Point(20,50+50*i),
					  new Dimension(2*senDim,senDim),new Color(75,148,89));
	mechparts.add(sensor); 
	sensors.add(sensor);
	} // close loop
	sensor = new Sensor(new String("ZB"), new Point(20,400),
			  new Dimension(2*senDim,senDim),new Color(75,148,89));
	mechparts.add(sensor); 
	sensors.add(sensor); 
	base.highSensors.add(sensor);
	
	} // close createParts method
	
	
	
	// GETTERS & SETTERS -----------------------------------------------------------------------		
	public static ArrayList<MechPart> getMechParts () {
		return mechparts;
	} // close getMechparts method
	
	public static void setMechParts(ArrayList<MechPart> parts) {
		mechparts = parts;
	} // close setMechParts method
		
	public static ArrayList<MovingPart> getMovingParts () {
		return actionparts;
	} // close getMovingparts method

	public static void setMovingParts(ArrayList<MovingPart> parts) {
		Machine.actionparts = parts;
	} // close setMovingParts method
	
	public static ArrayList<Sensor> getSensors () {
		return sensors;
	} // close getSensors method
	
	public static void setSensors(ArrayList<Sensor> parts) {
		Machine.sensors = parts;
	} // close setSensors method


	public static InteractionPanel getDrawPanel() {
		return drawPanel;
	}


	public static void setDrawPanel(InteractionPanel drawPanel) {
		Machine.drawPanel = drawPanel;
	}


	public static ControlPanel getControlPanel() {
		return controlPanel;
	}


	public static void setControlPanel(ControlPanel controlPanel) {
		Machine.controlPanel = controlPanel;
	}


	public static SerialController getSerialController() {
		return serialController;
	}


	public static void setSerialController(SerialController serialController) {
		Machine.serialController = serialController;
	}


	public static SerialOutputController getSOC() {
		return soc;
	}


	public static void setSOC(SerialOutputController soc) {
		Machine.soc = soc;
	}
	
} // close Machine class
