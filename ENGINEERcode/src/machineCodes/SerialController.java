package machineCodes;

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


public class SerialController implements SerialPortEventListener {
	private SerialPort serialPort; // here the serial port object will be assigned
	// Required to store the available serial ports so that the user can select from
	private ArrayList<String> serialPorts = new ArrayList<String>();

	// this boolean turns true when the program and the microcontroller have communicated
	private boolean portFound; 
	// turns true when all the initial actions have been completed (sensor reading)
	private boolean systemStarted;
    private Sensor sensor = null; // This sensor field is required to store the sensor who has
    					  // sent the latest message
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

	public SerialController(Machine parent) {

	} // close SerialController constructor method
	
	
	public void initialize() {

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
			input = new BufferedReader(new InputStreamReader(getSerialPort().getInputStream()));
			output = getSerialPort().getOutputStream();

			
//			serialPort.disableReceiveTimeout();
//			serialPort.enableReceiveThreshold(1);
			
			// add event listeners
			getSerialPort().addEventListener(this);
			getSerialPort().notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		} // close try-catch block
		
	} // close initialize method

	
	// This should be called when you stop using the port.
	// This will prevent port locking on platforms like Linux.
	public synchronized void close() {
		if (getSerialPort() != null) {
		getSerialPort().removeEventListener();
		getSerialPort().close();
		} // close if
	} // close "close" method

	
	// Handle an event on the serial port. Read the data and print it.
	public synchronized void serialEvent(SerialPortEvent oEvent) {
	
		if (!isSystemStarted()) { // true when the sensor initial values are retrieved
			if (isPortFound()) {  // true when the computer and the microcontroller communicated
			initializeSystem(oEvent);
			} else {
			// turns portFound into true when the microcontroller
			// has sent a proper message and the message is received
			checkPorts(oEvent); 
			} // close (portFound) if
		} else { // when the initial values of the sensors are retrieved
			standardSerialReading(oEvent);
		} // close (!systemStarted) if
		
		// Ignore all the other eventTypes, but you should consider the other ones.
		
	} // close serialEvent method
	
	// This method receives the initial values of the sensors when the system starts
	public void initializeSystem (SerialPortEvent oEvent) {
		// if there are data available in the serial port
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
			String inputLine=input.readLine();
			while (inputLine!=null) {
				// We need to check if the serial input is the name of the sensor
				if (inputLine.charAt(0)=='$') { // the sensor name starts with a $ symbol
					// we delete the $ symbol:
					inputLine=inputLine.substring(1, inputLine.length()); 
					// Find the sensor with the name received from the serial port
					setSensor(Investigator.find(Machine.getSensors(), "tag", inputLine));
			    } else if (inputLine.equals("@OK")){
			    	// when we receive a message starting with '@' it means that the 
			    	// microcontroller has sent the initial state for all the sensors
					setSystemStarted(true);
//					parent.setVisible(true);
				} else {
					if (getSensor()!=null) { // if we have already found a sensor with a name 
										// sent from the serial port
						if (inputLine.equals("0")) {
						getSensor().setState(false); // the sensor is OFF
						} else if (inputLine.equals("1")) {
						getSensor().setState(true);	// the sensor is ON
						} // close if
					setSensor(null); // initialize the sensor for the next time
					} // close "sensor state" if
				} // close "check inputLine" if
				inputLine=input.readLine();
				} // close while (inputLine!=null)
			} catch (Exception e) {
				System.err.println(e.toString());
			} // close try-catch block
		} // close "DATA_AVAILABLE" if
	} // close initializeSystem method
	
	// This method checks if the computer and the microcontroller can communicate
	public void checkPorts (SerialPortEvent oEvent) {
		// if there are data available in the serial port
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
			String inputLine=input.readLine();
			while (inputLine!=null) {
				// We need to check if the serial input is the initialization signal
				// from the microcontroller
				if (inputLine.equals("@OK")) { 
				OutputStream outputStream=getSerialPort().getOutputStream();
				// output stream cannot send more than one char at a time
				outputStream.write('@');
				outputStream.write('O');
				outputStream.write('K');
				setPortFound(true); // the communication is established
				} // close if
				inputLine=input.readLine();
				} // close while (inputLine!=null)
			} catch (Exception e) {
				System.err.println(e.toString());
			} // close try-catch block
		} // close "DATA_AVAILABLE" if
	} // close checkPorts method
	
	// This method is the method that reads the serial inputs after the system
	// is properly initialized. The serial inputs are from now on changes in the sensor states
	// We can NOT have a serial input without change in the state of a sensor.
	public void standardSerialReading (SerialPortEvent oEvent) {
	/*
	 * THIS IS THE PROPER WAY TO READ A BUFFER. OTHERWISE NOT ALL OF THE BUFFER INPUTS WILL BE READ!!
	 * 
		String inputLine=input.readLine();
		while (inputLine!=null) {
		System.out.println(inputLine);
		inputLine=input.readLine();
		} // close loop
	*/		
	if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		try {
			String inputLine=input.readLine();
			
			while (inputLine!=null) {
			// We need to check if the serial input is the name of the sensor
			if (inputLine.charAt(0)=='$') { // the sensor name starts with a $ symbol
				// we do this to delete the $ symbol
				inputLine=inputLine.substring(1, inputLine.length()); 
				// find the sensor with the name sent from the serial port
				setSensor(Investigator.find(Machine.getSensors(), "tag", inputLine));
			} // close (inputLine.charAt(0)=='$') "if"
			
			// The following "if" gets the currentStep variable of the motor in the 
			// hardware logic
			else if (inputLine.charAt(0)=='@') {
				// we do this to delete the @ symbol
				inputLine=inputLine.substring(1, inputLine.length()); 
			} // close '@' "if"
			
			else {
				if (getSensor()!=null) { // if we have already found a sensor with a name 
					// sent from the serial port
					if (inputLine.equals("0")) {
					getSensor().setState(false); // the sensor is OFF
					} else if (inputLine.equals("1")) {
					getSensor().setState(true);	// the sensor is ON
					} // close if
					setSensor(null); // initialize the sensor for the next time
				} // close (sensor!=null) "if"
			} // close else if
			inputLine=input.readLine();
			} // close while (inputLine!=null)
		} catch (Exception e) {
			System.err.println(e.toString());
		} // close try-catch block
	} // close "DATA_AVAILABLE" if
	} // close standardSerialReading method


	
	
	// GETTERS & SETTERS -----------------------------------------------------------------------
	public SerialPort getSerialPort() {
		return serialPort;
	}


	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}


	public ArrayList<String> getSerialPorts() {
		return serialPorts;
	}


	public void setSerialPorts(ArrayList<String> serialPorts) {
		this.serialPorts = serialPorts;
	}


	public boolean isPortFound() {
		return portFound;
	}


	public void setPortFound(boolean portFound) {
		this.portFound = portFound;
	}


	public boolean isSystemStarted() {
		return systemStarted;
	}


	public void setSystemStarted(boolean systemStarted) {
		this.systemStarted = systemStarted;
	}


	public Sensor getSensor() {
		return sensor;
	}


	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	
} // close SerialController class