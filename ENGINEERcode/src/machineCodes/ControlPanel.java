package machineCodes;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class ControlPanel extends JPanel {
/* ControlPanel is the panel where all the possible buttons will be placed.
 * So here the button classes are instantiated and assigned to the panel.
 */
	
	// The toggle button that switches between manual and auto mode.
	private static JToggleButton autoManualButton;
	// The handler of the toggle button
	private static ItemHandler itemHandler;
	
	public ControlPanel(Machine parent) {
	this.setLayout(new FlowLayout()); // defines the layout of the controlPanel
	autoManualButton = new JToggleButton("Auto", false);
	// set the preferred dimension of the button
	autoManualButton.setPreferredSize(new Dimension(60,30));
	// creates an item listener for the toggle button
	itemHandler = new ItemHandler(parent, autoManualButton);
	// assign the listener to the toggle button
	autoManualButton.addItemListener(itemHandler);
	// adds the togglebutton to the buttonpanel
	this.add(autoManualButton);
	} // close ControlPanel constructor method

	
	
	// GETTERS & SETTERS --------------------------------------------------------------------
	public static JToggleButton getAutoManualButton() {
		return autoManualButton;
	}

	public static void setAutoManualButton(JToggleButton autoManualButton) {
		ControlPanel.autoManualButton = autoManualButton;
	}

	public static ItemHandler getItemHandler() {
		return itemHandler;
	}

	public static void setItemHandler(ItemHandler itemHandler) {
		ControlPanel.itemHandler = itemHandler;
	}

} // close ControlPanel class

