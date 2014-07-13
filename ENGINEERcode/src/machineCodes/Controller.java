package machineCodes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller extends MouseAdapter {
/* The main purpose of the controller is to DEFINE THE CONDITIONS UNDER WHICH A NEW SELECTION
 * CAN BE MADE.
 * This is done through the AUTO and MANUAL modes.
 * AUTO and MANUAL modes both define the conditions under which a new selection can be made.
 * AUTO mode has the extra feature of defining the automatic motion of the parts.
 * BUT NO MOTION DEFINITION is done here. The Controller is sth like the selection and motion
 * manager. It tells what should be done, but DOES NOT KNOW HOW!
 */
	
private Selection selection; 			// selection is the instance that does the selection of the objects 
private String mode; 					// stores the AUTO/MANUAL mode
private AutoMotion autoMotion;  	// instance of the AutoMotion class
	
	public Controller (ArrayList<MovingPart> actionparts) {
		selection = new Selection(); 			// creates a new selection instance
	} // close Controller constructor method
	
	
	public void mousePressed(MouseEvent e) {
	if (mode=="Auto") {// We have to reassign the totalRange to the headbase to eliminate
		autoMode(e);
	} else if (mode=="Manual") {
		manualMode(e);
	} // close if
	} // close mousePressed method
	
	
	// in this autoMode we choose an object through MOUSE selection and then make it move 
	// through the AutoMotion action method
	private void autoMode (MouseEvent e) {
		// we need an oldPart to assign the previous selection made by the user.
		MovingPart oldPart=selection.getSelPart();
		
		if (oldPart==null){ // if there is no part selected before
			selection.selectWithMouse(e.getPoint()); // select a new part
			if (selection.getSelPart() != null) { // if a new part is indeed selected (we may have clicked in empty space)
				selection.flash(); // make the part flash
				selection.getSelPart().moving=true; // set the part moving field to true
				autoMotion = new AutoMotion(selection); // make the part move through the initialization of motion class
				autoMotion.action();
			} // close if
		} else { // if there is a part selected previously
			if (!oldPart.moving) { // if the previous part is not moving then
				selection.selectWithMouse(e.getPoint()); // we are allowed to make a new selection
					if (selection.getSelPart() != null) { // if a new part is indeed selected (we may have clicked in empty space)
					selection.flash(); // make the part flash
					selection.getSelPart().moving=true; // set the part moving field to true
					autoMotion = new AutoMotion(selection); // make the part move through the initialization of motion class
					autoMotion.action();
					} //close if
			} else { // if the part is moving
				selection.setSelPart(oldPart);
			} // close if (!oldPart.moving)
		} // close if (oldPart==null)
	} // close autoMode method
	
	
	// the difference between this overloaded autoMode method and the previous is the
	// fact that the part is not selected with the mouse but defined by Selection.select() method
	// So here we explicitly define the motion conditions.
	// In VARARGS array we can either have the head position or the exact low and high step
	// of a moving part.
	// THE PURPOSE OF THIS METHOD is just to do a selection and then call AutoMotion to
	// exactly define the motion. There is NOTHING defined here concerning the motion steps.
	public void autoMode (MovingPart newSelectedPart, int... varargs) {
		// we need an oldPart to assign the previous selection made by the user.
		MovingPart oldPart=selection.getSelPart();
		
		if (oldPart==null){ // if there is no part selected before
			selection.select(newSelectedPart); // select a new part
			if (selection.getSelPart() != null) { // if a new part is indeed selected (we may have clicked in empty space)
			selection.flash(); // make the part flash
			selection.getSelPart().moving=true; // set the part moving field to true
			autoMotion = new AutoMotion(selection); // make the part move through the initialization of motion class
			// We just pass varargs to action. Their processing is done there.
			// NO motion processing is done in Controller
			autoMotion.action(varargs);
			} // close if
		} else { // if there is a part selected previously
			if (!oldPart.moving) { // if the previous part is not moving then
				selection.select(newSelectedPart); // we are allowed to make a new selection
				if (selection.getSelPart() != null) { // if a new part is indeed selected (we may have clicked in empty space)
				selection.flash(); // make the part flash
				selection.getSelPart().moving=true; // set the part moving field to true
				autoMotion = new AutoMotion(selection); // make the part move through the initialization of motion class
				// We just pass varargs to action. Their processing is done there.
				autoMotion.action(varargs);
				} //close if
			} else { // if the part is moving
				selection.setSelPart(oldPart);
			} // close if (!oldPart.moving)
		} // close if (oldPart==null)
	} // close autoMode method
		
	
	// in manualMode we JUST SELECT a part and then all the action is achieved through
	// the keyboard. NO MOTION definition is done here.
	private void manualMode (MouseEvent e) {
		selection.selectWithMouse(e.getPoint()); // we are allowed to make a new selection
		if (selection.getSelPart() != null) { // if a new part is indeed selected (we may have clicked in empty space)
		selection.flash(); // make the part flash
		} //close if
	} // close manualMode method


	
	// GETTERS & SETTERS -----------------------------------------------------------------------
	public Selection getSelection() {
		return selection;
	}


	public void setSelection(Selection selection) {
		this.selection = selection;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public AutoMotion getAutoMotion() {
		return autoMotion;
	}


	public void setAutoMotion(AutoMotion autoMotion) {
		this.autoMotion = autoMotion;
	}
	
	
} // close MouseHandler class
