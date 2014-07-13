package hardwareParts;

public class Motor extends HardwarePart {

	private double motorSpeed; // the speed of the motor
	
	public Motor(String tag) {
		super(tag);
	}

	public double getMotorSpeed() {
		return motorSpeed;
	}

	public void setMotorSpeed(double motorSpeed) {
		this.motorSpeed = motorSpeed;
	}

}
