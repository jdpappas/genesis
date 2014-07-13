package hardwareParts;

public class Pump extends HardwarePart {

	private int flowSpeed;
	
	public Pump(String tag) {
		super(tag);
	}

	public int getFlowSpeed() {
		return flowSpeed;
	}

	public void setFlowSpeed(int flowSpeed) {
		this.flowSpeed = flowSpeed;
	}

}
