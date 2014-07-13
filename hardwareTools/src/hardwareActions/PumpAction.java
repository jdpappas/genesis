package hardwareActions;

/**
 * The class is used to control any pumps that a machine contains.
 * @author John
 *
 */
public class PumpAction extends SingleAction {

	private int finalBufferFlow;

	public PumpAction() {
	}

	public int getFinalBufferFlow() {
		return finalBufferFlow;
	}

	public void setFinalBufferFlow(int finalBufferFlow) {
		this.finalBufferFlow = finalBufferFlow;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
