package guiCodes;

import java.io.Serializable;

public class Protocol implements Serializable {

	String name; 
	Stage beadStage;
	Stage samplesStage;
	Stage detectionStage;
	Stage epStage;
	int finalBufferFlow;
	boolean cooling5;
	boolean cooling6;
	
	public Protocol(String name) {
		this.name=name;
	} // close Protocol constructor method
	

	public class Stage implements Serializable {
		private int velocity;
		private int time;
		private int wash;
		
		public Stage(int v, int t, int w) {
			setVelocity(v);
			setTime(t);
			setWash(w);
		} // close Stage constructor method

		public int getVelocity() {
			return velocity;
		}

		public void setVelocity(int velocity) {
			this.velocity = velocity;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}

		public int getWash() {
			return wash;
		}

		public void setWash(int wash) {
			this.wash = wash;
		}
	} // close Stage class
	
	
} // close Protocol class
