package hardwareEntities;


/**
 * A Constraint defined by a specified value.
 * @author John
 *
 */
public class ValueConstraint extends Constraint {

	private int value;
	
	public ValueConstraint(int value, ConstraintType type, boolean permanent) {
		super(type, permanent);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
