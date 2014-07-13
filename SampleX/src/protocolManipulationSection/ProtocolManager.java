package protocolManipulationSection;

import hardwareActions.BasicActionInterface;
import hardwareActions.CompositeAction;

import java.io.Serializable;
import java.util.HashMap;

public class ProtocolManager {
	private String currentProtocolName; 
	private HashMap<String, BasicActionInterface> protocolActions;
	
	
	public ProtocolManager() {
	} // close Protocol constructor method


	public String getName() {
		return currentProtocolName;
	}


	public void setName(String name) {
		this.currentProtocolName = name;
	}


	public HashMap<String, BasicActionInterface> getProtocolActions() {
		return protocolActions;
	}


	public void setProtocolActions(HashMap<String, BasicActionInterface> protocolActions) {
		this.protocolActions = protocolActions;
	}

} // close Protocol class
