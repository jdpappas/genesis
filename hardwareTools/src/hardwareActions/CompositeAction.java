package hardwareActions;

import java.util.ArrayList;

/**
 * The CompositeAction is an abstract class that defines the interface of executing complex actions.
 * The purpose of complex actions is to create a group of actions that have a logical - higher level meaning. 
 * @author John
 *
 */
public abstract class CompositeAction implements BasicActionInterface {

	protected boolean started;
	protected boolean finished;
	private ArrayList<BasicActionInterface> subActions;
	
	public CompositeAction() {
		// TODO Auto-generated constructor stub
	}

	protected void preConfigure() throws Exception {
		this.started = false;
		this.finished = false;
		if (subActions==null) {
			throw new Exception("There is no protocol selected");
		}
	}

	@Override
	public void execute() throws Exception {
		preConfigure();
		
		this.started = true;
		for (BasicActionInterface action: subActions) {
			action.execute();
		}
		
		postConfigure();
		
	}

	protected void postConfigure() {
		this.finished = true;
		
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
	
	public ArrayList<BasicActionInterface> getSubActions() {
		return subActions;
	}

	public void setSubActions(ArrayList<BasicActionInterface> subActions) {
		this.subActions = subActions;
	}
}
