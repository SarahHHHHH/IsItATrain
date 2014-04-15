/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will implement a track controller for a given set of blocks on a track. 
 */
package TrainSimulator;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class TrackController extends JFrame{
  
  //Fields
  /*
	 * "trackAndTrainsTable" Contains:
	 * 1. The Block range this Track Controller is Monitoring
	 * 2. The Speed limit for the track the controller is on
	 * 3. If there is a Broken Rail contained in the blocks this Track Controller is monitoring
	 * 4. Which Block a train is currently on
	 * 5. If there is a Track Circuit Failure contained in the blocks this Track Controller is monitoring
	 * 6. If there is a Power Failure contained in the blocks this Track Controller is monitoring
	 */
  private HashMap<String, String> trackAndTrainsTable;
  private String command="";
  private String generalMessage="";
  private String malfunctionMessage ="";
  private boolean flagMalfunction = false;
  private int trackControllerIdentity;
  private boolean CTCAcknowledgment = true; 
  
  //Logic Objects to Move Switches
  private ArrayList<ValidateBooleanLogic> switchFromCloseToOpen = new ArrayList<ValidateBooleanLogic>();
  private ArrayList<ValidateBooleanLogic> switchFromOpenToClose = new ArrayList<ValidateBooleanLogic>();
  private ArrayList<String> logicPoints = new ArrayList<String>();
  private ArrayList<String> logicPoints_RWC = new ArrayList<String>();
  
  private SafetyTrackSpeed _SafetyTrackSpeed;
  private int[] blockRange = new int[2];
  private String logicToLoad ="";
  private RailwayCrossing _RailwayCrossing;
  private TrackModel theLine;
  PhantomCTCGUI ctc;
  
  public TrackController(int identityNum, int firstBlock, int endBlock, TrackModel theLine, PhantomCTCGUI ctc){
	this.trackControllerIdentity = identityNum;
	this.blockRange[0] = firstBlock;
	this.blockRange[1] = endBlock;
	this.ctc = ctc;
	this.theLine = theLine;
	
	/*Create the logicTable for the the "ValidateBooleanLogic" Class: -> Done
	 	* DEALING WITH TRACK CHANGES: -> From Close to Open
	 		* 1.Get the Block ID that the switch is located on
	 		* 2.Get the Block ID of the Switch's Close State.
	 		* 3.Get the Block ID of the Switch's Open State.
	 			* 3.1 Get the Block ID of the Block before the Switch's Open State.
	 		* TRUTH TABLE:
	 			* 1_BlockID | 2_BlockID | 3_BlockID | 3.1_BlockID | Result (to switch or not)
	 			* 	*			*			*			TRUE		FALSE
	 			* 	TRUE		*			*			*			FALSE
	 			* 	*			TRUE		*			*			FALSE
	 			* 	*			*			TRUE		*			FALSE
	 			* 	FALSE		FALSE		FALSE		FALSE		TRUE
	 */
	this.logicToLoad = "(!&(!&(!&(!))))";
	for (int counter = 0; counter < 3; counter++)
		this.switchFromCloseToOpen.add(new ValidateBooleanLogic(this.logicToLoad));
	
	/*Create the logicTable for the the "ValidateBooleanLogic" Class: -> Done
 		* DEALING WITH TRACK CHANGES: -> From Open to Close
	 		* 1.Get the Block ID that the switch is located on
	 			* 1.1 Get the Block ID of the Block before the Switch's Position. 
	 		* 2.Get the Block ID of the Switch's Close State.	
	 		* 3.Get the Block ID of the Switch's Open State.
 				* 3.1 Get the Block ID of the Block before the Switch's Open State. 
 			* TRUTH TABLE:
	 			* 1_BlockID | 1.1_BlockID | 2_BLockID | 3_BlockID | 3.1_BlockID | Result (to switch or not)
	 			*	TRUE		*			*			*			*			  FALSE 
	 			*	*			TRUE		*			*			*			  FALSE
	 			*	*			*			TRUE		*			*			  FALSE
	 			*	*			*			*			TRUE		*		      FALSE
	 			*	*			*			*			*			TRUE		  FALSE
	 			*	FALSE		FALSE		FALSE		FALSE		FALSE		  TRUE
 	*/
	this.logicToLoad = "(!&(!&(!&(!&(!)))))";
	for (int counter = 0; counter < 3; counter++)
		this.switchFromOpenToClose.add(new ValidateBooleanLogic(this.logicToLoad));
	
	
	//////////////////////////////////////////////////////////////////////////
	
	//Instantiate SafetyTrackSpeed Object -> Done
	this._SafetyTrackSpeed = new SafetyTrackSpeed(this.blockRange[0], this.blockRange[1], theLine);
	
	//Instantiate RailwayCrossing Object
	this._RailwayCrossing = new RailwayCrossing(theLine);
	
    //Set the Default values to the trackAndTrainsTable
    /*
     * "trackAndTrainsTable" Contains:
     * 1. The Block range this Track Controller is Monitoring
     * 2. If there is a Broken Rail contained in the blocks this Track Controller is monitoring
     * 3. If there is a Track Circuit Failure contained in the blocks this Track Controller is monitoring
     * 4. If there is a Power Failure contained in the blocks this Track Controller is monitoring
     */
    //Stores Block Range
    //this.trackAndTrainsTable.put("Block_Range_Start", this.blockRange[0]+"");
    //this.trackAndTrainsTable.put("Block_Range_Stop", this.blockRange[1]+"");
    /* Stores whether there is Broken Rail Detected within any of the block that this
     * Track Controller Monitors
     */
        //this.trackAndTrainsTable.put("Broken_Rail", "0");
		
		/* Stores whether there is a Track Circuit Failure within any of the block that this
		 * Track Controller Monitors
		*/
		//this.trackAndTrainsTable.put("Track_Circuit_Failure", "0");
		
		/* Stores whether there is a Power Failure within any of the block that this
		 * Track Controller Monitors
		*/
		//this.trackAndTrainsTable.put("Power_Failure", "0");
	
  }
  
  //CTC calls this method to get the track status from this track controller
  public TrackStatus getTrackStatus(){
		//Instantiate TrackStatus Object and send it to CTC
		return (new TrackStatus(this.theLine, this.blockRange[0], this.blockRange[1]));
  }
  
  //Set Track Controller Identity -> Done
  public void setTrackControllerIdentifier(int identity){
	  this.trackControllerIdentity = identity;
  }
  
  //Get Track Controller Identity -> Done
  public int getTrackControllerIdentifier(){
	  return (this.trackControllerIdentity);
  }
  
  //Push the desired speed from CTC to Track Model -> Done
  public double getSpeed(int trainID, int block_ID){
	  /*
	   *1.This Method is called by the Track Model
	   *2.Get speed form CTC
	   *3.Compare the speed with the track's speed limit
	   *4.Pass The Speed to the track model
	   *5.Set CTCAcknowledgement
	   */
	  double tempSpeed = this.ctc.getSpeed(trainID);
	  
	  boolean speedIsGood = this._SafetyTrackSpeed.checkSpeed(tempSpeed, block_ID);
	  
	  if (speedIsGood){
		  //should I update track Status...?
		  
		  //Store that we acknowledged the request and it is going through
		  this.CTCAcknowledgment = true;
		  
		  return (tempSpeed);
	  }
	  else{
		  //Don't Pass along the speed
		  //Store that we are not acknowledging the request and it is NOT going through
		  this.CTCAcknowledgment = false;
	  }
	  return ((this._SafetyTrackSpeed.getSafeSpeed(block_ID)));
  }
  
  //Push the desired authority from CTC to Track Model ->..Done
  public double getAuthority(int trainID){
	  /*
	   *1.This Method is called by the TrackModel
	   *2.Get Authority from CTC
	   *2.Pass The Authority to the track model
	   *3.Set CTCAcknowledgment
	   */
	  
	  //Store that we acknowledged the request and it is going through
	  this.CTCAcknowledgment = true;
	  
	  return (this.ctc.getAuthority(trainID));
  }
  
  //Return the Speed Limit for a given block -> Done
  public double getSpeedLimit(int blockID){
	  // Check this track's speed limit from the SafetyTrackSpeed Object and Return it.
	  return (this._SafetyTrackSpeed.getSafeSpeed(blockID));
  }
  
  //Return the Railway Crossing associated with this Controller
  public RailwayCrossing getRailwayCrossing(){
      return (this._RailwayCrossing);
  }
  
  //Decides whether we need to drop the railway crossing bar -> Done
  public boolean dropCrossBar(int crossingBlock_ID, TrackModel theLine){
	
	//Empty out old logic points
	this.logicPoints_RWC = new ArrayList<String>();
	
	//LV: Getting the Block ID of the railway crossing, store train presence.
	this.logicPoints_RWC.add((theLine.blocks.get(crossingBlock_ID).trainOnBlock()) ? "1" : "0");
	
	//LV: Getting the Block ID of the the block before railway crossing, store train presence.
	logicPoints_RWC.add((theLine.blocks.get((crossingBlock_ID-1)).trainOnBlock()) ? "1" : "0");
	
	return (this._RailwayCrossing.doWeDropCrossing(logicPoints_RWC));
  }
  
  //Decides whether to raise crossing bar ->
  public boolean raiseCrossBar(int crossingBlock_ID, TrackModel theLine){
	  
	//Empty out old logic points
	this.logicPoints_RWC = new ArrayList<String>();
		
	//LV: Getting the Block ID of the railway crossing, store train presence.
	this.logicPoints_RWC.add((theLine.blocks.get(crossingBlock_ID).trainOnBlock()) ? "1" : "0");
	
	//LV: Getting the Block ID of the the block before railway crossing, store train presence.
	logicPoints_RWC.add((theLine.blocks.get((crossingBlock_ID-1)).trainOnBlock()) ? "1" : "0");
	
	return (this._RailwayCrossing.doWeRaiseCrossing(logicPoints_RWC));  
  }
  
  // Receives a switch Command from CTC, and decides whether to obey or not-> Done
  public boolean trackChange(TrackModel theLine, int block_ID, int openOrClose){
	  
	  int closedState_ID, openedState_ID;
	  Switch tempSwitch = null;
	  
	  //Empty out old logic points
	  this.logicPoints = new ArrayList<String>();
		
	  /*
	   * 1. Get the Block ID the Switch is Located on, store if there is train presence. 
	   * 2. Get the Block ID of the Switch State to Be, store if there is train presence. 
	   * 3. If Switch State to be is Opened: 
	   		* 3.1 Get the Block ID of the Closed Switch State, store if there is train presence. 
	   		* 3.1 Get the Block ID of the block before the Opened Switch State's Block ID,
	   		     store if there is train presence.
	   * 4. If Switch State to be is Closed:
	   		* 4.1 Get the Block ID of the block before the Switch's Location, 
	   		      store if there is train presence. 
	   		* 4.2 Get the Block ID of the Closed Switch State, store if there is train presence. 
	   		* 4.3 Get the Block ID of the Block before the Open Switch State, 
	   		      store if there is train presence. 
	   */
	  
	  switch (openOrClose){
	  	case 1: //To be opened
	  		//LV: Getting the Block ID of where the Switch is located on, store train presence. 
	  		this.logicPoints.add((theLine.blocks.get(block_ID).trainOnBlock()) ? "1" : "0");
	  		
	  		//Finding the right switch we are dealing with
	  		for (Switch _switch : theLine.switches ){
	  			if (block_ID == _switch.block_id[0]){
	  				tempSwitch = _switch;
	  				break;
	  			}
	  		}
	  		
	  		//Storing the Block ID of the Switch Closed State
	  		closedState_ID = tempSwitch.block_id[2];
	  		//Storing the Block ID of the Switch Opened State
	  		openedState_ID = tempSwitch.block_id[1];
	  		
	  		//LV: Getting the Block ID of the Closed Switch State, store train presence.
	  		this.logicPoints.add((theLine.blocks.get(closedState_ID).trainOnBlock()) ? "1" : "0");
	  		
	  		//LV: Getting the Block ID of the Opened Switch Sate, store train presence.
	  		this.logicPoints.add((theLine.blocks.get(openedState_ID).trainOnBlock()) ? "1" : "0");
	  		
	  		//LV: Getting the Block ID of the Block before the Opened Switch Sate, store train presence.
	  		this.logicPoints.add((theLine.blocks.get(openedState_ID-1).trainOnBlock()) ? "1" : "0");
	  		break;
	  	case 0: //To be closed
	  		//LV: Getting the Block ID of where the Switch is located on, store train presence. 
	  		this.logicPoints.add((theLine.blocks.get(block_ID).trainOnBlock()) ? "1" : "0");
	  		
	  		//LV: Getting the Block ID of the Block before where Switch is located on, store train presence. 
	  		this.logicPoints.add((theLine.blocks.get(block_ID-1).trainOnBlock()) ? "1" : "0");
	  		
	  		//Finding the right switch we are dealing with
	  		for (Switch _switch : theLine.switches ){
	  			if (block_ID == _switch.block_id[0]){
	  				tempSwitch = _switch;
	  				break;
	  			}
	  		}
	  		
	  		//Storing the Block ID of the Switch Closed State
	  		closedState_ID = tempSwitch.block_id[2];
	  		//Storing the Block ID of the Switch Opened State
	  		openedState_ID = tempSwitch.block_id[1];
	  		
	  		//LV: Getting the Block ID of the Closed Switch State, store train presence.
	  		this.logicPoints.add((theLine.blocks.get(closedState_ID).trainOnBlock()) ? "1" : "0");
	  		
	  		//LV: Getting the Block ID of the Opened Switch Sate, store train presence.
	  		this.logicPoints.add((theLine.blocks.get(openedState_ID).trainOnBlock()) ? "1" : "0");
	  		
	  		//LV: Getting the Block ID of the Block before the Opened Switch Sate, store train presence.
	  		this.logicPoints.add((theLine.blocks.get(openedState_ID-1).trainOnBlock()) ? "1" : "0"); 
	  		break;
	  }
	  
	  //Perform Logic and Redundancy Check and Send CTC Response
	  return (this.redundancy3(theLine, openOrClose, block_ID));
	  
  }
  
  // Returns the CTC Acknowledgment -> Done
  public boolean passOrDeny(){
	//Simply return the acknowledgment
	  return (this.CTCAcknowledgment);
  }
  
  /* Performs a Redundancy check for track change and return whether a 
   * track change will occur or not-> Done
   */
  public boolean redundancy3(TrackModel theLine, int openOrClose, int block_ID){
		
		boolean val1=false, val2=false, val3=false;
		
		/*1.Get the Logic value from all three ValidateBooleanLogic Objects
		 *2.From the result decide if we are going to do the switch change or not.
		 *3.Set and send back CTCAcknowledgment
		 */
		
		switch(openOrClose){
			case 1: //To be Opened
				val1 = this.switchFromCloseToOpen.get(0).evaluateLogic(this.logicPoints);
		  		val2 = this.switchFromCloseToOpen.get(1).evaluateLogic(this.logicPoints);
		  		val3 = this.switchFromCloseToOpen.get(2).evaluateLogic(this.logicPoints);
				break;
			case 0: //To be Closed
				val1 = this.switchFromOpenToClose.get(0).evaluateLogic(this.logicPoints);
		  		val2 = this.switchFromOpenToClose.get(1).evaluateLogic(this.logicPoints);
		  		val3 = this.switchFromOpenToClose.get(2).evaluateLogic(this.logicPoints);
				break;
		}
		
		//Redundancy Check!
		  if (val1 == val2 && val2 == val3){
			  //tell trackModel Track Change!!!!!
			  theLine.trackSwitch(block_ID, openOrClose);
			  
			  //set that CTC command was completed
			  this.CTCAcknowledgment  = true;
		  }
		  else{
			  //Don't tell trackModel Track Change!!!!
			  
			  //set that CTC command was not completed
			  this.CTCAcknowledgment = false;
		  }
		  
		  
		return (this.passOrDeny());
    }
}
    
  
