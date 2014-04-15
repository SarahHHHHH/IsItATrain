/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will maintain the Maximum Speed information for a given set of blocks on a track. 
 */
package TrainSimulator;

public class SafetyTrackSpeed {
	//Fields
	/*
	 * "safeSpeed" Contains:
	 * 1. The safe speed for the track this track controller is on
	 * */
	private double safeSpeed;
	private TrackModel theLine;
	
	//Constructor -> Done
	public SafetyTrackSpeed(int blockStart, int blockEnd, TrackModel theLine){
		
		//Store the TrackModel object that this Track Controller talks to
		this.theLine = theLine;
		
	}
	
	//Return the safe speed for this block -> Done
	public double getSafeSpeed(int block_ID){
		this.safeSpeed = theLine.blocks.get(block_ID).speedLimit;
		
		return (this.safeSpeed);
	}
	
	//Check whether the desired speed is safe and return boolean -> Done
	public boolean checkSpeed(double speedWanted, int block_ID){
            
            this.getSafeSpeed(block_ID);
            
		boolean speedIsSafe = false;
		
		if (speedWanted > this.safeSpeed){
			speedIsSafe = false;
			return speedIsSafe;
		}
		else{
			speedIsSafe = true;
			return speedIsSafe;
		}
	}
}
