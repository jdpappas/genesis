package machineCodes;

import java.awt.event.*;

import javax.swing.Timer;

public class AutoMotion {
/* The purpose of AutoMotion is to get a selected part and the varargs 
 * and create the timer that will do the motion automatically
 */
	
	private MovingPart selPart; // The selected moving part
	private Timer graphicMotionTimer; // The timer that controls the automatic motion
	private GraphicMotionTask graphicMotionTask; // The task that the timer performs in every time step
	private int direction=0; // Required for communication with hardware
	
	public AutoMotion (Selection selection){
		this.selPart=selection.getSelPart();
	} // close AutoMotion constructor method
		
	
	// This method contains the main actions layout of each auto motion. 
	// The proper secondary autoMotion methods are called for extra features.
	public void action(int... varargs) {
	// STEP 1: If the selPart is the Head then properly define its motion because it differs 
	// from the other parts' motion.
		if (selPart.tag.equals("Head")) {
			headDefinition(varargs);
		} // close if
	
		
	// STEP 2:  Define the acceleration/deleration/constant speed steps in order to be 
	// correctly sent through the serial port.
		if (varargs.length == 2) {
			selPart.calculateMotorSteps(varargs[0], varargs[1]);
		} else if (varargs.length < 2) {
			selPart.calculateMotorSteps(selPart.lowStep, selPart.highStep);
		} // close if
		
		
	// STEP 3: Call the method that defines the motion and creates the motion timer
	// that draws and moves the part in the GUI.
		actionDefinition();
		
		
	// STEP 4: Now the PROTOCOL IS COMPLETELY DEFINED. The next boolean informs the serial
	// output controller that now the protocol can be sent to the serial port.
		Machine.getSOC().setMotorProtReadyToSend(true);;
	} // close action method
	
	
	// The headDefinition method is used to define the range of the head motion.
	// An extra method is required for the head motion as there are 3 different ways of 
	// defining its motion as presented below:
	private void headDefinition(int... varargs) {
		HeadBase selPart=(HeadBase) this.selPart;
		// 1ST WAY: we have selected the head with the mouse
		if (varargs.length==0) {
			// it is important to state that through selectPositionWithMouse we only define
			// the motion range NOT do the motion. Motion action is defined inside 
			// actionDefinition -> actionPerformed
			selPart.selectPositionWithMouse();
		
		// 2ND WAY: we explicitly defined the exact position (a position where a
		// plate carrier can be moved)
		} else if (varargs.length==1) {
			selPart.setNextPosition(varargs[0]); 	// required in the following method
			selPart.defineAutoMotionRange(); 		// the method that defines the head motion range
			selPart.setPreviousPosition(selPart.getNextPosition()); // required for the previous method in case that
																	// the loop does not stop
		
		// 3RD WAY: we explicitly defined the exact step number which is different
		// from a carrier position so the head position will be 'x'.
		} else if  (varargs.length==2) {
			selPart.setNextPosition('x');
			selPart.setPreviousPosition('x');
		} // close if
	} // close headDefinition method
	
	
	// This method defines the motion and creates the motion timer
	// that draws and moves the part in the GUI.
	private void actionDefinition(int... varargs) {
		// The upper and lower steps are defined inside every object. AutoMotion does NOT
		// have to know how these steps are assigned. 
		// Here we only define the DIRECTION of motion.
		if (selPart.currentStep==selPart.lowStep){
			selPart.positiveDirection();
			direction=1;
		} else if (selPart.currentStep==selPart.highStep) {
			selPart.negativeDirection();
			direction=-1;
		} // close if
		
		// The following "if" block is required to send the direction message to 
		// the serialOutputController. In AUTO mode there is only a single message sent
		// from the serial port, so it must be out of the motionTimer, but there must also
		// be a checking if it can or cannot move to the pos/neg direction
		if (selPart.speed>0) { // when the object moves to POSITIVE direction
			if (selPart.isPositiveMoveable()) {
			Machine.getSOC().setDirection(direction);; // HARDWARE
			} // close if
		} else {			// when the object moves to NEGATIVE direction
			if (selPart.isNegativeMoveable()) {
			Machine.getSOC().setDirection(direction);; // HARDWARE
			} // close if
		} // close if
		
		// Create a motiontimer if there is no other created before
		if (graphicMotionTimer==null) {
			graphicMotionTask = new GraphicMotionTask(); // close taskPerformer
		graphicMotionTimer = new Timer(10, graphicMotionTask);
		} // close if
		graphicMotionTimer.start();
	} // close actionDefinition method
	
	
	// The motionListener actually offsets a part until the currentStep becomes equal
	// to the low/high step depending on the speed direction 
	private class GraphicMotionTask implements ActionListener {
		
		public GraphicMotionTask () {
			
		} // close MotionListener constructor method method
		
		public void actionPerformed(ActionEvent e) {
			// this inner "if" combined with the outer is required to be able to start and 
			// stop the object in the start and end position. Otherwise the object would either
			// pass the limit or wouldn't be able to start again.
			if (selPart.speed>0) { // when the object moves to POSITIVE direction
				if (selPart.isPositiveMoveable()) {
					selPart.offsetPart();
					Machine.getDrawPanel().repaint();
				} else if (selPart.currentStep==selPart.highStep) {
					selPart.moving=false;
					graphicMotionTimer.stop();
				} // close if
			} else {			// when the object moves to NEGATIVE direction
				if (selPart.isNegativeMoveable()) {
					selPart.offsetPart();
					Machine.getDrawPanel().repaint();
				} else if (selPart.currentStep==selPart.lowStep) {
					selPart.moving=false;
					graphicMotionTimer.stop();
				} // close if
			} // close (direction) "if"
		} // close ActionPerformed
	} // close MotionListener class
	
	
	// GETTERS & SETTERS ------------------------------------------------------------------------

	public MovingPart getSelPart() {
		return selPart;
	}


	public void setSelPart(MovingPart selPart) {
		this.selPart = selPart;
	}


	public Timer getGraphicMotionTimer() {
		return graphicMotionTimer;
	}


	public void setGraphicMotionTimer(Timer graphicMotionTimer) {
		this.graphicMotionTimer = graphicMotionTimer;
	}


	public GraphicMotionTask getGraphicMotionTask() {
		return graphicMotionTask;
	}


	public void setGraphicMotionTask(GraphicMotionTask graphicMotionTask) {
		this.graphicMotionTask = graphicMotionTask;
	}


	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	
} // close AutoMotion class