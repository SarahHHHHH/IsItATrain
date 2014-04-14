/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will maintain the Track and Train information for a Train on a 
 *          given set of blocks, on a track. 
 */
package TrainSimulator;
import java.util.HashMap;

public class TrackStatus {
	//Fields
	private int blockStart, blockEnd;
	private TrackModel theLine;
	
	//Constructor->Done
	public TrackStatus(TrackModel theLine, int blockStart, int blockEnd){
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
		this.theLine = theLine;
	}
	
	//Ask track if there is a broken rail and return the value -> Done
	public boolean brokenRailDetected(){
		/*1.Get Track Status from Track Model
		 *2.Update your Table of info
		 *3.If there is a broken rail alert track controller-main. Track Controller-main will then alert CTC.
		 */
		return false;
	}
	
	//Ask track if there is a badTrackCircuit and return the value -> Done
	public boolean badTrackCiruitDetected(){
		/*1.Get Track Status from Track Model
		 *2.Update your Table of info
		 *3.If there is a bad track circuit alert track controller-main. Track Controller-main will
		 *  then alert CTC.
		 */
		return false;
	}
	
	//Ask track if there is a track power failure and return the value -> Done
	public boolean trackPowerFailureDetected(){
		/*1.Get Track Status from Track Model
		 *2.Update your Table of info
		 *3.If there is a track power failure alert track controller-main. Track Controller-main will
		 *  then alert CTC.
		 */
		return false;
	}
	
	//Ask the track for the block id the train is on and return it -> Done
	public int getTrainBlockID(){
		return (this.theLine.getTrainBlockID());
	}
	
	//Return the first block this Track Controller is monitoring
	public int getBlockStart(){
		return (this.blockStart);
	}
	
	//Return the last block this Track Controller is monitoring
	public int getBlockEnd(){
		return (this.blockEnd);
	}
	
	//Return the Track Line this Track Controller is on
	public TrackModel getTrackLine(){
		return (this.theLine);
	}

}
