package hardwareActions;

import java.util.ArrayList;

import hardwareEntities.Constraint;
import hardwareEntities.ConstraintType;
import hardwareEntities.MovingEntity;

/**
 * The purpose of ElementaryMotion is to define which {@link MovingEntity} is going to move and
 * to apply any possible extra {@link Constraint}s for the current motion. 
 * @author John
 *
 */
public class ElementaryMotion extends SingleAction {

	private MovingEntity movingEntity;
	private ArrayList<Constraint> extraConstraints;
	
	
	public ElementaryMotion(MovingEntity movingEntity) {
		this.started = false;
		this.finished = false;
		this.movingEntity = movingEntity;
	}


	protected void preConfigure() {
		super.preConfigure();
		// Add any extra motion constraints
		if (!getExtraConstraints().isEmpty()) {
		for (Constraint constraint: this.getExtraConstraints()) {
			if (constraint.getType().equals(ConstraintType.UPPER)) {
				getMovingEntity().addUpperConstraint(constraint);
			} else if (constraint.getType().equals(ConstraintType.LOWER)) {
				getMovingEntity().addLowerConstraint(constraint);
			} else if (constraint.getType().equals(ConstraintType.STATE)) {
				getMovingEntity().addStateConstraint(constraint);
			}
		} // close for
		} // close if
	}


	@Override
	public void execute() {
		preConfigure();
		
		this.started = true;
		//TODO define data to be used with SerialController.writeDataToPort
		
		while (!finished) {
			//TODO waits until it receives message from the port that the motion is completed
		}
		postConfigure();
	}


	private void postConfigure() {
		// Delete the non-permanent motion constraints
		try {
			for (Constraint constraint: getMovingEntity().getLowerConstraints()) {
				if (!constraint.isPermanent()) {
					getMovingEntity().deleteLowerConstraint(constraint);
				}
			}
			for (Constraint constraint: getMovingEntity().getUpperConstraints()) {
				if (!constraint.isPermanent()) {
					getMovingEntity().deleteUpperConstraint(constraint);
				}
			}
			for (Constraint constraint: getMovingEntity().getStateConstraints()) {
				if (!constraint.isPermanent()) {
					getMovingEntity().deleteStateConstraint(constraint);
				}
			}
			
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		
	}


	public ArrayList<Constraint> getExtraConstraints() {
		return extraConstraints;
	}


	public void setExtraConstraints(ArrayList<Constraint> extraConstraints) {
		this.extraConstraints = extraConstraints;
	}

	public void addExtraConstraints(Constraint constraint) {
		this.extraConstraints.add(constraint);
	}

	public MovingEntity getMovingEntity() {
		return movingEntity;
	}


	public void setMovingEntity(MovingEntity movingEntity) {
		this.movingEntity = movingEntity;
	}

	
}
