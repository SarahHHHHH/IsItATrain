/*File name: TrainController.java
 * Author: Hongyao SHi
 * Group: Team Shwoz
 * Created Date: 4/11/2014
 */


package TrainSimulator;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*This class is the main Class for the train Controller
 * it will do all the calculation, specification and communication
 * with the TrainControllerGUI and the TrainModel
 */

/**
 * @author Hongyao Shi
 */

public class TrainController 
{   
    public int controllerID;
	
	public double trainMass;
	public double maxAcceleration;
	public double maxAccelerationRightUnit;	//change the Acceleration units into m/s^2
	public double maxPower;
	
	public double trackSpeedLimit=120;
	
	public double trackDesiredSpeed=0;
	public double trackDesiredAuthority=0;
	public double trackDesiredSpeedLimit=0;
	
	public double safeSpeedLimit;
	public double safeSpeed;
	public int safeLightState;
	public int safeDoorState;
	public int safeAuthority;
	public double safeTemp;
	public double safePower, safePower1, safePower2, safePower3;
	public double currentSpeed;
	public double currentSpeedRightUnit;	//Change the current speed units into m/s
	public double currentSpeedLimit;
	public double currentSpeedLimitRightUnit;	//Change the speed limit units into m/s
	public double currentAuthority;
	public double currentTemp;
	public int currentLightStats;
	public int currentDoorStats;
	public double suggestedSpeed;
	public double suggestedSpeedRightUnit;		//Change the suggestedSpeed units into m/s
	public double suggestedSpeedLimit;
	public int suggestedAuthority;
	public double suggestedTemp;
	public double speedBackup, speedlimitBackup, tempBackup, authorityBackup; //back up value for all the variables
	public int suggestedLightStats;
	public int suggestedDoorStats;
	public int brakeSignal;
	public int emergencyBrakeSignal;
	public int engineFailure;
	public int signalFailure;
	public int brakeFailure;
	public TrainModel traincart;
	public TrainControllerGUI trainGUI;
	public SafeControl safeControl;
	public TrainModelFailGUI failGUI;
	int tempAuthority[]=new int[2];		//temperary spot to store authority from the trainModel
	public int beaconValue=0;
	
	double vErrorCurrent=0;				//current velocity error
	double vErrorPrevious=0;			//velocity error from the last time step
	double uCurrent=0;					//current potential
	double uPrevious=0;					//potential from the previous time step
	double timeStep=0.1;
	public static double kp=1000;		//Proportional gain
	public static double ki=20;			//Integral gain
	public ArrayList<String> stoppedStation;     //list of the station to stop at
	
	//DecimalFormat df = new DecimalFormat("#.###");
	
	
	
	public TrainController(TrackModel line, int id, int trainStart)
	{
		this.controllerID=id;
		traincart=new TrainModel(line, id);
		failGUI=new TrainModelFailGUI();
		traincart.setBlockID(trainStart);
		trainGUI=new TrainControllerGUI();
		trainGUI.trainID=id;
		safeControl=new SafeControl(trackSpeedLimit);	
	}
	
	/**
	 *This function is created so that when the clock is ticking, 
	 * the commands in this function can be called in order
	 */
	
