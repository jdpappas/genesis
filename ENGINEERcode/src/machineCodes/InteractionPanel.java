package machineCodes;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class InteractionPanel extends JPanel {
	/* InteractionPanel is the pane where the parts are drawn. So its purpose is to define its 
	 * components which are the controller, keyHandler, mechparts and also useful is the parent 
	 */
	// The dimensions of the panel.
	private static final int winWidth=800;
	private static final int winHeight=600;
	// The mechparts and actionparts used
//	private static ArrayList<MechPart> mechparts = new ArrayList<MechPart>();
//	private static ArrayList<MovingPart> actionparts = new ArrayList<MovingPart>();
	
	Controller controller;
	ManualMotion keyHandler;
	Timer sensorTimer;
	
	public InteractionPanel(Machine parent) {
		setOpaque(true);
		// sets the size of the application window
		// setPreferredSize is preferred over setSize when a LayoutManager is used
		setPreferredSize(new Dimension(winWidth,winHeight));
		// this line is required so the component can be focused
		setFocusable(true);
		// we request focus so that we can directly use the keyboard to control the parts
		// when the program starts
		requestFocus();
		// This timer makes the sensors that are ON to flash.
		// Initially there are no tasks assigned. 
		// The tasks are assigned and removed afterwards.
		sensorTimer=new Timer(200, null);
		sensorTimer.start();
	} // close InteractionPanel constructor method

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
	for (MechPart m : Machine.getMechParts()) {
		m.paint(g);
	} // close loop
	} // close paintComponent method
	
	public void initializeController() {
		// add the mouse listener to the drawPanel
		controller=new Controller(Machine.getMovingParts());
		addMouseListener(controller);
	} // close initializeController method
	
	
	
	// GETTERS & SETTERS -----------------------------------------------------------------------
	public Controller getController() {
		return controller;
	} // close getController method

	public static int getWinwidth() {
		return winWidth;
	}

	public static int getWinheight() {
		return winHeight;
	}
} // close InteractionPanel class
