package guiCodes;

import javax.swing.Timer;

import machineCodes.HeadBase;
import machineCodes.Machine;
import machineCodes.MovingPart;

public abstract class HighLevelMotion {

	final static String LOW = "LOW";
	final static String HIGH = "HIGH";
	boolean isCompleted = false;
	boolean hasStarted = false;
	Timer stateTimer;
	
	public HighLevelMotion() {
		
	} // close HighLevelMotion constructor method

	
	public void run () {
		// Every time the method starts the variable must return to false to declare that
		// the method is NOT completed
		isCompleted = false;
		hasStarted = true;

		/* In the overriding of run method in the subclasses of HighLevelMotion we use
		 * a Timer.
		 * Why do we need a timer?? - Let's explain it with an example
		 * If we call the magnetRotate method the timer for the magnet rotation will start.
		 * So the boolean moving of the hrotator will be true.
		 * AutoMotion class allows another part to move ONLY if all the other parts' moving state
		 * is false. So because the motiontimer has not ended the magnetMove method that follows 
		 * will be called, but the magnet won't be able to move.
		 * This is the reason for creating the stateTimer. What this timer actually does is 
		 * checking in defined intervals of time which actions have been completed and it triggers
		 * the next action when the previous actions are completed.
		 */
	} // close run method
	
	
	public void headMove(HeadBase base, int... varargs) {
		pauseMotion(500);
		// varargs are the low and high steps when we want to define a specific head motion
		if (varargs.length==1) {
		Machine.getDrawPanel().getController().autoMode(base, varargs[0]);
		} else if (varargs.length==2) {
		Machine.getDrawPanel().getController().autoMode(base, varargs[0], varargs[1]);
		} // close if
	} // close headMove method
	
	
	// This method is responsible for the elementary motions of all the other moving parts 
	// except for the head base
	public void partMove(MovingPart selPart, int pauseTime, String... varargs) {
		pauseMotion(pauseTime);
		// varargs are the low and high steps when we want to define a specific magnet motion
		if (varargs.length==1) {
			if (varargs[0].equals(LOW)) {
				if (selPart.getCurrentStep() == selPart.getLowStep()) {
					return;
				} else {
					Machine.getDrawPanel().getController().autoMode(selPart);
				} // close if
			} else if (varargs[0].equals(HIGH)) {
				if (selPart.getCurrentStep() == selPart.getHighStep()) {
					return;
				} else {
					Machine.getDrawPanel().getController().autoMode(selPart);
				} // close if
			} // close (position) "if"
		} else if (varargs.length == 2) {
			Machine.getDrawPanel().getController().autoMode(selPart, Integer.parseInt(varargs[0]), Integer.parseInt(varargs[1]));
		} // close (varargs) "if"
	} // close partMove method
	
			
	// We added this method so that between the different motor motions there is a pause
	public void pauseMotion(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // close try - catch block
	} // close pause Motion method
	
	
} // close HighLevelMotion class
