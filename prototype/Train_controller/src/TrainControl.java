import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;

public class TrainControl 
{
	public double trainMass;
	public double maxAcceleration;
	public double maxPower;
	
	public double trackSpeedLimit=120;
	public double safeSpeedLimit;
	public double safeSpeed;
	public int safeLightState;
	public int safeDoorState;
	public double safeAuthority;
	public double safeTemp;
	public double safePower;
	public double currentSpeed;
	public double currentSpeedLimit;
	public double currentAuthority;
	public double currentTemp;
	public int currentLightStats;
	public int currentDoorStats;
	public double suggestedSpeed;
	public double suggestedSpeedLimit;
	public double suggestedAuthority;
	public double suggestedTemp;
	public double speedBackup, speedlimitBackup, tempBackup, authorityBackup;
	public int suggestedLightStats;
	public int suggestedDoorStats;
	public int brakeSignal;
	public int emergencyBrakeSignal;
	public TrainModel traincart;
	public TrainControllerGUI trainGUI;
	public SafeControl safeControl;
	double tempAuthority[]=new double[2];
	DecimalFormat df = new DecimalFormat("#.###");
	
	
	public TrainControl()
	{
		traincart=new TrainModel();
		trainGUI=new TrainControllerGUI();
		safeControl=new SafeControl(trackSpeedLimit);
		currentSpeed=traincart.speed;
		maxAcceleration=traincart.accelerationMax;
		maxPower=traincart.powerMax;
		trainMass=traincart.totalMass;
		
		speedBackup=trainGUI.setSpeedNumber;
		speedlimitBackup=trainGUI.setSpeedLimitNumber;
		tempBackup=trainGUI.setTempNumber;
		authorityBackup=trainGUI.setAuthorityNumber;
		
		int n=0;
		while(n<10000)
		{
			suggestedSpeed=trainGUI.getGUISpeed();
			suggestedSpeedLimit=trainGUI.getGUISpeedLimit();
			suggestedTemp=trainGUI.getGUITemp();
			suggestedLightStats=trainGUI.getGUILightSwitch();
			suggestedDoorStats=trainGUI.getGUIDoorSwitch();
			tempAuthority=trainGUI.getGUIAuthority();
			if(tempAuthority[1]==1)
			{
				suggestedAuthority=tempAuthority[0];
			}
			brakeSignal=trainGUI.getBrake();
			emergencyBrakeSignal=trainGUI.getEmergencyBrake();
			
			safeSpeed=safeControl.checkTrainSpeed(suggestedSpeed,suggestedSpeedLimit);
			safeSpeedLimit=safeControl.checkTrainSpeedLimit(suggestedSpeedLimit);
			safeLightState=safeControl.checkLightStatusChange(suggestedLightStats);
			safeDoorState=safeControl.checkDoorStatusChange(traincart.speed,suggestedDoorStats);
			safeTemp=safeControl.checkTrainTemp(suggestedTemp);
			safeAuthority=safeControl.checkAuthority(suggestedAuthority);
			safePower=computerPower(suggestedSpeed, traincart.speed,maxPower,maxAcceleration,trainMass,traincart.grade);
			putSafePower(safePower);
			putSafeSpeed(safeSpeed);
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
			traincart.updatePhyicsState();
			
			trainGUI.currentSpeedValue=traincart.speed;
			trainGUI.currentSpeedLimitValue=traincart.speedLimit;
			trainGUI.currentTempValue=traincart.temperature;
			trainGUI.currentAccelerationValue=traincart.acceleration;
			trainGUI.currentAuthorityValue=traincart.authority;
			
			trainGUI.setSpeedNumber=safeSpeed;
			trainGUI.setAuthorityNumber=safeAuthority;
			trainGUI.setSpeedLimitNumber=safeSpeedLimit;
			trainGUI.setTempNumber=safeTemp;
			
			if(trainGUI.setAuthorityChange==1)
			{
				trainGUI.setTempNumber=safeTemp;
				trainGUI.setAuthorityChange=0;
			}
			
			
			
			
			if(traincart.lightCondition==0)
				trainGUI.currentLightStats="Off";
			else
				trainGUI.currentLightStats="On";
			
			if(traincart.doorCondition==0)
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
			
			try{
				Thread.sleep(100);
			}
			catch(Exception xx)
			{}
			n++;
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
		traincart.power=inputPower;
	}
	
	void putSafeSpeed(double inputSpeed)
	{
		traincart.goalSpeed=inputSpeed;
	}
	
	void putSafeLight(int inputLight)
	{
		traincart.lightCondition=inputLight;
	}
	
	void putSafeDoor(int inputDoor)
	{
		traincart.doorCondition=inputDoor;
	}
	
	void putSafeTemp(double inputTemp)
	{
		traincart.setTemperature=inputTemp;
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
		double tempSpeed=1;;
		if(currentSpeed<=0)
			tempSpeed=1;
		else
			tempSpeed=currentSpeed;
		double tempPower_tractive1=tractiveCalculation(tempSpeed, trainMass, maxAcceleration, grade);
		double tempPower_tractive2=tractiveCalculation(tempSpeed, trainMass, maxAcceleration, grade);
		double tempPower_tractive3=tractiveCalculation(tempSpeed, trainMass, maxAcceleration, grade);
		double tempPower_tractive=safeControl.checkConsistency(tempPower_tractive1,tempPower_tractive2,tempPower_tractive3);
		double tempPower_newton1=newtonCalculation(tempSpeed, trainMass, maxAcceleration);
		double tempPower_newton2=newtonCalculation(tempSpeed, trainMass, maxAcceleration);
		double tempPower_newton3=newtonCalculation(tempSpeed, trainMass, maxAcceleration);
		double tempPower_newton=safeControl.checkConsistency(tempPower_newton1,tempPower_newton2,tempPower_newton3);
		double tempPower=safeControl.findMin(tempPower_tractive, tempPower_newton);
		if(tempPower>maxPower)
		{
			tempPower=maxPower;
		}
		if(currentSpeed>=suggestedSpeed)
		{
			tempPower=0;
		}
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
	
	public static void main(String[] args)
	{
		new TrainControl();
	}
	
	
}