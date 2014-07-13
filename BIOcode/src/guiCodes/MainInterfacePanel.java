package guiCodes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainInterfacePanel extends JPanel {

	JList<String> protocolList;
	ArrayList<Protocol> protocols;
	static DefaultListModel<String> listModel; 
	JTextArea selectionDisplay;
	ProtocolImplementation protocolImplementation = new ProtocolImplementation();
	
	JButton proceedButton; 
	
	public MainInterfacePanel() {
		// We get the list of protocols from the root class
		protocols = RootOfAll.protocols;

		// We define the layout of this panel
		setLayout(new BorderLayout());
		
		listModel = new DefaultListModel<String>();
		
		for (Protocol p:protocols) {
        listModel.addElement(p.name);
		} // close for loop
		
		// We create a new JList
		protocolList = new JList<String>(listModel);
		protocolList.setSize(200, 200);
		// We assign the list into a scrollpanel, so that the user can scroll through it
		// and we locate the scrollpanel in the west side of the mainInterfacePanel
		JScrollPane protocolListPanel = new JScrollPane(protocolList);
		protocolListPanel.setPreferredSize(new Dimension(250, 50));	
		add(protocolListPanel, BorderLayout.WEST);
		
		// The selection model is required to get the selections made and how
		// these selection are done.
		ListSelectionModel listSelectionModel = protocolList.getSelectionModel();
		listSelectionModel.addListSelectionListener(new SelectionListener());
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// We create the area where the data of the protocol will be described
		// and we define it as non-editable
		selectionDisplay = new JTextArea(5, 45);
		selectionDisplay.setEditable(false);
		selectionDisplay.setLineWrap(true);
		selectionDisplay.setWrapStyleWord(true);

		// We create a scrollpanel and assign there the textArea
		JScrollPane diplayPane = new JScrollPane(selectionDisplay,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// We locate the textArea panel at the east side of the mainInterfacePanel
		add(diplayPane, BorderLayout.EAST);
		
		// We create the button that will run the protocol and assign at the south of
		// the panel
		JPanel southPanel = new JPanel (new FlowLayout(FlowLayout.RIGHT));
		add(southPanel, BorderLayout.SOUTH);
		proceedButton = new JButton("Proceed");
		southPanel.add(proceedButton);
		proceedButton.addActionListener(new ProceedListener());
		proceedButton.setEnabled(false);
	} // close MainInterfacePanel constructor method

	class SelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Protocol selectedProt = protocols.get(((ListSelectionModel) e.getSource()).getMinSelectionIndex());
			selectionDisplay.setText(describeProtocol(selectedProt));
			protocolImplementation.setSelectedProtocol(selectedProt);
			proceedButton.setEnabled(true);
		} // close valueChanged method
		
	} // close SelectionListener class
	
	
	public String describeProtocol(Protocol protocol) {
		String description = " Protocol Name: " + protocol.name + "\n " + "\n "
			+ "Bead Capturing Stage:" + "\n "
			+ "The beads are agitated with a velocity of " 
			+ protocol.beadStage.getVelocity() + " ÏL/s"
			+ " for " + protocol.beadStage.getTime() + " minutes." + "\n "
			+ "After the bead capturing, " + protocol.beadStage.getWash() 
			+ " ÏL of buffer flow through each pipette." + "\n " + "\n "
			
			+ "Samples Stage:" + "\n "
			+ "The samples are agitated with the beads with a velocity of "
			+ protocol.samplesStage.getVelocity() + " ÏL/s"
			+ " for " + protocol.samplesStage.getTime() + " minutes." + "\n "
			+ "After the agitation completes, " + protocol.samplesStage.getWash() 
			+ " ÏL of buffer flow through each pipette." + "\n " + "\n "
			
			+ "Detection Antibodies Stage:" + "\n " 
			+ "The detection antibodies with the beads and the samples are agitated with a velocity of "
			+ protocol.detectionStage.getVelocity() + " ÏL/s"
			+ " for " + protocol.detectionStage.getTime() + " minutes." + "\n "
			+ "After the agitation completes, " + protocol.detectionStage.getWash() 
			+ " ÏL of buffer flow through each pipette." + "\n " + "\n "
			
			+ "Detection Antibodies Stage:" + "\n "
			+ "The EP with the beads, the samples and the detection antibodies are agitated with a velocity of "
			+ protocol.epStage.getVelocity() + " ÏL/s"
			+ " for " + protocol.epStage.getTime() + " minutes." + "\n "
			+ "After the agitation completes, " + protocol.epStage.getWash() 
			+ " ÏL of buffer flow through each pipette." + "\n " + "\n "
			
			+ "Last Stage: " + "\n "
			+ "After the completion of the previous steps "
			+ protocol.finalBufferFlow 
			+ " ÏL of buffer flow through each pipette. " + "\n "
			+ "The buffer and the ELISA products are inserted into an empty plate." 
			+ "\n " + "\n " + "Cooling:" + "\n ";
		
		if (protocol.cooling5) {
			description = description + "The plate in the 5th carrier is cooled." + "\n ";
		} // close if
		if (protocol.cooling6) {
			description = description + "The plate in the 6th carrier is cooled." + "\n ";
		} // close if
		if (!protocol.cooling5 && !protocol.cooling6) {
			description = description + "Non of the plates are cooled.";
		} // close if
		return description;
	} // close describeProtocol method
	
	
	class ProceedListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			protocolImplementation.run();
		} // close actionPerformed method
	} // close ProceedListener class
	
	
} // close MainInterfacePanel class
