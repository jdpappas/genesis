package hardwareEntities;

/** A Constraint can be either a sensor or a defined value e.g. number of steps,
	number of rotations etc. The MovingEntity has to be within the limits that a 
	constraint defines.
 * 
 * @author John
 *
 */
public class Constraint {

	protected ConstraintType type; // it is an upper or lower limit
	// This attribute is true when the constraint is permanent.
	// Terminal sensors are permanent, but intermediate positions defined by the number of
	// steps are permanent only for the duration of the specific motion.
	protected boolean permanent; 
	
	
	public Constraint(ConstraintType type, boolean permanent) {
		this.type = type;
		this.permanent = permanent;
	}
	
	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public ConstraintType getType() {
		return type;
	}

	public void setType(ConstraintType type) {
		this.type = type;
	}


	
}