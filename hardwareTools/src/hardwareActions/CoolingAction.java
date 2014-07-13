package hardwareActions;


/**
 * Cooling Action is used to control cooling elements that a device may have.
 * @author John
 *
 */
public class CoolingAction extends SingleAction {

	private boolean cooling;
	
	public CoolingAction() {
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isCooling() {
		return cooling;
	}

	public void setCooling(boolean cooling) {
		this.cooling = cooling;
	}


}
