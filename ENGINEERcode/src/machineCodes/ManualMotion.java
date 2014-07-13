package machineCodes;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class ManualMotion extends KeyAdapter {
/* ManualMotion actually defines the interaction of the user and the interface through the
 * keyboard. When a key is pressed it determines in which direction the object must move
 * and then calls the moveOnKeyPress method that actually offsets the part 1 step at a time
 */
	private Selection selection; // ManualMotion is applied on the selection made
	private MovingPart oldPart = null;
	
	public ManualMotion(Selection selection) {
		this.selection=selection;
	} // close ManualMotion constructor method

	@Override
	public void keyPressed(KeyEvent e) {

		// The following 2 inner "ifs" check if the puller/plunger/magnet are selected
		// only if they are not selected the base can instantly move without being selected 
		// with the mouse
		if (e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) {
			if (selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hpuller")
			&& selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hplunger")
			&& selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hmagnet")) {
				selection.select(Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "Head"));
				selection.flash(); // make the part flash
			} // close if
			selection.getSelPart().negativeDirection();
			moveOnKeyPress(selection.getSelPart());
		} // close if
		
		else if (e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) {
			if (selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hpuller")
			&& selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hplunger")
			&& selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hmagnet")) {
				selection.select(Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "Head"));
				selection.flash(); // make the part flash
			} // close if
			selection.getSelPart().positiveDirection();
			moveOnKeyPress(selection.getSelPart());
		} // close if
		
		else if (e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT) {
			if (selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hrotator")) {
			MovingPart robot=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "robot");
			selection.select(robot);
			selection.flash(); // make the part flash
			} // close if
			selection.getSelPart().negativeDirection();
			moveOnKeyPress(selection.getSelPart());
		} // close if
		
		else if (e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if (selection.getSelPart()!=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "hrotator")) {
			MovingPart robot=Investigator.find((ArrayList <MovingPart>) Machine.getMovingParts(), "tag", "robot");
			selection.select(robot);
			selection.flash(); // make the part flash
			} // close if
			selection.getSelPart().positiveDirection();
			moveOnKeyPress(selection.getSelPart());
		} // close if
		
		
		// The following "if" block turns motorProtReadToSend true, so that it sends a message 
		// to the serial port when a new  moving part is selected.
		if (oldPart==null) {
			Machine.getSOC().setMotorProtReadyToSend(true);;
			oldPart=selection.getSelPart();
		} else if (oldPart!=null) {
			if (selection.getSelPart()!=oldPart) {
			Machine.getSOC().setMotorProtReadyToSend(true);;
			oldPart=selection.getSelPart();
			} // close if
		} // close if
	} // close keyPressed method
	
	
	public void moveOnKeyPress(MovingPart selPart) {
		// the object must move inside the allowed range even when the user handles the machine
		// It is important to note here the reason why we have Machine.soc.direction out of the 
		// "if" block that checks if the object's graphical representation can move. The reason
		// is that in manual mode we want to be able to move the hardware motor even if its 
		// graphical representation has reached the graphical motion limit.
		if (selPart.speed>0) {
			Machine.getSOC().setDirection(1);; // CAUTION !!! NO BORDER HARDWARE CONTROL !!!
			if (selPart.isPositiveMoveable()) {
			selPart.offsetPart();
			selPart.moving=false;
			} // close if
		} else {
			Machine.getSOC().setDirection(-1);; // CAUTION !!! NO BORDER HARDWARE CONTROL !!!
			if (selPart.isNegativeMoveable()) {
			selPart.offsetPart();
			selPart.moving=false;
			} // close if
		} // close if
		Machine.getDrawPanel().repaint();
	} // close moveOnKeyPress method
	
	
} // close ManualMotion class