package guiCodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import machineCodes.HeadBase;
import machineCodes.HeadMagnet;
import machineCodes.HeadPlunger;
import machineCodes.HeadRotator;
import machineCodes.Investigator;
import machineCodes.Machine;
import machineCodes.Robot;

public class ExperimentalAction extends HighLevelMotion {

	boolean magnetRotated = false;
	boolean magnetMoved = false;
	boolean headMoved1 = false;
	boolean robotMoved1 = false;
	boolean magnetMoved2 = false;
	boolean magnetRotated2 = false;
	boolean plungerMoved = false;
	boolean magnetRotated3 = false;
	boolean magnetMoved3 = false;
	boolean robotMoved2 = false;
	boolean headMoved2 = false;
	boolean headMoved3 = false;
	
	// The variable that stores in which plate will the experimental action take place.
	int plateNum; 
	
	Timer plungerTimer = null;
	int velocity;
	int time;
	
	public ExperimentalAction(int plateNum, int velocity, int washTime) {
		this.plateNum = plateNum;
		this.velocity = velocity;
		this.time = washTime;
	} // close ExperimentalAction constructor method

	
	@Override
	public void run() {
	super.run();

	ActionListener stateTask = new ActionListener() {
		HeadRotator hrotator = (HeadRotator) Investigator.find(Machine.getMovingParts(), "tag", "hrotator");
		HeadMagnet hmagnet = (HeadMagnet) Investigator.find(Machine.getMovingParts(), "tag", "hmagnet");
		HeadBase base = (HeadBase) Investigator.find(Machine.getMovingParts(), "tag", "Head");
		Robot robot = (Robot) Investigator.find(Machine.getMovingParts(), "tag", "robot");
		HeadPlunger hplunger = (HeadPlunger) Investigator.find(Machine.getMovingParts(), "tag", "hplunger");
		
		public void actionPerformed(ActionEvent e) {
			// The following "ifs" are responsible for calling the next method when the previous
			// is completed
			// STAGE 1 MAGNET ROTATION -----------------------------------------------------------
			if (!magnetRotated) {
				partMove(hrotator, 500, LOW);
				magnetRotated = true; 
			} // close (!magnetRotated) "if"
			
			/* The following "ifs" lie inside try-catch blocks that catch NullPointerException
			 * These exceptions are thrown from the autoMotion class. The reason is as follows:
			 * If e.g. the rotator is at the proper position, then it does not need to move.
			 * If it does not move, then there is no instance of autoMotion, so when the "if"
			 * condition checks if the graphicMotionTimer is running it throws a NullPointerException
			 * So in this case we just need to check if the previous stage is checked and if the 
			 * step to follow has not started yet. 
			 */
						
			// STAGE 2 MAGNET Z MOTION -----------------------------------------------------------
			try {
			if  (magnetRotated && !magnetMoved &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(hmagnet, 500, LOW);
				magnetMoved = true;
			} // close "if"
			} catch (NullPointerException ex) {
			if  (magnetRotated && !magnetMoved) {
				partMove(hmagnet, 500, LOW);
				magnetMoved = true;
			} // close "if"
			} // close try-catch block
			
			// STAGE 3 HEAD Z MOTION -----------------------------------------------------------
			try {
			if  (magnetRotated && magnetMoved && !headMoved1 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				headMove(base, plateNum);
				headMoved1 = true;
			} // close "if"
			} catch (NullPointerException ex) {
			if  (magnetRotated && magnetMoved && !headMoved1) {
				headMove(base, plateNum);
				headMoved1 = true;
			} // close "if"
			} // close try-catch block
			
			
			// In the following steps we do NOT NEED TRY - CATCH blocks, as the head must 
			// definitely move in order to get to the 1st carrier.
			// STAGE 4 ROBOT MOTION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && !robotMoved1 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(robot, 500, LOW);
				robotMoved1 = true;
			} // close "if"

			
			// STAGE 5 MAGNET DOWN MOTION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && !magnetMoved2 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(hmagnet, 500, HIGH);
				magnetMoved2 = true;
			} // close "if"

			
			// STAGE 6 MAGNET ROTATION TO NARROW POSITION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					!magnetRotated2 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(hrotator, 500, HIGH);
				magnetRotated2 = true;
			} // close "if"
			
			
			// STAGE 7 PLUNGER MOTION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && !plungerMoved &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				if (time>0) { // in case the user does not want to agitate
				plungerMotionBlock(velocity, time);
				} else {
				plungerMoved = true;
				} // close if
			} // close "if"
						
			
			// STAGE 8 MAGNET ROTATION TO WIDE POSITION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && plungerMoved && !magnetRotated3 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(hrotator, 500, LOW);
				magnetRotated3 = true;
			} // close "if"
			
			// STAGE 9 MAGNET MOVE UP -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && plungerMoved && magnetRotated3 && !magnetMoved3 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(hmagnet, 500, LOW);
				magnetMoved3 = true;
			} // close "if"
						
				
			// STAGE 10 ROBOT MOVE -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && plungerMoved && magnetRotated3 && magnetMoved3 &&
					!robotMoved2 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(robot, 500, HIGH);
				robotMoved2 = true;
			} // close "if"
						
			
			// STAGE 11 HEAD MOVE -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && plungerMoved && magnetRotated3 && magnetMoved3 &&
					robotMoved2 && !headMoved2 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				headMove(base, 7);
				headMoved2 = true;
			} // close "if"
					
			
			// STAGE 11 HEAD MOVE -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && plungerMoved && magnetRotated3 && magnetMoved3 &&
					robotMoved2 && headMoved2 && !headMoved3 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				headMove(base, 0);
				headMoved3 = true;
			} // close "if"
						
			
			// END -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && magnetMoved2 &&
					magnetRotated2 && plungerMoved && magnetRotated3 && magnetMoved3 &&
					robotMoved2 && headMoved2 && headMoved3 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				stateTimer.stop();
				isCompleted=true;
			} // close "if"
			
		} // close ActionPerformed
		}; // close taskPerformer
		
		// Create a graphicMotionTimer if there is no other created before
		if (stateTimer==null) {
			stateTimer = new Timer(100, stateTask);
		} // close if
		stateTimer.start();
			
	} // close run method
	

	public void plungerMotionBlock (int velocity, int time) {
		final int numOfLoops = timeToLoops(time, velocity);
		HeadPlunger hplunger = (HeadPlunger) Investigator.find(Machine.getMovingParts(), "tag", "hplunger");
		hplunger.setMotorSpeed(velocity);
		

		ActionListener plungerTask = new ActionListener() {
		HeadPlunger hplunger = (HeadPlunger) Investigator.find(Machine.getMovingParts(), "tag", "hplunger");
			int i = 0;
	
			public void actionPerformed(ActionEvent e) {
			if (hplunger.getCurrentStep()==hplunger.getLowStep()) {
				partMove(hplunger, 100, HIGH);
				i++;
			} else if (hplunger.getCurrentStep()==hplunger.getHighStep()) {
				partMove(hplunger, 100, LOW);	
				i++;
			} // close if
			
			if (i==numOfLoops-1) {
				plungerTimer.stop();
				plungerMoved = true;
			} // close if
		} // close action performed method
		}; // close plungerTask class
				
		// Create a plungerTimer if there is no other created before
		if (plungerTimer==null) {
			plungerTimer = new Timer(100, plungerTask);
		} // close if
		plungerTimer.start();
			
	} // close plungerLoop method
	
	
	// The purpose of this method is to calculate how many times the plunger must move
	// up and down based on the time given at the protocol.
	public int timeToLoops(int time, int velocity) {
		int numOfLoops = time;
		return numOfLoops;
	} // close timeToLoops method
	
	
	// The purpose of this method is to define the max speed of the motor in pulses/s
	// based on the speed given at the protocol.
	public int speedToMaxPulses(int velocity) {
		int numOfPulses = velocity;
		return numOfPulses;
	} // close speedToMaxPulses method
	
} // close ExperimentalAction class