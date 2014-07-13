package baseSection;


import java.util.HashMap;

import hardwareEntities.*;
import hardwareParts.AnalogSensor;
import hardwareParts.Motor;
import hardwareParts.Sensor;
import hardwareParts.SwitchSensor;

public class SampleX {

	private HashMap<String, MovingEntity> movingEntities = new HashMap<String, MovingEntity>();
	private HashMap<String, Sensor> sensors = new HashMap<String, Sensor>();
	

	public static void main(String[] args) {
		new SampleX();
	}
	
	public SampleX() {
		this.createParts();
		this.assignConstraints();
	}

	
	private void createParts () {
		// All the moving entities of SampleX are created here
		this.movingEntities.put("HeadBase", new MovingEntity("HeadBase", new Motor("zMotor")));
		this.movingEntities.put("HeadPuller", new MovingEntity("HeadPuller", new Motor("pullerMotor")));
		this.movingEntities.put("HeadPlunger", new MovingEntity("HeadPlunger", new Motor("plungerMotor")));
		this.movingEntities.put("HeadMagnet", new MovingEntity("HeadMagnet", new Motor("magnetMotor")));
		this.movingEntities.put("HeadRotator", new MovingEntity("HeadRotator", new Motor("rotatorMotor")));
		this.movingEntities.put("Robot", new MovingEntity("Robot", new Motor("xMotor")));
		
		// All the sensors of SampleX, that are not considered as SensorEntities, are created here
		this.sensors.put("CL", new SwitchSensor("CL"));
		this.sensors.put("ZT", new SwitchSensor("ZT"));
		this.sensors.put("ZB", new SwitchSensor("ZB"));
		this.sensors.put("RS", new SwitchSensor("RS"));
		this.sensors.put("RE", new SwitchSensor("RE"));
		
		this.sensors.put("HPD", new SwitchSensor("HPD"));
		this.sensors.put("HPU", new SwitchSensor("HPU"));
		this.sensors.put("HPM", new SwitchSensor("HPM"));
		this.sensors.put("HMD", new SwitchSensor("HMD"));
		this.sensors.put("HMP", new AnalogSensor("HMP"));
		
		for (int i=1; i<=4; i++) {
			this.sensors.put("SC"+i, new SwitchSensor("SC"+i));
			this.sensors.put("EC"+i, new SwitchSensor("EC"+i));
			this.sensors.put("Z"+i, new SwitchSensor("Z"+i));
			this.sensors.put("R"+i, new SwitchSensor("R"+i));
		}
		
		for (int i=5; i<=6; i++) {
			this.sensors.put("SC"+i, new SwitchSensor("SC"+i));
			this.sensors.put("EC"+i, new SwitchSensor("EC"+i));
			this.sensors.put("Z"+i, new SwitchSensor("Z"+i));
		}
	}
	
	
	private void assignConstraints() {
		// The upper Constraints of SampleX moving entities are assigned
		this.movingEntities.get("HeadBase").addUpperConstraint(
				new SensorConstraint(getSensors().get("ZT"), ConstraintType.UPPER, true));
		this.movingEntities.get("HeadBase").addUpperConstraint(
				new SensorConstraint(getSensors().get("R1"), ConstraintType.UPPER, true));
		this.movingEntities.get("Robot").addUpperConstraint(
				new SensorConstraint(getSensors().get("R4"), ConstraintType.UPPER, true));
		this.movingEntities.get("Robot").addUpperConstraint(
				new SensorConstraint(getSensors().get("RE"), ConstraintType.UPPER, true));
		this.movingEntities.get("HeadPlunger").addUpperConstraint(
				new SensorConstraint(getSensors().get("HPD"), ConstraintType.UPPER, true));
		this.movingEntities.get("HeadPuller").addUpperConstraint(
				new SensorConstraint(getSensors().get("HPM"), ConstraintType.UPPER, true));
		
		// The lower Constraints of SampleX moving entities are assigned
		this.movingEntities.get("HeadBase").addLowerConstraint(
				new SensorConstraint(getSensors().get("ZB"), ConstraintType.LOWER, true));
		this.movingEntities.get("HeadBase").addLowerConstraint(
				new SensorConstraint(getSensors().get("R2"), ConstraintType.LOWER, true));
		this.movingEntities.get("HeadBase").addLowerConstraint(
				new SensorConstraint(getSensors().get("HMD"), ConstraintType.LOWER, true));
		this.movingEntities.get("Robot").addLowerConstraint(
				new SensorConstraint(getSensors().get("R3"), ConstraintType.LOWER, true));
		this.movingEntities.get("Robot").addLowerConstraint(
				new SensorConstraint(getSensors().get("RS"), ConstraintType.LOWER, true));
		this.movingEntities.get("HeadPuller").addLowerConstraint(
				new SensorConstraint(getSensors().get("HPU"), ConstraintType.LOWER, true));
		this.movingEntities.get("HeadMagnet").addLowerConstraint(
				new SensorConstraint(getSensors().get("HPM"), ConstraintType.LOWER, true));
		
		// The state constraints of SampleX are defined
		this.movingEntities.get("HeadHeadRotator").addStateConstraint(
				new SensorConstraint(getSensors().get("HMP"), ConstraintType.STATE, true));
	}

	public HashMap<String, MovingEntity> getMovingEntities() {
		return movingEntities;
	}

	public void setMovingEntities(HashMap<String, MovingEntity> movingEntities) {
		this.movingEntities = movingEntities;
	}



	public HashMap<String, Sensor> getSensors() {
		return sensors;
	}



	public void setSensors(HashMap<String, Sensor> sensors) {
		this.sensors = sensors;
	}
	

}
