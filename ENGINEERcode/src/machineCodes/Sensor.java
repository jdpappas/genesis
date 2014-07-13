package machineCodes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sensor extends MechPart {
	
	private boolean state; // state is true when the sensor is on
	private ActionListener sensorTask; // this listener changes the color of the sensor when activated
	private Color trueColor; // required to reset the color of the sensor when after the 
					 // flashing ends
	
	public Sensor(String TAG, Point P, Dimension D, Color C) {
		super(TAG, P, D, C); // calls the constructor of the super class
		trueColor=color; // assign the value of the initial color of the sensor
		
		// This task will be added to the sensor timer (see: InteractionPanel) when the 
		// sensor is activated so that it flashes and the user understands it is activated
		sensorTask = new ActionListener() { 
		// state variable is used to check whether the object color becomes brighter or not
		boolean taskState; 
		
		public void actionPerformed(ActionEvent e) {
		// the state changes between false and true so that in every step of the timer
		// the color becomes brighter and in the next step it restores the initial color
		
		if (taskState==false) {
			color=color.brighter(); // make the color brighter
			Machine.getDrawPanel().repaint();
			taskState=true; 
		} else {
			color=trueColor;
			Machine.getDrawPanel().repaint();
			taskState=false;
		} // close if
		
		} // close ActionPerformed method
		}; // close sensorTask inner class
	
	} // close Sensor constructor method
		
	@Override
	public void paint(Graphics g) {
		// calls the draw method of the superclass, else it would only call the commands specified in the subclass
		super.paint(g); 
		g.fill3DRect(p.x,p.y,d.width,d.height,true);
	} // close paint method
	

	public void setState(boolean state) {
	this.state=state;
	if (!state) { 		// when the state is true
		// delete this sensor's task from sensor timer so that it 
		// stops flashing from now on
		Machine.getDrawPanel().sensorTimer.removeActionListener(sensorTask);
		// change the color to its initial value
		color=trueColor;
		// redraw the panel so that the sensor gets it's initial color
		Machine.getDrawPanel().repaint();
	} else {			// when the state is false
		// add the sensorTask to the sensor timer so it starts flashing from now on
		Machine.getDrawPanel().sensorTimer.addActionListener(sensorTask);
	} // close "state" if
	} // close setState method
	
	
	
	// GETTERS & SETTERS -------------------------------------------------------------------
	public boolean getState() {
		return state;
	} // close setState method

	public boolean isState() {
		return state;
	}

	public ActionListener getSensorTask() {
		return sensorTask;
	}

	public void setSensorTask(ActionListener sensorTask) {
		this.sensorTask = sensorTask;
	}

	public Color getTrueColor() {
		return trueColor;
	}

	public void setTrueColor(Color trueColor) {
		this.trueColor = trueColor;
	}
} // close Sensor class
