/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will maintain the Track information for a Train on a 
 *          given set of blocks, on a track. 
 */
package TrainSimulator;

import java.util.ArrayList;

public class TrackStatus {
	//Fields
	private int blockStart, blockEnd;
        private boolean powerFailure = false;
	private TrackModel theLine;
        private ArrayList<Integer> brokenRailLocations = new ArrayList<Integer>();
	private ArrayList<Integer> badTrackCircuitLocations = new ArrayList<Integer>();
        
	//Constructor
	public TrackStatus(TrackModel theLine, int blockStart, int blockEnd){
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
		this.theLine = theLine;
	}
	
	//Track Model tells Wayside it detects a broken Rail, and where 
	public void brokenRailsDetected(int blockLocation){
            //Check if this location is already known to be broken
            if (this.brokenRailLocations.contains(new Integer(blockLocation))){
                //This Location is already known to be broken
            }
            else{
                //This is a new Location of the Track that is broken
                //Store the location of the brokenRail 
                this.brokenRailLocations.add(new Integer(blockLocation));
            }
	}
        
        //Return the list of Broken Rail Locations
	public ArrayList<Integer> brokenRailsDetected(){
            return (this.brokenRailLocations);
	}
        
        //Track Model tells Wayside if a broken rail was fixed and where 
	public void fixedBrokenRailHere(int blockLocation){
            //Check if this location is already known to be broken
            if (this.brokenRailLocations.contains(new Integer(blockLocation))){
                //Remove this location from our list
                this.brokenRailLocations.remove(new Integer(blockLocation));
            }
            else{
                //We never knew this location was broken!
                System.out.println("We Never Knew This Rail Location was Broken!");
            }
	}
        
	//Track Model tells Wayside it detects a Bad Track Circuit, and where 
	public void badTrackCircuitDetected(int blockLocation){
            //Check if this location is already known to be bad
            if (this.badTrackCircuitLocations.contains(new Integer(blockLocation))){
                //This Location is already known to be bad
            }
            else{
                //This is a new Location of the Track that is bad
                //Store the location of the bad Track Circuit
                this.badTrackCircuitLocations.add(new Integer(blockLocation));
            }
	}
        
        //Return the list of Bad Track Circuit Locations
	public ArrayList<Integer> badTrackCircuitLocations(){
            return (this.badTrackCircuitLocations);
	}
        
        //Track Model tells Wayside if a Bad Track Circuit was fixed and where 
	public void fixedBadTrackCircuitHere(int blockLocation){
            //Check if this location is already known to be bad
            if (this.badTrackCircuitLocations.contains(new Integer(blockLocation))){
                //Remove this location from our list
                this.badTrackCircuitLocations.remove(new Integer(blockLocation));
            }
            else{
                //We never knew this location had a bad track circuit!
                System.out.println("We Never Knew This Track Circuit was Bad!");
            }
	}
	
	//Track Model tells Track Controller there is a power failure 
	public void trackPowerFailureDetected(){
		//Store that this track has a power failure
                this.powerFailure = true;
	}
        
        //CTC Asks if there is a power failure
        public boolean trackPowerFailure(){
            return (this.powerFailure);
        }
        
        //Track Model tells Track Controller the Power has been Restored 
	public void trackPowerFailureRestored(){
		//Store that this track's power has been restored
                this.powerFailure = false;
	}
	
	//Ask the track for the block id the train is on and return it 
	public int getTrainBlockID(){
		return (this.theLine.getTrainBlockID());
	}
        
        //Ask the train for the train id on this block
	public int getTrainID(int blockItem){
         return (this.theLine.getTrainID(blockItem));   
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
