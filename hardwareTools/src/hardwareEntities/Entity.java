package hardwareEntities;

/**
 * The purpose of Entity class is to hold information about a part that can do an action.
 * For example a motor with the sensors that constrain its motion is a special case of Entity called
 * {@link MovingEntity}.
 * Another example can be a pump along with the pressure sensors that define the pressure limits
 * for a specific operation.
 * @author John
 *
 */
public abstract class Entity {

	private String tag;
	
	public Entity (String tag) {
		this.tag = tag;
	}

	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
