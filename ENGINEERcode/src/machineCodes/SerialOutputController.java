package machineCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class SerialOutputController extends Thread {
// The purpose of this class is to create a new thread 
// that will write messages to the serial port
	private Selection selection;
	private OutputStream outputStream;
	private int direction=0;
	// this boolean variable is required to declare that the protocol is ready to be sent
	// through the serial port.
	private boolean motorProtReadyToSend = false; 
//	long systemTime=0;
	
	public SerialOutputController (SerialController serialController) {
		try {
			outputStream=serialController.getSerialPort().getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // close try - catch block
	} // close SerialOutputController constructor method
	
	
	// The run method is actually a loop that checks for any change at the mode, the motor
	// or the hardware direction through 3 methods:
	// sendModeProtocol, sendMotorProtocol, sendDirection.
	public void run() {
		// currentMode is user to temporally store the last mode selection
		String currentMode="";
		// motorSelections is an array used to temporally store the last :
		// 1. motor name  2. motor low step  3. motor high step
		String[] motorSelections = {"", "0", "0"};
		
	while (true) {
		// This method sends a message of the currently selected mode (AUTO/MANUAL)
		currentMode = sendModeProtocol(currentMode);
		
		// The reason why this try - catch block is added is because sometimes there
		// is a NullPointerException thrown from sendMotorProtocol in the "if"-block
		// that checks if there is a new selection made.
		try {
			// This method sends a message of the currently selected motor 
			// and returns the name of it.
			motorSelections = sendMotorProtocol(motorSelections);
		} catch (NullPointerException ex) {
			continue;
		} // close try - catch block
		
		// This method sends a message to move the motor in the proper direction,
		// if there is such a command given
		sendDirection();
		
		// We add sleep here, so that it is clear when the microcontroller is ready
		// to receive a message. Otherwise the motor motion won't be stable.
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // close try - catch block
	} // close while loop
	} // close run method
	
	
	private String sendModeProtocol(String currentMode) {
	// check if the mode have changed and if so send the proper msg to the serial port
	if (Machine.getDrawPanel().controller.getMode()!=currentMode) {
		currentMode = Machine.getDrawPanel().controller.getMode();
		String modeMsg=""; 	// this string is required to store the message sent
						   	// to the microcontroller depending on the mode selected
		if (currentMode=="Auto") {
			modeMsg="AU";
		} else if (currentMode=="Manual") {
			modeMsg="MA";
		} // close (currentMode) "if"
		// The string must be converted to array in order to send to 
		// the serial port single characters
		char[] modeChar = modeMsg.toCharArray();
		
		// In the try-catch block the msg is sent to the serial port
		try {
			// The symbol that makes the microcontroller recognize that 
			// a mode-change protocol arrives
			outputStream.write('#'); 
			// Sends the selected protocol code
			for (int i=0; i<modeChar.length; i++) {
			outputStream.write(modeChar[i]);
			} // close for loop
			// Sends the symbol that terminates the protocol code sending
			outputStream.write('!');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // close try - catch block
	} // close (!=currentMode) "if"
		
	return currentMode;
	} // close sendModeProtocol method
		
	
	// sendMotorProtocol method checks if there is a new moving part selected and if so
	// it must send the proper information to the serial port through a proper protocol.
	private String[] sendMotorProtocol(String[] motorSelections) {
	// This "if" block checks if there is a new MovingPart selected and if so it sends ----------
	// the message to the controller to change the moving motor.
	if (selection!=null) {         // The first 2 "if" blocks are required to handle
	if (selection.getSelPart()!=null) { // NullPointerExceptions
		// if the motorProtocol is ready to be sent (check AutoMotion method for details)
		if (motorProtReadyToSend) {
			motorProtReadyToSend = false;
			// the serialMsg variable will gather the whole motor protocol definition
			// and will finally send every char of it to the serial port
			char[] serialMsg = new char[23];
			
		    /* 	serialName variable is required to define the sensor name that will be send
		   	through the protocol to the serial port
			The Arrays.copyOf(array, array_size) static method is used to create a new array
			of size (here 3) that consists of an initial array elements and an amount of 
			null character elements so that the length of the initial array and the null
			characters is equal to the one defined by the user (here 3)
		    */
			char[] serialName = Arrays.copyOf(selection.getSelPart().motorName.toCharArray(), 3);
			
			// The '%' is required for the micro-controller to understand that a
			// protocol sequence will arrive.
			serialMsg[0]='%';
			
			/* The purpose of the following "if" block is to assign next to the name of the 
			 * head motor (Z) some extra information. More specifically:
			 * 1. "T" if the head must move to the top position
			 * 2. "B" if the head must move to the bottom position
			 * 3. A number from 1-6 if the head must move to a carrier
			 * 4. "x" if the head moves to no special position (e.g. manually moved) 
			 */
			if (selection.getSelPart().tag.equals("Head")) {
				HeadBase base=(HeadBase) selection.getSelPart();
//				System.out.println(base.getPreviousPosition() + " " + base.getNextPosition());
				if (base.getNextPosition()==0) {
					for (int i=0; i<serialName.length; i++) {
						if (serialName[i]=='\0') {
							serialName[i]='T';
							break;
						} // close if
					} // close for loop
				} else if (base.getNextPosition()==7) {
					for (int i=0; i<serialName.length; i++) {
						if (serialName[i]=='\0') {
							serialName[i]='B';
							break;
						} // close if
					} // close for loop						
				} else if (base.getNextPosition()>=1 && base.getNextPosition()<=6) {
					for (int i=0; i<serialName.length; i++) {
						if (serialName[i]=='\0') {
							serialName[i]=(char) (base.getNextPosition()+'0');
							break;
						} // close if
					} // close for loop
				} else {
					for (int i=0; i<serialName.length; i++) {
						if (serialName[i]=='\0') {
							serialName[i]='x';
							break;
						} // close if
					} // close for loop
				} // close (base.nextPosition) if
			} // close equals("Head") if
				
			
			/* The purpose of the following "if" block is to assign next to the name of the 
			 * robot motor (X) some extra information. More specifically:
			 * 1. the number of the carrier if the robot is at a carrier level
			 * 2. the character "x" if the carrier is at no specific carrier level
			 * This information is required from the hardware to define the proper sensors
			 * to prevent the motor from crashing.
			 */
			if (selection.getSelPart().tag.equals("robot")) {
			Robot robot = (Robot) selection.getSelPart();
			if (robot.getSelectedCarrier()==null) {
				for (int i=0; i<serialName.length; i++) {
					if (serialName[i]=='\0') {
						serialName[i]='x';
						break;
					} // close if
				} // close for loop
			} else {
				// send the number of carrier
				for (int i=0; i<serialName.length; i++) {
					if (serialName[i]=='\0') {
						serialName[i]=robot.getSelectedCarrier().tag.charAt(robot.getSelectedCarrier().tag.length()-1);
						break;
					} // close if
				} // close for loop
			} // close (robot.selectedCarrier==null) if
			} // close equals("Head") if
				
				
			// The motor name with the proper extra information (if any) are assigned to the 
			// serialMsg variable. The null characters are replaced by whitespace.
			for (int i=0; i<3; i++) {
				if (serialName[i]!='\0') {
					serialMsg[i+1]=serialName[i];					
				} else {
					serialMsg[i+1]=' ';
				} // close if
			} // close for loop

			
			/* The following 4 for-loops are required to break the speed, steps_a, steps_b
			 * and steps_c variables to single digits, in order to store them to the proper 
			 * array positions.
			 * It is important to note here the following:
			 * 1. The for-loops are decreasing in order to assign the digits with the proper order.
			 * 2. The "%" symbol is the symbol for the modulo calculation.
			 * 3. serialMsg is a char array so digit must be casted from int to char
			 * 4. every time we add to the digit the '0' character. This is because the '0'
			 * 	  ASCII character has a value of 48, the '1' char a value of 49, etc..
			 * 	
			 */
			// Each digit of the motorSpeed must be sent separately
			for (int i=3; i>=0; i--) {
				int digit = (int) Math.floor((selection.getSelPart().motorSpeed%(Math.pow(10, i+1))) / Math.pow(10, i));
				serialMsg[4+(3-i)] = (char) (digit+'0');
			} // close for loop
			
			// Each digit of the acceleration steps must be sent separately
			for (int i=3; i>=0; i--) {
				int digit = (int) Math.floor((selection.getSelPart().motorSteps_a%(Math.pow(10, i+1))) / Math.pow(10, i));
				serialMsg[8+(3-i)] = (char) (digit+'0');
			} // close for loop
			
			// Each digit of the constant speed steps must be sent separately
			for (int i=5; i>=0; i--) {
				int digit = (int) Math.floor((selection.getSelPart().motorSteps_b%(Math.pow(10, i+1))) / Math.pow(10, i));
				serialMsg[12+(5-i)] = (char) (digit+'0');
			} // close for loop
			
			// Each digit of the deceleration steps must be sent separately
			for (int i=3; i>=0; i--) {
				int digit = (int) Math.floor((selection.getSelPart().motorSteps_c%(Math.pow(10, i+1))) / Math.pow(10, i));
				serialMsg[18+(3-i)] = (char) (digit+'0');
			} // close for loop
				
				
			// The '!' is required for the micro-controller to understand that
			// the sequence of characters is completed.
			serialMsg[22] = '!';
			
				
			try {
				// This for-loop sends the motor protocol to the serial port digit-by-digit.
				for (int i=0; i<serialMsg.length; i++) {
				outputStream.write(serialMsg[i]);
//					System.out.print(serialMsg[i]);
					if (i==serialMsg.length-1) {
						System.out.println("\n");
					} // close if
				} // close for loop
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // close try - catch block
			
			// assign the new motor selections
			motorSelections[0]=selection.getSelPart().motorName;
			motorSelections[1]=""+selection.getSelPart().lowStep;
			motorSelections[2]=""+selection.getSelPart().highStep;
		} // close new motor selection "if"
		
	  // the following 2 "ifs" were required to check if the instances are not null
	} // close (selection.selPart!=null) "if"
	} // close (selection!=null) "if"
	
	return motorSelections;
	} // close sendMotorProtocol method
	
	
	// This method is required to send the proper motor direction
	private void sendDirection() {
		try {
			// This  "if" block tells the microcontroller to move with the proper direction 
			if (direction==1) {
//				long a = correctTime();
//				try {
//					Thread.sleep(a);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				outputStream.write('-');
				
//				System.out.println(System.currentTimeMillis());
			} else if (direction==-1) {
//				long a = correctTime();
//				try {
//					Thread.sleep(a);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				outputStream.write('1');
				
//				System.out.println(System.currentTimeMillis());
			} // close if 
			// direction becomes 0 again in order to send only a single message
			// to the serial port.
			direction=0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // close try - catch block
	} // close sendDirection method


	
	// GETTERS & SETTERS ------------------------------------------------------------------------
	public Selection getSelection() {
		return selection;
	}


	public void setSelection(Selection selection) {
		this.selection = selection;
	}


	public OutputStream getOutputStream() {
		return outputStream;
	}


	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}


	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}


	public boolean isMotorProtReadyToSend() {
		return motorProtReadyToSend;
	}


	public void setMotorProtReadyToSend(boolean motorProtReadyToSend) {
		this.motorProtReadyToSend = motorProtReadyToSend;
	}
	
	
//	public long correctTime() {
//	long temp = System.currentTimeMillis();
//	long diff = temp - systemTime;
//	systemTime = temp;
//	if (diff>0 && diff<50) {
//		temp = 47 - diff;
//	} else {
//		temp = 0;
//	} // close if
//	return temp;
//	} // close correctTime method
	
} // close SerialOutputController class
