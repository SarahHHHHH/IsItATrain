/*
 * Author: Derrick Ward
 * Project: Train System
 * Component: Track Controller
 * Purpose: This class will detect maintain information about which trains are at the Railway Crossing.
 * 			This object is instantiated with each Track Controller. If the controller is not monitoring
 * 			a railway crossing, this object will have info implicating so.  
 */
package TrainSimulator;
import java.util.ArrayList;

public class RailwayCrossing {
	//Fields
	private String logicToLoad="";
	boolean crossingDropped = false;
        
	//Logic Objects to Handle Railway Crossing
	private ArrayList<ValidateBooleanLogic> railwayCrossingPullDown = new ArrayList<ValidateBooleanLogic>();
	private ArrayList<ValidateBooleanLogic> railwayCrossingPullUp = new ArrayList<ValidateBooleanLogic>();
	
	//Constructor -> Done
	public RailwayCrossing(TrackModel theLine){
		
	/*Create the logicTable for the the "ValidateBooleanLogic" Class:
	* DEALING WITH RAILWAY CROSSING: -> Dropping the Crossing Down
	* 1.Get the Block ID of the Railway Crossing. -> Needs to be False 
	* 2.Get the Block ID of the Block ID Before the RailWay Crossing. -> Needs to be True
		* TRUTH TABLE:
			* 1_BlockID | 2_BLockID | Result (to Drop Crossing or Not)
			* 	TRUE		*		   FALSE
			* 	FALSE		TRUE	   TRUE
			* 	FALSE		FALSE	   FALSE
	*/
	//stores logic to be loaded
	this.logicToLoad = "(1&(!))";
	
	//Instantiate PLC Program(3 of them), with the needed logic -> Done
	for (int counter = 0; counter < 3; counter++)
			this.railwayCrossingPullDown.add(new ValidateBooleanLogic(this.logicToLoad));
	
	/*Create the logicTable for the the "ValidateBooleanLogic" Class:
	* DEALING WITH RAILWAY CROSSING: -> Raising the Crossing Down
	* 1.Get the Block ID of the Railway Crossing. -> Needs to be False 
	* 2.Get the Block ID of the Block ID Before the RailWay Crossing. -> Needs to be False
		* TRUTH TABLE:
			* 1_BlockID | 2_BLockID | Result (to Drop Crossing or Not)
			* 	TRUE		*		   FALSE
			* 	*			TRUE	   FALSE
			* 	FALSE		FALSE	   TRUE
	*/
	//stores logic to be loaded
	this.logicToLoad = "(!&(!))";
	
	//Instantiate PLC Program(3 of them), with the needed logic -> Done
	for (int counter = 0; counter < 3; counter++)
			this.railwayCrossingPullUp.add(new ValidateBooleanLogic(this.logicToLoad));
	}
	
	//Decides whether we drop the railway crossing bar or not -> Done
	public boolean doWeDropCrossing(ArrayList<String> logicPoints){
		boolean val1, val2, val3;
		
		val1 = this.railwayCrossingPullDown.get(0).evaluateLogic(logicPoints);
  		val2 = this.railwayCrossingPullDown.get(1).evaluateLogic(logicPoints);
  		val3 = this.railwayCrossingPullDown.get(2).evaluateLogic(logicPoints);
		

		//Redundancy Check!
		if (val1 == val2 && val2 == val3){
                    this.crossingDropped = true;
                    return true;
		}
		else{
                    this.crossingDropped = false;
                    return false;
		}
	}
	
	//Decides whether we raise the railway crossing bar or not -> Done
	public boolean doWeRaiseCrossing(ArrayList<String> logicPoints){
		boolean val1, val2, val3;
		
		val1 = this.railwayCrossingPullUp.get(0).evaluateLogic(logicPoints);
  		val2 = this.railwayCrossingPullUp.get(1).evaluateLogic(logicPoints);
  		val3 = this.railwayCrossingPullUp.get(2).evaluateLogic(logicPoints);
		

		//Redundancy Check!
		if (val1 == val2 && val2 == val3){
                    this.crossingDropped = false;
                    return true;
		}
		else{
                    this.crossingDropped = true;
                    return false;
		}
	}

}
