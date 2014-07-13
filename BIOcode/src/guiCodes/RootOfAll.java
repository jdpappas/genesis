package guiCodes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import machineCodes.Investigator;
// Project Machine codes is added in project GUIcodes. The procedure is:
// Right-click -> Properties -> Java Build Path -> Projects tab -> Add
import machineCodes.Machine;
import machineCodes.Sensor;
import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;


public class RootOfAll extends JFrame {

	public static ArrayList<Protocol> protocols = new ArrayList<Protocol>();
	static RootOfAll swingInterface;
	static Machine machineInterface;
	
	public static void main(String[] args) {
		// invokeLater takes a Runnable object and queues it to be processed by EventDispatcher Thread.
		// The run method will be called after all queued events are processed.
		// This is standard for the GUI applications.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(
                    		new SyntheticaSimple2DLookAndFeel());
                                // "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                                // UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } // close try-catch block
				// The parts of the machine are created. The user can now access all the
				// parts as they are static fields of the Machine class
				//Machine.createParts();
				// creates a new frame named SampleX
				swingInterface = new RootOfAll("SampleX");
				// creates the machine project
				machineInterface = new Machine("SampleX Machine Interface");
			} // close run method
		}); // close Runnable
		
		// This sleep is required so that the hardware has the time to be configured
		// before the main GUI methods start checking on its state.
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // close try - catch block
		
		// Reads the file where the proper initial state of the sensors is defined
		compareSensorState(getSensorsInitialState());

		// The interface is made visible only after the sensor initial state is 
		// presented to the user.
		swingInterface.setVisible(true);
	} // close main method

	
	public RootOfAll(String name) {
		super(name);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800,600);
		
		// The following lines read the proper folder and get the stored protocols
		// and fill the protocols array
		File folder = new File("C:\\Users\\John\\Dropbox\\Eclipse Files\\GUItempFiles");
		File[] files = folder.listFiles();
	 	for (File fileEntry : files) {
	 		if (fileEntry.isFile()) {
	        	FileInputStream fin;
				try {
					fin = new FileInputStream(fileEntry.getAbsolutePath());
		        	ObjectInputStream ois = new ObjectInputStream(fin);
		        	Protocol protocol = (Protocol) ois.readObject();
		        	protocols.add(protocol);
		        	ois.close();
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // close try - catch block
	        } // close (fileEntry.isFile()) if
	    } // close for loop
	 	
		
		// This is the main pane of the application
		Container rootPane = getContentPane();
		// The layout is defined
		rootPane.setLayout(new BorderLayout());
		
		// We create the panel of tabs and we place it in the center of the root panel
		JTabbedPane tabbedPane = new JTabbedPane();
		rootPane.add(tabbedPane, BorderLayout.CENTER);
		
		// We instantiate the panel of the main tab.
		MainInterfacePanel mainInterfaceTab = new MainInterfacePanel();
		tabbedPane.addTab("Protocol Selection", mainInterfaceTab);
		
		// We instantiate the panel where the new protocols will be created
		CreateNewProtocolPanel createNewProtocolTab = new CreateNewProtocolPanel();
		tabbedPane.addTab("Create New Protocol", createNewProtocolTab);
		
		// We create the menuBar and assign it at the NORTH of the root panel
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		// We create a new menu and assign it to the menuBar
		JMenu protocolMenu = new JMenu("Protocol");
		menuBar.add(protocolMenu);
		// We create new items and assign them to the menu
		JMenuItem protocolItem = new JMenuItem("Protocol");
		protocolMenu.add(protocolItem);
		JMenuItem machineItem = new JMenuItem("Machine Interface");
		protocolMenu.add(machineItem);
		machineItem.addActionListener(new MachineListener());
		
		// centers the window on the screen
		setLocationRelativeTo(null);
	} // close RootOfAll constructor method
	
	
	public static ArrayList<String[]> getSensorsInitialState () {
	// This method reads the file where the proper initial state of the sensors is stored
		
	// The sensor name and state are added to an arrayList of arrays.
	// The array contains a string with the sensor name (cell 0) and a string with
	// the sensor state (cell 1).
	ArrayList<String[]> sensorsInit = new ArrayList<String[]>(); 
	try {
	// The file where the initial state of the sensors is stored
	File file = new File("C:\\Users\\John\\Dropbox\\Eclipse Files\\sensorStates\\initialSensorStates.txt");
	// The reader that will read the .txt file
	BufferedReader br = new BufferedReader(new FileReader(file));
	// The string where each line of the file will be temporarily stored
	String inputLine;
	
	while ((inputLine = br.readLine()) != null) {
	// Each line that is read is splited where there are whitespaces. This is defined
	// with the expression in the brackets. 
	String[] str = inputLine.split("\\s+");
	if (Integer.parseInt(str[1])==1) {
		str[1]="true";
	} else if (Integer.parseInt(str[1])==0) {
		str[1]="false";
	} // close if
	
	sensorsInit.add(str);
	} // close while loop
			
	br.close(); // the buffered reader is closed when the whole file is read.
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // close try - catch block

	return sensorsInit;
	} // close getSensorsInitialState method
	
	
	public static void compareSensorState(ArrayList<String[]> sensorsInit) {

		Sensor sensor;
		String wrongSensorNames = new String();
		
		for (String[] str: sensorsInit) {
			sensor = Investigator.find(Machine.getSensors(), "tag", str[0]);
			if (sensor.getState()==Boolean.parseBoolean(str[1])) {
				continue;
			} else {
				wrongSensorNames = wrongSensorNames + '\n' + sensor.getTag() ;
			} // close if
		} // close for loop
		// A message is displayed to the user to inform him about the state
		// of the sensors.
		if (wrongSensorNames.length()>0) {
		JOptionPane.showMessageDialog(null, "The following sensors are not in" +
				"the proper state " + wrongSensorNames , "Error", JOptionPane.ERROR_MESSAGE);
		} // close if
	} // close compareSensorState method
	
	
	class MachineListener implements ActionListener {
	// This class is used to make the machine interface visible when the user clicks
	// the Machine menuItem of the Protocol menu
		@Override
		public void actionPerformed(ActionEvent e) {
			machineInterface.setVisible(true);			
		} // close actionPerformed method
	} // close MachineListener class
	
} // close RootOfAll class