	void executeGUI()
	{
		int[] failMatrix=new int[3];
		failMatrix=failGUI.getFailure();
		engineFailure=failMatrix[0];
		signalFailure=failMatrix[1];
		brakeFailure=failMatrix[2];
           
		currentSpeed=traincart.getSpeed();
		maxAcceleration=traincart.getMaxAcceleration();
		maxPower=traincart.getMaxPower();
		trainMass=traincart.getTotalMass();
		currentAuthority=traincart.getRemainingAuthority();
		
		currentSpeedRightUnit=2.23694*currentSpeed;
		maxAccelerationRightUnit=2.23694*maxAcceleration;
		suggestedSpeedRightUnit=2.23694*suggestedSpeed;
		//the above 3 lines convert the number into international unit for calculation
		
		speedBackup=trainGUI.setSpeedNumber;
		speedlimitBackup=trainGUI.setSpeedLimitNumber;
		tempBackup=trainGUI.setTempNumber;
		authorityBackup=trainGUI.setAuthorityNumber;
		//the above 4 lines extract the values from the trainGUI for back up so that it will
		//be compared to current set value to avoid repeating getting the same value and not
		//updating
		suggestedSpeed=traincart.getDesiredSpeed();
		
		if(currentAuthority==0||engineFailure==1||signalFailure==1)
		{
			trainGUI.emergencyBrakeSignal=1;
		}
		
		if(suggestedSpeed==trackDesiredSpeed)
		{
			suggestedSpeed=trainGUI.getGUISpeed();
		}
		else
		{
			trackDesiredSpeed=suggestedSpeed;
		}
			
		tempAuthority=traincart.getAuthority();
		if(tempAuthority[0]==trackDesiredAuthority)
		{
			tempAuthority=trainGUI.getGUIAuthority();
		}
		else
		{
			trackDesiredAuthority=tempAuthority[0];
		}
		if(tempAuthority[1]==1)
		{
			suggestedAuthority=tempAuthority[0];
		}
			
		suggestedSpeedLimit=traincart.getSpeedLimit();
		if(suggestedSpeedLimit==trackDesiredSpeedLimit || trainGUI.getGUISpeedLimit()<suggestedSpeedLimit)
		{
			suggestedSpeedLimit=trainGUI.getGUISpeedLimit();
		}
		else
		{
			trackDesiredSpeedLimit=suggestedSpeedLimit;
		}
		
		suggestedTemp=trainGUI.getGUITemp();
		suggestedLightStats=trainGUI.getGUILightSwitch();
		suggestedDoorStats=trainGUI.getGUIDoorSwitch();
		brakeSignal=trainGUI.getBrake();
		emergencyBrakeSignal=trainGUI.getEmergencyBrake();
	
		
		
		safeSpeed=safeControl.checkTrainSpeed(suggestedSpeed,suggestedSpeedLimit);
		safeSpeedLimit=safeControl.checkTrainSpeedLimit(suggestedSpeedLimit);
		safeLightState=safeControl.checkLightStatusChange(suggestedLightStats);
		safeDoorState=safeControl.checkDoorStatusChange(traincart.speed,suggestedDoorStats);
		safeTemp=safeControl.checkTrainTemp(suggestedTemp);
		safeAuthority=safeControl.checkAuthority(suggestedAuthority);
		//The above 6 lines check the safe critic of the parameters which will be passed to trainModel
		
		safePower1=computerPower(suggestedSpeedRightUnit, currentSpeedRightUnit,maxPower);
		safePower2=computerPower(suggestedSpeedRightUnit, currentSpeedRightUnit,maxPower);
		safePower3=computerPower(suggestedSpeedRightUnit, currentSpeedRightUnit,maxPower);
		safePower=safeControl.checkConsistency(safePower1,safePower2,safePower3);
		if(brakeFailure==1)
		{
			safePower=0;
		}
		updatePowerStep();
		//The above 4 lines calculate the power output to the trainModel 3 times and then check for 
		//consistency to for safe critic
		
		putSafePower(safePower);
		putSafeLight(safeLightState);
		putSafeDoor(safeDoorState);
		putSafeTemp(safeTemp);
		//output the parameters to the trainModle Class
		
		if(trainGUI.setAuthorityChange==1)
		{
			putSafeAuthority(safeAuthority);
			trainGUI.emergencyBrakeSignal=0;
			trainGUI.brakeSignal=0;
			//trainGUI.emergencyBrakeSignal=1;
			
		}
		//The above 3 lines check if the authority is a new input instead 
		//an old input to avoid keep refreshing the authority
		putSafeSpeedLimit(safeSpeedLimit);
		putBrake(brakeSignal);
		putEmergencyBrake(emergencyBrakeSignal);
		traincart.updateTrain(0.1);
		
		trainGUI.currentSpeedValue=currentSpeed;
		trainGUI.currentSpeedLimitValue=traincart.speedLimit;
		trainGUI.currentTempValue=traincart.temperature;
		trainGUI.currentPowerValue=safePower;
		trainGUI.currentAuthorityValue=traincart.getRemainingAuthority();
		
		trainGUI.setSpeedNumber=safeSpeed;
		trainGUI.setAuthorityNumber=safeAuthority;
		trainGUI.setSpeedLimitNumber=safeSpeedLimit;
		trainGUI.setTempNumber=safeTemp;
		//The above 9 lines update the values in the trainGUI class
		
		if(trainGUI.setAuthorityChange==1)
		{
			trainGUI.setTempNumber=safeTemp;
			trainGUI.setAuthorityChange=0;
		}
		
		
		
		
		if(traincart.lightStatus==0)
			trainGUI.currentLightStats="Off";
		else
			trainGUI.currentLightStats="On";
		
		if(traincart.doorStatus==0)
			trainGUI.currentDoorStats="Closed";
		else
			trainGUI.currentDoorStats="Open";
		
		trainGUI.updateDisplay();
		if(safeSpeed!=speedBackup || safeSpeedLimit!=speedlimitBackup || safeTemp!=tempBackup || safeAuthority!=authorityBackup)
		{
			trainGUI.updateSetDisplay();
			speedBackup=safeSpeed;
			speedlimitBackup=safeSpeedLimit;
			tempBackup=safeTemp;
			authorityBackup=safeAuthority;
		}
		if(beaconValue==0)
		{
			//executeBeacon();
		}
		else
		{
			if(traincart.speed==0)
			{
				trainGUI.currentDoorStats="open";
				//traincart.boardPassengers();
			}
			String temp=traincart.getBeacon();
			if(temp.equals("none"))
			{
				beaconValue=0;
			}
		}
	}
	
	
	/**
	 *The following function receives beacon signals from the 
	 * train model, and based on the content of the signal, train controller
	 * will decide whether to stop the train or not
	 */	
	void executeBeacon()
	{
		String stationName=traincart.getBeacon();
		if(stationName.equals("None"))
		{}
		else
		{
			for(int i=0; i<stoppedStation.size();i++)
			{
				if(stationName.equals(stoppedStation.get(i)))
				{
					trainGUI.annoucement.append("Approaching "+stationName+"\n");	
					trainGUI.emergencyBrakeSignal=1;
					trainGUI.setSpeedNumber=0;
					beaconValue=1;
				}
			}
		}
	}
	
