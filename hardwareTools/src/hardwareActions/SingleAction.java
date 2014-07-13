package hardwareActions;


/**
 * The class defines an interface for simple actions that a machine may make.
 * @author John
 *
 */
public abstract class SingleAction implements BasicActionInterface {

	protected boolean started;
	protected boolean finished;
	
	public SingleAction() {
		// TODO Auto-generated constructor stub
	}

	protected void preConfigure() {
		this.started = false;
		this.started = false;
		
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
