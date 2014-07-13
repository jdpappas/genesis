package protocolManipulationSection;

import java.util.ArrayList;

import hardwareActions.BasicActionInterface;
import hardwareActions.CompositeAction;

public class ProtocolImplementation extends CompositeAction {

	
	public ProtocolImplementation() {
		
	}
	
	public void assignSubActions(Protocol selectedProtocol) {
		setSubActions((ArrayList<BasicActionInterface>) selectedProtocol.getProtocolActions().values());
	}
}
