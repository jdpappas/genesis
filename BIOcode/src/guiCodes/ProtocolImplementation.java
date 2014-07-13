package guiCodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/* The purpose of this class is to take the selected protocol the user selected and apply
 * the HighLevelMotion class instances for each stage of the experimental procedure
 */
public class ProtocolImplementation {

	// This is the protocol the user has selected
	private Protocol selectedProtocol;
	/* The purpose of this timer is to check in specific intervals if the program
	 * can move to the next stage eg. bead loading, pipetteExtraction and if so it
	 * calls the respective HighLevelMotion
	 */
	Timer highMotionTimer;
	
	public ProtocolImplementation() {
		
	} // close ProtocolImplementation constructor method

	public void run() {
		ActionListener highMotionTask = new ActionListener() {
			// The following are the stages that a protocol contains
			PipetteLoading pipetteLoading = new PipetteLoading();
			ExperimentalAction beadLoading = new ExperimentalAction(2, 
					selectedProtocol.beadStage.getVelocity(), selectedProtocol.beadStage.getTime());
			ExperimentalAction samplesLoading = new ExperimentalAction(3, 
					selectedProtocol.samplesStage.getVelocity(), selectedProtocol.samplesStage.getTime());
			ExperimentalAction detectionAbLoading = new ExperimentalAction(4, 
					selectedProtocol.detectionStage.getVelocity(), selectedProtocol.detectionStage.getTime());
			ExperimentalAction epLoading = new ExperimentalAction(5, 
					selectedProtocol.epStage.getVelocity(), selectedProtocol.epStage.getTime());
			FinalStage finalStage = new FinalStage();
			PipetteExtraction pipetteExtraction = new PipetteExtraction();
			
		public void actionPerformed(ActionEvent e) {
			// The 1ST "if" block does the PIPETTE LOADING just for one time.
			// If pipette loading has started then it is no longer called
			if (!pipetteLoading.isCompleted && !pipetteLoading.hasStarted) {
				System.out.println("Pipette Loading");
				pipetteLoading.run();
			} // close if
			
			
			// The 2ND "if" block does the BEAD LOADING just for one time.
			// If bead loading has started then it is no longer called
			if (pipetteLoading.isCompleted && !beadLoading.isCompleted && 
					!beadLoading.hasStarted) {
				System.out.println("Bead Loading");
				beadLoading.run();
			} // close if
			
			
			// The 3RD "if" block does the SAMPLES LOADING just for one time.
			// If samples loading has started then it is no longer called
			if (pipetteLoading.isCompleted && beadLoading.isCompleted && 
					!samplesLoading.hasStarted && !samplesLoading.isCompleted) {
				System.out.println("Samples Loading");
				samplesLoading.run();
			} // close if
						
			
			// The 4TH "if" block does the DETECTION_Ab LOADING just for one time.
			// If detectionAb loading has started then it is no longer called
			if (pipetteLoading.isCompleted && beadLoading.isCompleted && 
					samplesLoading.isCompleted &&
					!detectionAbLoading.hasStarted && !detectionAbLoading.isCompleted) {
				System.out.println("Detection Antibody Loading");
				detectionAbLoading.run();
			} // close if
					
			
			// The 5TH "if" block does the EP LOADING just for one time.
			// If EP loading has started then it is no longer called
			if (pipetteLoading.isCompleted && beadLoading.isCompleted && 
					samplesLoading.isCompleted && detectionAbLoading.isCompleted &&
					!epLoading.hasStarted && !epLoading.isCompleted) {
				System.out.println("EP Loading");
				epLoading.run();
			} // close if
						
					
			// The 6TH "if" block does the FINAL STAGE just for one time.
			// If FINAL STAGE has started then it is no longer called
			if (pipetteLoading.isCompleted && beadLoading.isCompleted && 
					samplesLoading.isCompleted && detectionAbLoading.isCompleted &&
					epLoading.isCompleted &&
					!finalStage.hasStarted && !finalStage.isCompleted) {
				System.out.println("Final Stage");
				finalStage.run();
			} // close if
			
						
			// The LAST "if" block does the pipette EXTRACTION just for one time.
			// If pipette extraction has started then it is no longer called
			if(pipetteLoading.isCompleted && beadLoading.isCompleted && 
					samplesLoading.isCompleted && detectionAbLoading.isCompleted &&
					epLoading.isCompleted && finalStage.isCompleted &&
					!pipetteExtraction.isCompleted && !pipetteExtraction.hasStarted) {
			System.out.println("Pipette Extraction");
			pipetteExtraction.run();
			// The timer does no longer need to search if there is another stage to be
			// implemented, as the pipette extraction is the last one.
			highMotionTimer.stop();
			} // close if 
			
		} // close ActionPerformed
		}; // close taskPerformer
		
		// Create a motiontimer if there is no other created before
		if (highMotionTimer==null) {
			highMotionTimer = new Timer(1000, highMotionTask);
		} // close if
		highMotionTimer.start();
	} // close run method
	
	public void setSelectedProtocol (Protocol sel) {
		selectedProtocol = sel;
	} // close setSelectedProtocol method
	
	
} // close ProtocolImplementation class
