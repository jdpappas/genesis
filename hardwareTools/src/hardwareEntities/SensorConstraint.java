package hardwareEntities;

import hardwareParts.Sensor;

/**
 * A Constraint defined by a Sensor.
 * @author John
 *
 */
public class SensorConstraint extends Constraint {

	private Sensor sensor;
	
	public SensorConstraint(Sensor sensor, ConstraintType type, boolean permanent) {
		super(type, permanent);
		this.sensor = sensor;
	}

	
	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	
}
