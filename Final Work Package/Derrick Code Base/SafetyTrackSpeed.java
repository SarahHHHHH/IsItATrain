/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will maintain the Maximum Speed information for a given set of blocks on a track. 
 */
package TrainSimulator;


/**
* This class will maintain the Maximum Speed information for a
* given set of blocks on a track.
* 
* @author Derrick Ward
*/
public class SafetyTrackSpeed {
	//Fields
	/*
	 * "safeSpeed" Contains:
	 * 1. The safe speed for the track this track controller is on
	 * */
	private double safeSpeed;
	private TrackModel theLine;
	
	
        /**
        * This constructor will create a SafetyTrackSpeed Object and store the 
        * Track Line it is monitoring blocks on. 
        * 
        * @param blockStart The first block this Track Controller monitors
        * @param blockEnd The last block this Track Controller monitors
        * @param theLine The Track Line this controller is on
        */
	public SafetyTrackSpeed(int blockStart, int blockEnd, TrackModel theLine){
		
		//Store the TrackModel object that this Track Controller talks to
		this.theLine = theLine;
		
	}
	
	
        /**
        * This method will return the speed limit for this block 
        * 
        * @param block_ID The ID of the block 
        * @return The speed limit of the block
        */
	public double getSafeSpeed(int block_ID){
		this.safeSpeed = theLine.blocks.get(block_ID).speedLimit;
		
		return (this.safeSpeed);
	}
	
	
        /**
        * This method checks whether the desired speed is safe
        * 
        * @param speedWanted The desired speed
        * @param block_ID The ID of the block 
        * @return Whether or not the desired speed is OK
        */
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
