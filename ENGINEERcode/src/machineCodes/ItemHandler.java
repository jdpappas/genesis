package machineCodes;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JToggleButton;


public class ItemHandler implements ItemListener {
// ItemHandler is the class that defines the action when the toggleButton changes state	
	
	// The "super" components
	private Controller controller;
	// The "sub" components
	private JToggleButton button; // the button that triggers the itemEvent
	private ManualMotion keySel;
	
	public ItemHandler(Machine parent, JToggleButton button) {
		// initially the conditions are "auto", so we add the mouselistener
		this.button=button;
		this.controller=Machine.getDrawPanel().controller;
		this.controller.setMode("Manual");// add the keyHandler to the drawPanel
		
		// here is the keyHandler created and assigned to the drawPanel as 
		// listener and field
		keySel=new ManualMotion(controller.getSelection());
		Machine.getDrawPanel().addKeyListener(keySel);
		Machine.getDrawPanel().keyHandler=keySel;
	} // close ItemHandler constructor
	
	// if the button is selected we are in "auto" mode else in "manual" mode
	public void itemStateChanged(ItemEvent ie) {
		if (!this.button.isSelected()) { 			// MANUAL MODE
			button.setText("Auto");

			Machine.getDrawPanel().addKeyListener(keySel);
			// We request again the focus to the drawPanel so it gets the keyboard inputs
			Machine.getDrawPanel().requestFocus();
			// define the mode of the Controller
			controller.setMode("Manual");
			// deselect any previous selection
			controller.getSelection().deselect();// previous selection in the auto Mode
			
			// The headBase when the mode changes from auto to manual can move from the top 
			// to the bottom position.
			HeadBase temp=(HeadBase) Investigator.find((ArrayList <MechPart>) Machine.getMechParts(), "tag", "Head");
			temp.highStep=temp.totalSteps;
			temp.lowStep=0;
		} else { 									// AUTO MODE	
			button.setText("Manual");
			// define the mode of the MouseHandler
			controller.setMode("Auto");
			// deselect any previous selection
			controller.getSelection().deselect();
			// remove access of the keyboard
			Machine.getDrawPanel().removeKeyListener(keySel);
		} // close (!this.button.isSelected()) if
	} // close itemStateChanged method
	
} // close ItemListener class