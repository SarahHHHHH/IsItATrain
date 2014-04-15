package TrainSimulator;

import java.text.DecimalFormat;
public class TrainController 
{
	public int controllerID;
	
	public double trainMass;
	public double maxAcceleration;
	public double maxAccelerationRightUnit;
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
	public double safePower;
	public double currentSpeed;
	public double currentSpeedRightUnit;
	public double currentSpeedLimit;
	public double currentSpeedLimitRightUnit;
	public double currentAuthority;
	public double currentTemp;
	public int currentLightStats;
	public int currentDoorStats;
	public double suggestedSpeed;
	public double suggestedSpeedRightUnit;
	public double suggestedSpeedLimit;
	public int suggestedAuthority;
	public double suggestedTemp;
	public double speedBackup, speedlimitBackup, tempBackup, authorityBackup;
	public int suggestedLightStats;
	public int suggestedDoorStats;
	public int brakeSignal;
	public int emergencyBrakeSignal;
	public TrainModel traincart;
	public TrainControllerGUI trainGUI;
	public SafeControl safeControl;
	int tempAuthority[]=new int[2];
	public int beaconValue=0;
	DecimalFormat df = new DecimalFormat("#.###");
	
	
	
	public TrainController(TrackModel line, int id, int trainStart)
	{
		this.controllerID=id;
		traincart=new TrainModel(line, id);
                traincart.setBlockID(trainStart);
		trainGUI=new TrainControllerGUI();
		safeControl=new SafeControl(trackSpeedLimit);	
	}

	void executeGUI()
	{
		
		System.out.println(brakeSignal);
           
		currentSpeed=traincart.getSpeed();
		maxAcceleration=traincart.getMaxAcceleration();
		maxPower=traincart.getMaxPower();
		trainMass=traincart.getTotalMass();
		
		currentSpeedRightUnit=0.44704*currentSpeed;
		maxAccelerationRightUnit=0.44704*maxAcceleration;
		suggestedSpeedRightUnit=0.44704*suggestedSpeed;
		
		speedBackup=trainGUI.setSpeedNumber;
		speedlimitBackup=trainGUI.setSpeedLimitNumber;
		tempBackup=trainGUI.setTempNumber;
		authorityBackup=trainGUI.setAuthorityNumber;
		suggestedSpeed=traincart.getDesiredSpeed();
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
		safePower=computerPower(suggestedSpeedRightUnit, currentSpeedRightUnit,maxPower,maxAccelerationRightUnit,trainMass,traincart.grade);
		putSafePower(safePower);
		//putSafeSpeed(safeSpeed);
		putSafeLight(safeLightState);
		putSafeDoor(safeDoorState);
		putSafeTemp(safeTemp);
		if(trainGUI.setAuthorityChange==1)
		{
			putSafeAuthority(safeAuthority);
		}
		putSafeSpeedLimit(safeSpeedLimit);
		putBrake(brakeSignal);
		putEmergencyBrake(emergencyBrakeSignal);
		traincart.updateTrain(0.1);
		
		System.out.println(currentSpeed);
		trainGUI.currentSpeedValue=currentSpeed;
		trainGUI.currentSpeedLimitValue=traincart.speedLimit;
		trainGUI.currentTempValue=traincart.temperature;
		trainGUI.currentAccelerationValue=traincart.acceleration;
		trainGUI.currentAuthorityValue=traincart.getRemainingAuthority();
		
		trainGUI.setSpeedNumber=safeSpeed;
		trainGUI.setAuthorityNumber=safeAuthority;
		trainGUI.setSpeedLimitNumber=safeSpeedLimit;
		trainGUI.setTempNumber=safeTemp;
		
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
			executeBeacon();
		}
		else
		{
			if(traincart.speed==0)
			{
				trainGUI.currentDoorStats="open";
				//boardPassengers();
			}
			String temp=traincart.getBeacon();
			if(temp.equals(null))
			{
				beaconValue=0;
			}
		}
		
		try{
			Thread.sleep(100);
		}
		catch(Exception xx)
		{}
	}
		
	
	
	
	void executeBeacon()
	{
		String stationName="None";
		if(stationName.equals("None"))
		{}
		else
		{
			trainGUI.annoucement.append("Approaching "+stationName+"\n");	
			trainGUI.brakeSignal=1;
			trainGUI.setSpeedNumber=0;
			beaconValue=1;
		}
	}
	
	void putBrake(int brakeSignal)
	{
		traincart.brake=brakeSignal;
	}
	
	void putEmergencyBrake(int emergencyBrakeSignal)
	{
		traincart.emergencyBrake=emergencyBrakeSignal;
	}
	
	void putSafePower(double inputPower)
	{
		traincart.setPower(inputPower);
	}
	
	void putSafeLight(int inputLight)
	{
		traincart.setDoorStatus(inputLight);
	}
	
	void putSafeDoor(int inputDoor)
	{
		traincart.setDoorStatus(inputDoor);
	}
	
	void putSafeTemp(double inputTemp)
	{
		traincart.setTemperature(inputTemp);
	}
	
	void putSafeSpeedLimit(double inputSpeedLimit)
	{
		traincart.speedLimit=inputSpeedLimit;
	}
	
	void putSafeAuthority(double inputAuthority)
	{
		traincart.authority=inputAuthority;
	}
	
	
	double computerPower(double suggestedSpeed, double currentSpeed, double maxPower, double maxAcceleration, double trainMass, double grade)
	{
		double tempSpeed=1;
		if(currentSpeed<=0)
			tempSpeed=1;
		else
			tempSpeed=currentSpeed;
		//double tempPower_tractive1=tractiveCalculation(tempSpeed, trainMass, maxAcceleration, grade);
		//double tempPower_tractive2=tractiveCalculation(tempSpeed, trainMass, maxAcceleration, grade);
		//double tempPower_tractive3=tractiveCalculation(tempSpeed, trainMass, maxAcceleration, grade);
		//double tempPower_tractive=safeControl.checkConsistency(tempPower_tractive1,tempPower_tractive2,tempPower_tractive3);
		double tempPower_newton1=newtonCalculation(tempSpeed, trainMass, maxAcceleration);
		double tempPower_newton2=newtonCalculation(tempSpeed, trainMass, maxAcceleration);
		double tempPower_newton3=newtonCalculation(tempSpeed, trainMass, maxAcceleration);
		double tempPower_newton=safeControl.checkConsistency(tempPower_newton1,tempPower_newton2,tempPower_newton3);
		double tempPower=tempPower_newton;
		//double tempPower=safeControl.findMin(tempPower_tractive, tempPower_newton);
		if(tempPower>maxPower)
		{
			tempPower=maxPower;
		}
		if(currentSpeed>=suggestedSpeed|| brakeSignal==1 || emergencyBrakeSignal==1)
		{
			tempPower=0;
		}
		//System.out.println(tempPower);
		return tempPower;
	}
	
	double tractiveCalculation(double tempSpeed, double trainMass, double acceleration, double grade)
	{
		double power=(0.35*trainMass/1000+100*grade)*tempSpeed;
		return power;
	}
	
	double newtonCalculation(double tempSpeed, double trainMass, double acceleration)
	{
		double power=trainMass*tempSpeed*acceleration;
		return power;
	}
	
	/*public static void main(String[] args)
	{
		int lineColor=1;
		new TrainControl(lineColor,controllerID);
	}*/	
}
