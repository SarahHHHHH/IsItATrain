/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will implement a track controller for a given set of blocks on a track. 
 */
package TrainSimulator;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.io.*;


/**
 * This class will implement a track controller for a given set of blocks on a track. 
 * 
 * @author Derrick Ward
 */
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
  
  public SafetyTrackSpeed _SafetyTrackSpeed;
  private TrackStatus _TrackStatus;
  private int[] blockRange = new int[2];
  private String logicToLoad ="";
  private RailwayCrossing _RailwayCrossing;
  private TrackModel theLine;
  PhantomCTCGUI ctc;
  
  
 /**
 * This Constructor creates a TrackController object that can modify the Switches, 
 * Crossing Lights, and Railway Crossings of a track. 
 * 
 * <p>
 * This constructor creates a WaySide Controller as well as all of its
 * necessary safety objects. The PLC Program (ValidateBooleanLogic) is also
 * loaded and instantiated with its logic here.
 * 
 * @param  identityNum  A number Identifying this Wayside Controller Object
 * @param  firstBlock The First Block Location this Controller Monitors
 * @param  endBlock  The Last Block Location this Controller Monitors
 * @param  theLine The Track this Wayside Controller is on
 * @param  ctc The CTC Monitoring the whole Train Simulation System
 */
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
	 			* 	*           *		*           TRUE		FALSE
	 			* 	TRUE        *		*           *			FALSE
	 			* 	*           TRUE	*           *			FALSE
	 			* 	*           *		TRUE        *			FALSE
	 			* 	FALSE       FALSE	FALSE       FALSE		TRUE
	 */
	//this.logicToLoad = "(!&(!&(!&(!))))";
        try{
            this.logicToLoad = (new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("switchFromCloseToOpenLogic.txt"))))).readLine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
	 			*	TRUE	*               *           *		*		FALSE 
	 			*	*	TRUE		*           *		*		FALSE
	 			*	*	*		TRUE        *		*		FALSE
	 			*	*	*		*           TRUE	*		FALSE
	 			*	*	*		*           *		TRUE		FALSE
	 			*	FALSE	FALSE		FALSE       FALSE	FALSE		TRUE
 	*/
	//this.logicToLoad = "(!&(!&(!&(!&(!)))))";
	try{
            this.logicToLoad = (new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("switchFromOpenToCloseLogic.txt"))))).readLine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (int counter = 0; counter < 3; counter++)
		this.switchFromOpenToClose.add(new ValidateBooleanLogic(this.logicToLoad));
	
	
	//////////////////////////////////////////////////////////////////////////
	
	//Instantiate SafetyTrackSpeed Object -> Done
	this._SafetyTrackSpeed = new SafetyTrackSpeed(this.blockRange[0], this.blockRange[1], theLine);
	
	//Instantiate RailwayCrossing Object
	this._RailwayCrossing = new RailwayCrossing(theLine);
        
        //Instantiate TrackStatus Object
        _TrackStatus = new TrackStatus(this.theLine, this.blockRange[0], this.blockRange[1]);	
  }
  
  
 /** 
 * This method returns this Controller's TrackStatus Object. 
 * 
 * This is object allows the CTC to get the track status for this 
 * track controller.
 * @return Returns a this Controller's Track Status Object
 */
  public TrackStatus getTrackStatus(){
	return (this._TrackStatus);
  }
  
  
/** 
 * This method sets the identity of this WaySide Controller Object
 * 
 * @param  identity  A number Identifying this Wayside Controller Object
 */
  public void setTrackControllerIdentifier(int identity){
	  this.trackControllerIdentity = identity;
  }
  
  
 /** 
 * This method return the identity of this WaySide Controller Object
 * 
 * @return  A number Identifying this Wayside Controller Object
 */
  public int getTrackControllerIdentifier(){
	  return (this.trackControllerIdentity);
  }
  
  
/** 
 * This method returns the desired Train Speed from CTC
 * 
 * @param trainID The Id of the Train
 * @param block_ID The Id of the Block
 * @return  The Speed the CTC wants this train to run at
 */
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
  
/** 
 * This method returns the desired Train Authority from CTC
 * 
 * @param trainID The Id of the Train
 * @return  The Authority the CTC wants this train travel
 */
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
  
/** 
 * This method return the Speed Limit for a Block
 * 
 * @param blockID The Id of the Block
 * @return  The Speed Limit for a Block
 */
  public double getSpeedLimit(int blockID){
	  // Check this track's speed limit from the SafetyTrackSpeed Object and Return it.
	  return (this._SafetyTrackSpeed.getSafeSpeed(blockID));
  }
  