	/**
	 *the folloing function put the brake signal from the train controller
	 * into the train model
	 *
	 * @param  brakeSignal an int given by the train controller to assign the brake in the train model
	 */
	
	void putBrake(int brakeSignal)
	{
		traincart.brake=brakeSignal;
	}
	
	/**
	 *The following function assigns the train models emergency brake
	 *
	 * @param  emergencyBrakeSignal an int given by the train controller to assign the emergency brake in the train model
	 */
	
	void putEmergencyBrake(int emergencyBrakeSignal)
	{
		traincart.emergencyBrake=emergencyBrakeSignal;
	}
	
	/**
	 *The following function assigns the train models power
	 *
	 * @param  inputPower an double given by the train controller to assign the power in the train model
	 */
	
	void putSafePower(double inputPower)
	{
		traincart.setPower(inputPower);
	}
	
	/**
	 *The following function assigns the train models light status
	 *
	 * @param  inputLight an double given by the train controller to assign the light status in the train model
	 */

	void putSafeLight(int inputLight)
	{
		traincart.setDoorStatus(inputLight);
	}
	
	/**
	 *The following function assigns the train models door status
	 *
	 * @param  inputDoor an double given by the train controller to assign the door status in the train model
	 */

	void putSafeDoor(int inputDoor)
	{
		traincart.setDoorStatus(inputDoor);
	}
	
	/**
	 *The following function assigns the train models temperature
	 *
	 * @param  inputTemp an double given by the train controller to assign the safe temperature in the train model
	 */
	
	void putSafeTemp(double inputTemp)
	{
		traincart.setTemperature(inputTemp);
	}
	
	/**
	 *The following function assigns the train models speedlimit
	 *
	 * @param  inputSpeedLimit an double given by the train controller to assign the safe speed limit in the train model
	 */
	
	void putSafeSpeedLimit(double inputSpeedLimit)
	{
		traincart.speedLimit=inputSpeedLimit;
	}
	
	/**
	 *The following function assigns the train models authority
	 *
	 * @param  inputAuthority an double given by the train controller or track to assign the safe authority in the train model
	 */
	
	void putSafeAuthority(double inputAuthority)
	{
		traincart.remainingAuthority=inputAuthority;
	}
	
	/**
	 *The following function computes the output power using the stored velocity error and the u
	 *
	 * @param  suggestedSpeed a double value that represents the current desired speed of the train in m/s
	 * @param  currentSpeed a double value that represents the current speed of the train in m/s
	 * @param  maxPower a double value that represents that maxPower of the train in W
	 * @return the power input to the train 
	 */
	double computerPower(double suggestedSpeed, double currentSpeed, double maxPower)
	{
		vErrorCurrent=suggestedSpeed-currentSpeed;
		uCurrent=uPrevious+(timeStep/2)*(vErrorCurrent+vErrorPrevious);
		double outputPower=kp*vErrorCurrent+ki*uCurrent;
		if(outputPower<maxPower)
		{
			uCurrent=uPrevious+(timeStep/2)*(vErrorCurrent+vErrorPrevious);
		}
		else
		{
			uCurrent=uPrevious;
		}
		if(traincart.brake==1||traincart.emergencyBrake==1||suggestedSpeed<currentSpeed)
		{
			outputPower=0;
		}
		return outputPower;
	}
	
	/**
	 *The following function updates the previous velocity error and previous u
	 *
	 */
	
	void updatePowerStep()
	{
		vErrorPrevious=vErrorCurrent;
		uPrevious=uCurrent;
	}
	
	/**
	 *The following function minimizes the current GUI
	 */
	
	void minimalize()
	{
		this.trainGUI.minimalize();
		this.failGUI.minimalize();
	}
	
	/**
	 *The following function normalize the current GUI
	 */
	
	void normalize()
	{
		this.trainGUI.normalize();
		this.failGUI.normalize();
	}
	
	/**
	 *The following function Assign the stations the train will stop at
	 *
	 *@param stationList A arrayList of Strings with stations in it
	 */
	
	void setStationList(ArrayList<String> stationList)
	{
		this.stoppedStation=stationList; 
        
	}
	
	/**
	 *The following function change the train ID to a string
	 *
	 *@return it returns the train id in string form
	 */
	
    public String toString()
    {
        return("Train " + Integer.toString(traincart.id));
    }
	/*The test main function for the sub module*/
	/*public static void main(String[] args)
	{
		int lineColor=1;
		new TrainControl(lineColor,controllerID);
	}*/
        
        //public double dt = traincart.getDt();
       
}
