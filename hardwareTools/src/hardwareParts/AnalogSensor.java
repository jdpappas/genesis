package hardwareParts;

public class AnalogSensor extends Sensor {

	private double value;
	
	public AnalogSensor(String tag) {
		super(tag);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
