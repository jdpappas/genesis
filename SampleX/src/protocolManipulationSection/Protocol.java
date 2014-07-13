package protocolManipulationSection;

import hardwareActions.BasicActionInterface;
import hardwareActions.CompositeAction;

import java.io.Serializable;
import java.util.HashMap;

public class Protocol implements Serializable {

	private static final long serialVersionUID = 2215004155451881153L;
	private String name; 
	private HashMap<String, BasicActionInterface> protocolActions;
	
	public Protocol(String name) {
		this.setName(name);
	} // close Protocol constructor method


	private void createProtocolActions() {
		// Must be called soon in the program and when a protocol is selected only its values 
		// change. The protocolActionsObjects already exist waiting to be executed.
	}
		
	
	private class ProtocolImplementation extends CompositeAction {

		protected boolean started;
		protected boolean finished;
		
		public ProtocolImplementation() {
			
		}

		@Override
		protected void preConfigure() {
			super.preConfigure();
		}

		@Override
		public void execute() {
			preConfigure();
			int numOfActions = protocolActions.values().size();
			
			for (int i=0; i<numOfActions; i++) {
				BasicActionInterface protocolAction = protocolActions.get(new Integer(i));
				protocolAction.execute();
			}
			
			postConfigure();
		}
		
		@Override
		protected void postConfigure() {
			// TODO Auto-generated method stub
			
		}

	}
	
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	
	
} // close Protocol class
