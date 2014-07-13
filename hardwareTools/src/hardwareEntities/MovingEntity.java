package hardwareEntities;

import java.util.ArrayList;

import hardwareParts.*;

/**
 * MovingEntity is a special {@link Entity} whose purpose is to hold information for the motion
 * of a motor. The {@link Constraint} fields hold information that define the Constraints of the 
 * motor motion, so it can safely move, without causing any damage.
 * @author John
 *
 */
public class MovingEntity extends Entity {

	private Motor motor;
	private ArrayList<Constraint> upperConstraints;
	private ArrayList<Constraint> lowerConstraints;
	private ArrayList<Constraint> stateConstraints;
	private boolean active;
	
	public MovingEntity(String tag, Motor motor) {
		super(tag);
		this.motor = motor;
	}

	
	/**
	 * Checks if a list of constraints is indeed of the expected type (UPPER, LOWER, STATE).
	 * @param constraintList The list of constraints to be checked.
	 * @param validConstraint The valid ConstraintType enumeration.
	 * @return A boolean value that declares the outcome of the validation procedure.
	 */
	private boolean isConstraintListValid(ArrayList<Constraint> constraintList, ConstraintType validConstraint) {
		for (Constraint constraint: constraintList) {
			if (!isConstraintValid(constraint, validConstraint)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Checks if a list of constraints is indeed of the expected type (UPPER, LOWER, STATE).
	 * @param constraint The constraint to be checked.
	 * @param validConstraint The valid ConstraintType enumeration.
	 * @return A boolean value that declares the outcome of the validation procedure.
	 */
	private boolean isConstraintValid(Constraint constraint, ConstraintType validConstraint) {
		if (constraint.getType().equals(validConstraint)) {
			return false;	
		}
		return true;		
	}
	
	public Motor getMotor() {
		return motor;
	}

	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	
	// Upper constraints management --------------------------------------------------------------------------------------------------------------------
	public ArrayList<Constraint> getUpperConstraints() {
		return upperConstraints;
	}

	public void setUpperConstraints(ArrayList<Constraint> upperConstraints) throws IllegalArgumentException {
		if (isConstraintListValid(upperConstraints, ConstraintType.UPPER)) {
		this.upperConstraints = upperConstraints;
		} else {
			throw new IllegalArgumentException("The ConstraintTypes in the list are not of UPPER type");
		}
	}
	
	public void addUpperConstraint(Constraint Constraint) throws IllegalArgumentException{
		if (isConstraintValid(Constraint, ConstraintType.UPPER)) {
		getUpperConstraints().add(Constraint);
		} else {
			throw new IllegalArgumentException("The ConstraintType is not valid!");
		}
	}
	
	public void deleteUpperConstraint(Constraint Constraint) throws Exception {
		if (!Constraint.isPermanent()) {
			getUpperConstraints().remove(Constraint);
		} else {
			throw new Exception("The current Constraint cannot be deleted!");
		}
	}

	// Lower constraints management --------------------------------------------------------------------------------------------------------------------
	public ArrayList<Constraint> getLowerConstraints() {
		return lowerConstraints;
	}

	public void setLowerConstraints(ArrayList<Constraint> lowerConstraints) throws IllegalArgumentException {
		if (isConstraintListValid(lowerConstraints, ConstraintType.LOWER)) {
			this.lowerConstraints = lowerConstraints;
			} else {
				throw new IllegalArgumentException("The ConstraintTypes in the list are not of LOWER type");
			}
	}

	public void addLowerConstraint(Constraint Constraint) {
		if (isConstraintValid(Constraint, ConstraintType.LOWER)) {
			getLowerConstraints().add(Constraint);
		} else {
			throw new IllegalArgumentException("The ConstraintType is not valid!");
		}
	}

	public void deleteLowerConstraint(Constraint Constraint) throws Exception {
		if (!Constraint.isPermanent()) {
			getLowerConstraints().remove(Constraint);
		} else {
			throw new Exception("The current Constraint cannot be deleted!");
		}
	}


	// State constraints management --------------------------------------------------------------------------------------------------------------------
	public ArrayList<Constraint> getStateConstraints() {
		return stateConstraints;
	}


	public void setStateConstraints(ArrayList<Constraint> stateConstraints) throws IllegalArgumentException {
		if (isConstraintListValid(stateConstraints, ConstraintType.STATE)) {
			this.stateConstraints = stateConstraints;
		} else {
			throw new IllegalArgumentException("The ConstraintTypes in the list are not of STATE type");
		}
	}
	
	
	public void addStateConstraint(Constraint constraint) throws IllegalArgumentException{
		if (isConstraintValid(constraint, ConstraintType.STATE)) {
			getUpperConstraints().add(constraint);
		} else {
			throw new IllegalArgumentException("The ConstraintType is not valid!");
		}
	}

	public void deleteStateConstraint(Constraint constraint) throws Exception {
		if (!constraint.isPermanent()) {
			getStateConstraints().remove(constraint);
		} else {
			throw new Exception("The current Constraint cannot be deleted!");
		}
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean moving) {
		this.active = moving;
	}
	
}

