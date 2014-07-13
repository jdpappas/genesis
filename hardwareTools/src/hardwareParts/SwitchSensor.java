package hardwareParts;

public class SwitchSensor extends Sensor {
	
	private boolean state; // state is true when the sensor is on
	
	public SwitchSensor(String tag) {
		super(tag);
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

}
