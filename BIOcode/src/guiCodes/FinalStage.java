package guiCodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import machineCodes.HeadBase;
import machineCodes.HeadMagnet;
import machineCodes.HeadRotator;
import machineCodes.Investigator;
import machineCodes.Machine;
import machineCodes.Robot;

public class FinalStage extends HighLevelMotion {

	boolean magnetRotated = false;
	boolean magnetMoved = false;
	boolean headMoved1 = false;
	boolean robotMoved1 = false;
	boolean robotMoved2 = false;
	boolean headMoved2 = false;
	boolean headMoved3 = false;
	
	public FinalStage() {
		// TODO Auto-generated constructor stub
	} // close FinalStage constructor method

	@Override
	public void run() {
	super.run();

	ActionListener stateTask = new ActionListener() {
		HeadRotator hrotator = (HeadRotator) Investigator.find(Machine.getMovingParts(), "tag", "hrotator");
		HeadMagnet hmagnet = (HeadMagnet) Investigator.find(Machine.getMovingParts(), "tag", "hmagnet");
		HeadBase base = (HeadBase) Investigator.find(Machine.getMovingParts(), "tag", "Head");
		Robot robot = (Robot) Investigator.find(Machine.getMovingParts(), "tag", "robot");
		
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
				headMove(base, 6);
				headMoved1 = true;
			} // close "if"
			} catch (NullPointerException ex) {
			if  (magnetRotated && magnetMoved && !headMoved1) {
				headMove(base, 6);
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


			// STAGE 5 ROBOT BACK MOTION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && !robotMoved2 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				partMove(robot, 500, HIGH);
				robotMoved2 = true;
			} // close "if"

			// STAGE 6 HEAD Z BOTTOM MOTION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && robotMoved2 
					&& !headMoved2 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				headMove(base, 7);
				headMoved2 = true;
			} // close "if"
			
			
			// STAGE 6 HEAD Z TOP MOTION -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && robotMoved2 
					&& headMoved2 && !headMoved3 &&
					!(Machine.getDrawPanel().getController().getAutoMotion().getGraphicMotionTimer().isRunning())) {
				headMove(base, 0);
				headMoved3 = true;
			} // close "if"
						
			// END -----------------------------------------------------------
			if  (magnetRotated && magnetMoved && headMoved1 && robotMoved1 && robotMoved2 
					&& headMoved2 && headMoved3 &&
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
	
} // close FinalStage class
