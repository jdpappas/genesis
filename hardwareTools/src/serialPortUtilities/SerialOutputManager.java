package serialPortUtilities;

public class SerialOutputManager {

	public SerialOutputManager() {
		// TODO Auto-generated constructor stub
	}

	public void translateToOutputMessage() {
		String output = "";
		
		SerialController.getInstance().writeDataToPort(output);
	}
	
}
