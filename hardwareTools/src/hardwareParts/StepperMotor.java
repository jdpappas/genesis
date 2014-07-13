package hardwareParts;

public class StepperMotor extends Motor {

	private int startStep;
	private int currentStep;
	private int endStep;
	
	
	public StepperMotor(String tag) {
		super(tag);
	}


	public int getStartStep() {
		return startStep;
	}


	public void setStartStep(int startStep) {
		this.startStep = startStep;
	}


	public int getCurrentStep() {
		return currentStep;
	}


	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}


	public int getEndStep() {
		return endStep;
	}


	public void setEndStep(int endStep) {
		this.endStep = endStep;
	}

}