/** 
 * This method return the Railway Crossing associated with this Controller
 * 
 * @return The Controller Associated with this Controller
 */
  public RailwayCrossing getRailwayCrossing(){
      return (this._RailwayCrossing);
  }
  
/** 
 * This method decides whether we need to drop the Railway Crossing Bar.
 * 
 * @param crossingBlock_ID The Id of the Block the Crossing is located on
 * @param theLine the Line this Controller and the Crossing is on
 * @return  Whether the Railway Crossing Bar was Dropped
 */
  public boolean dropCrossBar(int crossingBlock_ID, TrackModel theLine){
	
	//Empty out old logic points
	this.logicPoints_RWC = new ArrayList<String>();
	
	//LV: Getting the Block ID of the railway crossing, store train presence.
	this.logicPoints_RWC.add((theLine.blocks.get(crossingBlock_ID).trainOnBlock()) ? "1" : "0");
	
	//LV: Getting the Block ID of the the block before railway crossing, store train presence.
	logicPoints_RWC.add((theLine.blocks.get((crossingBlock_ID-1)).trainOnBlock()) ? "1" : "0");
	
	return (this._RailwayCrossing.doWeDropCrossing(logicPoints_RWC));
  }
  
 /** 
 * This method decides whether we need to raise the Railway Crossing Bar.
 * 
 * @param crossingBlock_ID The Id of the Block the Crossing is located on
 * @param theLine the Line this Controller and the Crossing is on
 * @return  Whether the Railway Crossing Bar was Raised
 */
  public boolean raiseCrossBar(int crossingBlock_ID, TrackModel theLine){
	  
	//Empty out old logic points
	this.logicPoints_RWC = new ArrayList<String>();
		
	//LV: Getting the Block ID of the railway crossing, store train presence.
	this.logicPoints_RWC.add((theLine.blocks.get(crossingBlock_ID).trainOnBlock()) ? "1" : "0");
	
	//LV: Getting the Block ID of the the block before railway crossing, store train presence.
	logicPoints_RWC.add((theLine.blocks.get((crossingBlock_ID-1)).trainOnBlock()) ? "1" : "0");
	
	return (this._RailwayCrossing.doWeRaiseCrossing(logicPoints_RWC));  
  }
  
 /** 
 * This method decides whether we can perform a Track Switch Change and 
 * implements it if we can.
 * 
 * @param theLine the Line this Controller and the Crossing is on
 * @param block_ID The Id of the Block the Switch is located on
 * @param openOrClose The State we want the Switch in. 1 for Open, 0 for Closed
 * @return  Whether the Track Switch Change was completed
 */
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
	  		closedState_ID = tempSwitch.block_id[1];
	  		//Storing the Block ID of the Switch Opened State
	  		openedState_ID = tempSwitch.block_id[2];
	  		
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
	  		closedState_ID = tempSwitch.block_id[1];
	  		//Storing the Block ID of the Switch Opened State
	  		openedState_ID = tempSwitch.block_id[2];
	  		
                        //System.out.println(openedState_ID+" - 1= "+(openedState_ID-1));
                        
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
  
 /** 
 * This method returns whether the CTC Command was Acknowledged.
 * 
 * @return  Whether the CTC Command was Acknowledged
 */
  public boolean passOrDeny(){
	//Simply return the acknowledgment
	  return (this.CTCAcknowledgment);
  }
  

 /** 
 * This method performs the redundancy check for Track Switch Changes.
 * 
 * @param theline the Line this Controller and the Crossing is on
 * @param openOrClose The State we want the Switch in. 1 for Open, 0 for Closed
 * @param block_ID The Id of the Block the Switch is located on
 * @return  Whether the Track Switch Change was completed
 */
  private boolean redundancy3(TrackModel theLine, int openOrClose, int block_ID){
		
		boolean val1=false, val2=false, val3=false;
		
		/*1.Get the Logic value from all three ValidateBooleanLogic Objects
		 *2.From the result decide if we are going to do the switch change or not.
		 *3.Set and send back CTCAcknowledgment
		 */
		
                System.out.println("Logic Points:\n"+this.logicPoints);
                
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
    
 /** 
 * This method returns the period of the clock signal the system is running on.
 * 
 * @return  The period of the clock signal the system is running on
 */  
  public double getDt()
    {
        return ctc.getDt();
    }
}
    
  
