package machineCodes;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.Timer;

public class Selection {
	private MovingPart selPart; // the part selected
	private Color trueColor; // the initial object color
	
	private ActionListener flashTask; // the listener that makes the objects to flash
	private Timer flashtimer; // the timer that schedules the flashing


	public void deselect() {
	// initially the previous selection must be nullified so that the data of the new
	// element are assigned
	if (selPart!=null) {
		flashtimer.stop(); // the flashtimer must stop
		selPart.color=trueColor; // the color of the previously selected part must be set to the initial value
		Machine.getDrawPanel().repaint(); // this repaint is required to return the part in the original color
		selPart.selected=false; // the part selected turns to false
		selPart=null; // the selPart is nullified
	} // close if
	} // close deselect method
	
	
	public void select(MovingPart part) {
		deselect(); // to make a new selection there must be no previous selection 
		part.selected=true; // the "selected" field becomes true
		selPart=part; // the part is assigned into "selPart" field
		trueColor=part.color; // trueColor field takes the initial color for the part selected
	} // close select method
	
	
	// select method checks if the mouse is pressed inside the borders of an object
	// and assigns it to selPart field
	public void selectWithMouse(Point chosenP) {
	deselect(); // to make a new selection there must be no previous selection 
	
	// the code iterates over all the actionparts to check if the mouse clicked in one of them
	for (MovingPart part : Machine.getMovingParts()) {
		if (part.b.contains(chosenP)) {
		part.selected=true; // the "selected" field becomes true
		selPart=part; // the part is assigned into "selPart" field
		trueColor=part.color; // trueColor field takes the initial color for the part selected
		} // close if	
	} // close loop
		
	} // close selectWithMouse method
	
	// flash method makes the selected objects flash
	public void flash() {
		
	if (flashTask==null) { // If there is no actionlistener assigned
	flashTask = new ActionListener() { 
	boolean state; // state variable is used to check whether the object color becomes brighter or not
	
	public void actionPerformed(ActionEvent e) {
		// the state changes between false and true so that in every step of the timer
		// the color becomes brighter and in the next step it restores the initial color
	if (state==false) {
		selPart.color=selPart.color.brighter(); // make the color brighter
		Machine.getDrawPanel().repaint();
		state=true; 
	} else {
		selPart.color=trueColor;
		Machine.getDrawPanel().repaint();
		state=false;
	} // close if
	} // close ActionPerformed
	}; // close flashTask
	} // close if
	
	// start the timer
	flashtimer = new Timer(200,flashTask);
	flashtimer.start();
	} // close flash method


	
	
	
	// SETTERS & GETTERS -----------------------------------------------------------------------
	public MovingPart getSelPart() {
		return selPart;
	}


	public void setSelPart(MovingPart selPart) {
		this.selPart = selPart;
	}


	public Color getTrueColor() {
		return trueColor;
	}


	public void setTrueColor(Color trueColor) {
		this.trueColor = trueColor;
	}


	public ActionListener getFlashTask() {
		return flashTask;
	}


	public void setFlashTask(ActionListener flashTask) {
		this.flashTask = flashTask;
	}


	public Timer getFlashtimer() {
		return flashtimer;
	}


	public void setFlashtimer(Timer flashtimer) {
		this.flashtimer = flashtimer;
	}
	

	
} // close Selection class