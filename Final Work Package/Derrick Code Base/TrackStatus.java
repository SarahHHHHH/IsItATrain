/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will maintain the Track information for a Train on a 
 *          given set of blocks, on a track. 
 */
package TrainSimulator;

import java.util.ArrayList;


/**
 * This class will maintain the Track information for a Train on a 
 *          given set of blocks, on a track. 
 * 
 * @author Derrick Ward
 */
public class TrackStatus {
	//Fields
	private int blockStart, blockEnd;
        private boolean powerFailure = false;
	private TrackModel theLine;
        private ArrayList<Integer> brokenRailLocations = new ArrayList<Integer>();
	private ArrayList<Integer> badTrackCircuitLocations = new ArrayList<Integer>();
        
	/**
        * This Constructor creates a TrackStatus Object 
        * 
        * @param theLine The Track Line this controller is on
        * @param blockStart The first block this Track Controller monitors
        * @param blockEnd The last block this Track Controller monitors
        */
	public TrackStatus(TrackModel theLine, int blockStart, int blockEnd){
		this.blockStart = blockStart;
		this.blockEnd = blockEnd;
		this.theLine = theLine;
	}
	
	
        /** 
        * This method is called by Track Model: To tell Wayside it detects a 
        * broken Rail, and where.
        * 
        * @param blockLocation The ID of the Block
        */ 
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
        
        
        /** 
        * This method returns an ArrayList of Broken Rail Locations
        * 
        * @return An ArrayList of Broken Rail Locations 
        */ 
	public ArrayList<Integer> brokenRailsDetected(){
            return (this.brokenRailLocations);
	}
        
        
        /** 
        * This method is called by Track Model: To tell Wayside if a broken rail 
        * was fixed and where
        * 
        * @param blockLocation The ID of the block 
        */ 
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
        

        /** 
        * This method is called by Track Model: To tell Wayside it detects a 
        * Bad Track Circuit, and where
        * 
        * @param blockLocation The ID of the block 
        */
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
        
        //Return the list of 
        /** 
        * This method returns an ArrayList of Bad Track Circuit Locations
        * 
        * @return An ArrayList of Broken Rail Locations 
        */
	public ArrayList<Integer> badTrackCircuitLocations(){
            return (this.badTrackCircuitLocations);
	}
        
        
        /** 
        * This method is called by Track Model: To tell Wayside if a Bad Track
        * Circuit was fixed and where
        * 
        * @param blockLocation The ID of the block 
        */
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
	

        /** 
        * This method is called by Track Model: To tell Track Controller there
        * is a power failure
        */
	public void trackPowerFailureDetected(){
		//Store that this track has a power failure
                this.powerFailure = true;
	}
        

        /** 
        * This method is called by CTC: To see if there is a power failure
        * 
        * @return boolean value of whether there is a power failure or not
        */
        public boolean trackPowerFailure(){
            return (this.powerFailure);
        }
        
        
        /** 
        * This method is called by Track Model: To tell Track Controller the 
        * Power has been Restored
        */
	public void trackPowerFailureRestored(){
		//Store that this track's power has been restored
                this.powerFailure = false;
	}
	
	/** 
        * This method return the block ID the train is on
        * 
        * @return Block ID the train is on
        */ 
	public int getTrainBlockID(){
		return (this.theLine.getTrainBlockID());
	}
        
        
        /** 
        * This method return the Train ID, if a train is on the block
        * 
        * @param blockItem the ID of the Block
        * @return Train ID
        */
	public int getTrainID(int blockItem){
         return (this.theLine.getTrainID(blockItem));   
        }
        
	 
        /** 
        * This method returns the first block this Track Controller is
        * monitoring
        * 
        * @return The first Block ID this Track Controller is monitoring
        */
	public int getBlockStart(){
		return (this.blockStart);
	}
	
	
        /** 
        * This method returns the last block this Track Controller is
        * monitoring
        * 
        * @return The last Block ID this Track Controller is monitoring
        */
	public int getBlockEnd(){
		return (this.blockEnd);
	}
	
	
        /** 
        * This method returns the Track Line this Track Controller is on
        * 
        * @return The Track Line this Track Controller is on
        */
	public TrackModel getTrackLine(){
		return (this.theLine);
	}

}