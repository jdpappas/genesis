package guiCodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import machineCodes.HeadBase;
import machineCodes.HeadMagnet;
import machineCodes.HeadPuller;
import machineCodes.HeadRotator;
import machineCodes.Investigator;
import machineCodes.Machine;
import machineCodes.Robot;

public class PipetteExtraction extends HighLevelMotion {

	boolean magnetRotated = false;
	boolean magnetMoved1 = false;
	boolean headMoved1 = false;
	boolean robotMoved1 = false;
	boolean magnetMoved2 = false;
	boolean pullerMoved1 = false;
	boolean pullerMoved2 = false;
	boolean magnetMoved3 = false;
	boolean robotMoved2 = false;
	boolean headMoved2 = false;
	
	public PipetteExtraction() {
		
	} // close PipetteExtraction constructor method

	
	@Override
	public void run() {
	super.run();
	
	ActionListener stateTask = new ActionListener() {
		HeadRotator hrotator = (HeadRotator) Investigator.find(Machine.getMovingParts(), "tag", "hrotator");
		HeadMagnet hmagnet = (HeadMagnet) Investigator.find(Machine.getMovingParts(), "tag", "hmagnet");
		HeadBase base = (HeadBase) Investigator.find(Machine.getMovingParts(), "tag", "Head");
		Robot robot = (Robot) Investigator.find(Machine.getMovingParts(), "tag", "robot");
		HeadPuller hpuller = (HeadPuller) Investigator.find(Machine.getMovingParts(), "tag", "hpuller");
		
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
		if  (magnetRotated && !magnetMoved1 &&
			!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(hmagnet, 500, LOW);
			magnetMoved1 = true;
		} // close "if"
		} catch (NullPointerException ex) {
		if  (magnetRotated && !magnetMoved1) {
			partMove(hmagnet, 500, LOW);
			magnetMoved1 = true;
		} // close "if"
		} // close try-catch block
		
		// STAGE 3 HEAD Z MOTION -----------------------------------------------------------
		try {
		if  (magnetRotated && magnetMoved1 && !headMoved1 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			headMove(base, 1);
			headMoved1 = true;
		} // close "if"
		} catch (NullPointerException ex) {
		if  (magnetRotated && magnetMoved1 && !headMoved1) {
			headMove(base, 1);
			headMoved1 = true;
		} // close "if"
		} // close try-catch block
		
		
		// In the following steps we do NOT NEED TRY - CATCH blocks, as the head must 
		// definitely move in order to get to the 1st carrier.
		// STAGE 4 ROBOT MOTION -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && !robotMoved1 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(robot, 500, LOW);
			robotMoved1 = true;
		} // close "if"

		// STAGE 5 MAGNET MINI Z MOTION -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && !magnetMoved2 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(hmagnet, 500, ""+hmagnet.getCurrentStep(), ""+(hmagnet.getCurrentStep()+1000));
			magnetMoved2 = true;
		} // close "if"

		// STAGE 6 PULLER EXTRACTION -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && magnetMoved2 &&
			!pullerMoved1 &&
			!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(hpuller, 500, "HIGH");
			pullerMoved1 = true;
		} // close "if"

		// STAGE 7 PULLER RETURN -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && magnetMoved2 &&
				pullerMoved1 && !pullerMoved2 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(hpuller, 500, "LOW");
			pullerMoved2 = true;
		} // close "if"

		// STAGE 8 MAGNET MINI RETURN -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && magnetMoved2 &&
				pullerMoved1 && pullerMoved2 && !magnetMoved3 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(hmagnet, 500, ""+(hmagnet.getCurrentStep()-1000), ""+hmagnet.getCurrentStep());
			magnetMoved3 = true;
		} // close "if"
		
		// STAGE 9 ROBOT MOTION -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && magnetMoved2 &&
				pullerMoved1 && pullerMoved2 && magnetMoved3 && !robotMoved2 && 
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			partMove(robot, 500, HIGH);
			robotMoved2 = true;
		} // close "if"
					
		// STAGE 10 HEAD MOTION ----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && magnetMoved2 &&
				pullerMoved1 && pullerMoved2 && magnetMoved3 && robotMoved2 && !headMoved2 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			headMove(base, 0);
			headMoved2 = true;
		} // close "if"
					
		
		// END -----------------------------------------------------------
		if  (magnetRotated && magnetMoved1 && headMoved1 && robotMoved1 && magnetMoved2 &&
				pullerMoved1 && pullerMoved2 && magnetMoved3 && robotMoved2 && headMoved2 &&
				!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
			stateTimer.stop();
			isCompleted=true;
		} // close "if"
					
					
	} // close ActionPerformed
	}; // close taskPerformer
	
	// Create a stateTimer if there is no other created before
	if (stateTimer==null) {
		stateTimer = new Timer(100, stateTask);
	} // close if
	stateTimer.start();
		
	} // close run method
	
} // close PipetteExtraction class
