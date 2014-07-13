package serialPortUtilities;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JOptionPane;

// Notice that the class is only accessible from classes inside the package.
class SerialController implements SerialPortEventListener {
	public static SerialController serialController = new SerialController();
	
	private SerialPort serialPort; // here the serial port object will be assigned
	// Required to store the available serial ports so that the user can select from
	private ArrayList<String> serialPorts = new ArrayList<String>();

   /*
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	// The output stream to the port 
	private OutputStream output;
	// Milliseconds to block while waiting for port open 
	private static final int TIME_OUT = 2000;
	// Default bits per second for COM port. 
	private static final int DATA_RATE = 9600;

	private SerialController() {
		this.initialize();
	} // close SerialController constructor method
	
	
	public static SerialController getInstance() {
		return serialController;
	}

	
	// This should be called when you stop using the port.
	// This will prevent port locking on platforms like Linux.
	public synchronized void close() {
		if (getSerialPort() != null) {
		getSerialPort().removeEventListener();
		getSerialPort().close();
		} // close if
	} // close "close" method
	
	
	private void initialize() {

		CommPortIdentifier portId = null;
		// gets an enumeration of all the known ports
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// adds the serial ports names to the serialPorts arraylist
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier temp = (CommPortIdentifier) portEnum.nextElement();
			getSerialPorts().add(temp.getName());
		} // close while loop
		
		// The user must select the serial port through a new selection window
    	String selPort = (String) JOptionPane.showInputDialog(null,"Port Number (COM)",
				"Select Arduino Serial Port", JOptionPane.PLAIN_MESSAGE, null, getSerialPorts().toArray() ,null);

    	if (selPort!=null) {
    	// We have to get again the commPorts because we cannot iterate through an enumeration twice
		portEnum = CommPortIdentifier.getPortIdentifiers();
		// First, find an the instance of serial port that the user has selected
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().equals(selPort)) {
				portId = currPortId;
				break;
			} // close if
		} // close while loop
    	} // close if
    	
    	// if there is no selection made the program must close because it cannot work 
		// without the proper port selected
		if (selPort==null || portId==null) {
			close();
			System.exit(0);
		} // close if

   		try {
			// open serial port, and use class name for the appName.
			setSerialPort((SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT));

			// set port parameters
			getSerialPort().setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			this.input = new BufferedReader(new InputStreamReader(getSerialPort().getInputStream()));
			this.output = getSerialPort().getOutputStream();

			
			// add event listeners
			getSerialPort().addEventListener(this);
			getSerialPort().notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		} // close try-catch block
		
	} // close initialize method

	
	// Handle an event on the serial port. 
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		// if there are data available in the serial port
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				
			// notify SerialInputManager: getInput().readLine();
				
			} catch (Exception e) {
				System.err.println(e.toString());
			} // close try-catch block
		} // close "DATA_AVAILABLE" if
				
		// Ignore all the other eventTypes, but you should consider the other ones.
		
	} // close serialEvent method
	
	public synchronized void writeDataToPort(String output) {
		try {
			for (int i=0; i<=output.length();i++) {
				this.output.write(output.charAt(i));
			}
		} catch (Exception ex) {
			System.out.println("Serial port exception thrown");
		}
	}	
	
	
	
	// GETTERS & SETTERS -----------------------------------------------------------------------
	private SerialPort getSerialPort() {
		return serialPort;
	}


	private void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}


	private ArrayList<String> getSerialPorts() {
		return serialPorts;
	}


	private void setSerialPorts(ArrayList<String> serialPorts) {
		this.serialPorts = serialPorts;
	}

	
} // close SerialController class