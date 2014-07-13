package guiCodes;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CreateNewProtocolPanel extends JPanel {

	// North Panel Components
	JLabel nameLabel;
	JTextField nameField;
	// The 4 configuration panels
	ConfigurationPanel beadPanel; 
	ConfigurationPanel samplePanel;
	ConfigurationPanel detectionPanel;
	ConfigurationPanel epPanel;
	// The final buffer components
	JLabel finalBufferLabel;
	JSlider finalBufferSlider;
	JTextField finalBufferText;
	// The cooling selection components
	JLabel cooling5Label;
	JLabel cooling6Label;
	JCheckBox cooling5Check;
	JCheckBox cooling6Check;
	// The save and reset buttons
	JButton saveButton;
	JButton resetButton;
	
	
	public CreateNewProtocolPanel() {
		// We set the layout of the panel to BorderLayout
		setLayout(new BorderLayout());
		
		// We create the NORTH PANEL and assign the label and text field for the protocol NAME
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nameLabel = new JLabel("Protocol Name:");
		nameField = new JTextField("(Insert here the protocol name)", 62);
		nameField.setFont(new Font(nameField.getFont().getName(),Font.ITALIC, 
						nameField.getFont().getSize()));
		// we add the mouse listener to the text field
		nameField.addMouseListener(new NameTextMouse());
		nameField.addActionListener(new NameTextListener());
		
		
		add(northPanel, BorderLayout.NORTH);
		northPanel.add(nameLabel);
		northPanel.add(nameField);
		
		
		// We create the CENTER PANEL and assign the configuration panels
		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		
		beadPanel = new ConfigurationPanel(1, "Bead"); 
		samplePanel = new ConfigurationPanel(2, "Samples");
		detectionPanel = new ConfigurationPanel(3, "Detection antibodies");
		epPanel = new ConfigurationPanel(4, "EP");

		// Create the rest of the panels required for the createNewProtocolPanel
		String title = "Final Stage:";
		JPanel finalStagePanel = new JPanel(new FlowLayout());
		finalStagePanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), 
				title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION),
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
		
		// We create the components for the final buffer selections
		finalBufferLabel = new JLabel("Buffer Flow (ÏL/pipette)");
		finalBufferSlider = new JSlider();
		finalBufferText = new JTextField("" + finalBufferSlider.getValue());
		
		// we define the slider properties
		finalBufferSlider.setMajorTickSpacing(20);
		finalBufferSlider.setMinorTickSpacing(5);
		finalBufferSlider.setPaintTicks(true);
		finalBufferSlider.setSnapToTicks(true);
		
		// we assign the listeners for the text and the slider
		finalBufferSlider.addChangeListener(new SliderListener(finalBufferSlider, finalBufferText));
		finalBufferText.addActionListener(new TextListener(finalBufferSlider, finalBufferText));
		// text field properties
		// we define the PreferredSize because the FlowLayout draws components
		// with their PreferredSize
		finalBufferText.setPreferredSize(new Dimension(30,20));
		
		// We assign to the finalStagePanel the components for the buffer selection
		finalStagePanel.add(finalBufferLabel);
		finalStagePanel.add(finalBufferSlider);
		finalStagePanel.add(finalBufferText);
		finalStagePanel.setMaximumSize(new Dimension(finalStagePanel.getPreferredSize().width, 
									finalBufferSlider.getPreferredSize().height));
		
		// Create the rest of the panels required for the createNewProtocolPanel
		title = "Cooling:";
		JPanel coolingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		coolingPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), 
				title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION),
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
		
		cooling5Check = new JCheckBox("Carrier 5");
		cooling6Check = new JCheckBox("Carrier 6");
		
		coolingPanel.setMaximumSize(new Dimension(coolingPanel.getPreferredSize().width, 
								cooling5Check.getPreferredSize().height));
		coolingPanel.add(cooling5Check);
		coolingPanel.add(cooling6Check);
		
		
		// Create and assign the center panel layout
		GroupLayout centerPanelLayout = new GroupLayout(centerPanel);
		centerPanel.setLayout(centerPanelLayout);
		
		// Define the Horizontal alignment of the centerPanelLayout
		centerPanelLayout.setHorizontalGroup(centerPanelLayout.createSequentialGroup()
			.addGroup(centerPanelLayout.createParallelGroup()
			    .addComponent(beadPanel)
			    .addComponent(samplePanel)
			    .addComponent(detectionPanel))
		    .addGroup(centerPanelLayout.createParallelGroup()
	    		.addComponent(epPanel)
	    		.addGroup(centerPanelLayout.createParallelGroup()
    				.addComponent(finalStagePanel)
    				.addComponent(coolingPanel)))
		);
				
		// Define the Vertical alignment of the centerPanelLayout
		centerPanelLayout.setVerticalGroup(centerPanelLayout.createSequentialGroup()
			.addGroup(centerPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(beadPanel)
			    .addComponent(epPanel))
		    .addGroup(centerPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	    		.addComponent(samplePanel)
	    		.addGroup(centerPanelLayout.createSequentialGroup()
    				.addComponent(finalStagePanel)
    				.addComponent(coolingPanel)))
    		.addGroup(centerPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	    		.addComponent(detectionPanel))
		);
				
		
		// We create the SOUTH PANEL and assign the buttons
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(southPanel, BorderLayout.SOUTH);
		saveButton = new JButton("Save");
		resetButton = new JButton("Reset");
		
		// The button becomes enabled only if the user has inserted a name
		saveButton.setEnabled(false);
		
		saveButton.addActionListener(new SaveListener());
		resetButton.addActionListener(new ResetListener());
		
		southPanel.add(saveButton);
		southPanel.add(resetButton);
		
	} // close CreateNewProtocolPanel constructor method

	
	
	public class ConfigurationPanel extends JPanel {
		
		JLabel velocityLabel;
		JLabel timeLabel;
		JLabel washLabel;
		JSlider velocitySlider;
		JSlider timeSlider;
		JSlider washSlider;
		JTextField velocityText;
		JTextField timeText;
		JTextField washText;
		
	public ConfigurationPanel (int index, String name) {
		String title = "Stage " + index + ": " + name + " configuration";
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), 
				title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION),
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
		
		
		// Create the labels ------------------------------------------------------------
		velocityLabel = new JLabel("Velocity (ÏL/s)");
		timeLabel = new JLabel("Time (min)");
		washLabel = new JLabel("Washing buffer (ÏL/pipette)");

		// Create the sliders -----------------------------------------------------------
		velocitySlider = new JSlider();
		timeSlider = new JSlider();
		washSlider = new JSlider();

		// Slider properties
		velocitySlider.setMajorTickSpacing(20);
		velocitySlider.setMinorTickSpacing(5);
		velocitySlider.setPaintTicks(true);
		velocitySlider.setSnapToTicks(true);
		
		timeSlider.setMajorTickSpacing(20);
		timeSlider.setMinorTickSpacing(5);
		timeSlider.setPaintTicks(true);
		timeSlider.setSnapToTicks(true);
		
		washSlider.setMajorTickSpacing(20);
		washSlider.setMinorTickSpacing(5);
		washSlider.setPaintTicks(true);
		washSlider.setSnapToTicks(true);
		
		// Create the text fields --------------------------------------------------------
		velocityText = new JTextField("" + velocitySlider.getValue());
		timeText = new JTextField("" + timeSlider.getValue());
		washText = new JTextField("" + washSlider.getValue());
		// text field properties
		velocityText.setMinimumSize(new Dimension(30,10));
		timeText.setMinimumSize(new Dimension(30,10));
		washText.setMinimumSize(new Dimension(30,10));
		velocityText.setMaximumSize(new Dimension(30,20));
		timeText.setMaximumSize(new Dimension(30,20));
		washText.setMaximumSize(new Dimension(30,20));
		
		// We add the listeners for the sliders
		velocitySlider.addChangeListener(new SliderListener(velocitySlider, velocityText));
		timeSlider.addChangeListener(new SliderListener(timeSlider, timeText));
		washSlider.addChangeListener(new SliderListener(washSlider, washText));
		
		// We add the listeners for the text
		velocityText.addActionListener(new TextListener(velocitySlider, velocityText));
		timeText.addActionListener(new TextListener(timeSlider, timeText));
		washText.addActionListener(new TextListener(washSlider, washText));
		
		// We create a new groupLayout and assign it as the layout for the CreateNewProtocolPanel
		GroupLayout groupLayout = new GroupLayout(this);
		setLayout(groupLayout);
		// The groupLayout automatically creates the gaps
		groupLayout.setAutoCreateGaps(false);
		groupLayout.setAutoCreateContainerGaps(true);
		
		// Define the Horizontal alignment of the groupLayout
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
			.addGroup(groupLayout.createParallelGroup()
			    .addComponent(velocityLabel)
			    .addComponent(timeLabel)
			    .addComponent(washLabel))
		    .addGroup(groupLayout.createParallelGroup()
	    		.addComponent(velocitySlider)
	    		.addComponent(timeSlider)
	    		.addComponent(washSlider))
    		.addGroup(groupLayout.createParallelGroup()
	    		.addComponent(velocityText)
	    		.addComponent(timeText)
	    		.addComponent(washText))
		);
		
		// Define the Vertical alignment of the groupLayout
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
			.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(velocityLabel)
			    .addComponent(velocitySlider)
			    .addComponent(velocityText))
		    .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	    		.addComponent(timeLabel)
	    		.addComponent(timeSlider)
	    		.addComponent(timeText))
    		.addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
	    		.addComponent(washLabel)
	    		.addComponent(washSlider)
	    		.addComponent(washText))
		);
	} // close ConfigurationPanel constructor method
		
	} // close ConfigurationPanel class
	
	
	// Contains the action when the value of the slider changes	
	class SliderListener implements ChangeListener {
		JSlider slider;
		JTextField text;
		
		public SliderListener (JSlider slider, JTextField text) {
			this.slider = slider;
			this.text = text;
		} // close SliderListener constructor method

		@Override
		public void stateChanged(ChangeEvent ev) {
		// we inform the text that the slider value has changed
		text.setText("" + slider.getValue());
		} // close stateChanged method
		
	} // close SliderListener class
	
	
	// Contains the action when the text changes
	class TextListener implements ActionListener {
		JSlider slider;
		JTextField text;
		
		public TextListener (JSlider slider, JTextField text) {
			this.slider = slider;
			this.text = text;
		} // close TextListener constructor method
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// We store the old value in case the user gives an invalid input
			int oldSliderValue = slider.getValue();
			try {
				// Changes the value of the text to the one given by the user	
				slider.setValue(Integer.parseInt(text.getText()));
			} catch (NumberFormatException ex) {
				// In case the input value is invalid it prints a message
				JOptionPane.showMessageDialog(null, "The input is invalid.\n " +
									"Give a value between 0-100", "Error", JOptionPane.ERROR_MESSAGE);
				slider.setValue(oldSliderValue);
				text.setText("" + oldSliderValue);
			} // close try - catch block
		} // close actionPerformed method
		
	} // close TextListener class
	
	// NameTextMouse contains the action when the user presses the text field
	// for the new protocol name
	class NameTextMouse extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			JTextField text = (JTextField) e.getComponent();
			if (text.getText().equals("(Insert here the protocol name)")) {
				text.setText("");
			} // close if
		} // close mouseClicked method
		
	} // close NameTextMouse
	
	
	// Contains the actions performed when the user inputs a new name for the protocol
	class NameTextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// If there is a new name (the input text is not empty) 
			// inserted the save button is enabled
			if (!nameField.getText().equals("")) {
				saveButton.setEnabled(true);
			} else { // else it is disabled
				saveButton.setEnabled(false);
			} // close if
		} // close actionPerformed method
		
	} // close TextListener class
	
	
	// SaveListener contains the actions to be executed when the SAVE button is pressed
	class SaveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// We create a new protocol with the name defined by the user
			Protocol newProtocol = new Protocol(nameField.getText());
			setProtocol(newProtocol);
			try {
				// We create a file stream where the new protocol object will be stored
				FileOutputStream fout = new FileOutputStream(
					"C:\\Users\\John\\Dropbox\\Eclipse Files\\GUItempFiles" + newProtocol.name);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(newProtocol);
				oos.close(); // close the output stream
				
				// We add the new protocol to the root object protocols list
				RootOfAll.protocols.add(newProtocol);
				// ...and at the list model of the main interface panel
				MainInterfacePanel.listModel.addElement(newProtocol.name);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} // close actionPerformed method
		
		
		// setProtocol method defines the values of the fields of the created protocol
		private void setProtocol(Protocol newProtocol) {
			newProtocol.beadStage = newProtocol.new Stage(beadPanel.velocitySlider.getValue(),
				beadPanel.timeSlider.getValue(), beadPanel.washSlider.getValue());
			newProtocol.samplesStage = newProtocol.new Stage(samplePanel.velocitySlider.getValue(),
					samplePanel.timeSlider.getValue(), samplePanel.washSlider.getValue());
			newProtocol.detectionStage = newProtocol.new Stage(detectionPanel.velocitySlider.getValue(),
					detectionPanel.timeSlider.getValue(), detectionPanel.washSlider.getValue());
			newProtocol.epStage = newProtocol.new Stage(epPanel.velocitySlider.getValue(),
					epPanel.timeSlider.getValue(), epPanel.washSlider.getValue());
			
			newProtocol.finalBufferFlow = finalBufferSlider.getValue();
			newProtocol.cooling5 = cooling5Check.isSelected();
			newProtocol.cooling6 = cooling6Check.isSelected();	
			
		} // close setProtocol method
	} // close SaveListener class
	
	
	// ResetListener contains the actions to be executed when the RESET button is pressed
	class ResetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {	
		// the value of the protocol name is initialized
		nameField.setText("(Insert here the protocol name)");
		
		// Reset the bead configuration values
		beadPanel.velocitySlider.setValue(50);
		beadPanel.velocityText.setText("" + 50);
		beadPanel.timeSlider.setValue(50);
		beadPanel.timeText.setText("" + 50);
		beadPanel.washSlider.setValue(50);
		beadPanel.washText.setText("" + 50);
		// Reset the samples configuration values
		samplePanel.velocitySlider.setValue(50);
		samplePanel.velocityText.setText("" + 50);
		samplePanel.timeSlider.setValue(50);
		samplePanel.timeText.setText("" + 50);
		samplePanel.washSlider.setValue(50);
		samplePanel.washText.setText("" + 50);
		// Reset the detection configuration values
		detectionPanel.velocitySlider.setValue(50);
		detectionPanel.velocityText.setText("" + 50);
		detectionPanel.timeSlider.setValue(50);
		detectionPanel.timeText.setText("" + 50);
		detectionPanel.washSlider.setValue(50);
		detectionPanel.washText.setText("" + 50);
		// Reset the EP configuration values
		epPanel.velocitySlider.setValue(50);
		epPanel.velocityText.setText("" + 50);
		epPanel.timeSlider.setValue(50);
		epPanel.timeText.setText("" + 50);
		epPanel.washSlider.setValue(50);
		epPanel.washText.setText("" + 50);
		
		// Reset the final buffer slider
		finalBufferSlider.setValue(50);
		finalBufferText.setText("" + 50);
		// Reset the cooling check boxes
		cooling5Check.setSelected(false);
		cooling6Check.setSelected(false);
		
		// Disable the save button 
		saveButton.setEnabled(false);
		} // close actionPerformed method
		
	} // close ResetListener class
} // close CreateNewProtocolPanel class
