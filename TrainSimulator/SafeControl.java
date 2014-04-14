package TrainSimulator;
import java.lang.Math.*;

public class SafeControl 
{
	double trainPhysicalSpeedLimit=120.0;
	double tempMax=90;
	double tempMin=70;
	double trackSpeedLimit;
	
	
	public SafeControl(double limit)
	{
		trackSpeedLimit=limit;
	}
	
	double findMin(double min1, double min2)
	{
		if(min1<min2)
			return min1;
		else 
			return min2;
	}
	
	double findMax(double max1, double max2)
	{
		if(max2<max1)
			return max1;
		else 
			return max2;
	}
	
	double checkTrainSpeedLimit(double suggetSpeedLimit)
	{
		if(suggetSpeedLimit>findMin(trainPhysicalSpeedLimit,trackSpeedLimit))
			suggetSpeedLimit=findMin(trainPhysicalSpeedLimit,trackSpeedLimit);
		if(suggetSpeedLimit<5.0)
			suggetSpeedLimit=5;
		return suggetSpeedLimit;
	}
	
	double checkTrainSpeed(double suggestedSpeed, double suggestedSpeedLimit)
	{
		if(suggestedSpeed>suggestedSpeedLimit)
			suggestedSpeed=suggestedSpeedLimit;
		if(suggestedSpeed<0)
			suggestedSpeed=0;
		return suggestedSpeed;
	}
	
	int checkDoorStatusChange(double currentSpeed, int doorStat)
	{
		if(currentSpeed>0)
		{
			doorStat=0;
			return doorStat;
		}
		else
		{
			return doorStat;
		}
	}
	
	int checkLightStatusChange(int lightStat)
	{
		return lightStat;
	}
	
	double checkTrainTemp(double suggestedTemp)
	{
		if(suggestedTemp>tempMax)
			suggestedTemp=tempMax;
		else if(suggestedTemp<tempMin)
			suggestedTemp=tempMin;
		return suggestedTemp;
	}
	
	int checkAuthority(int suggestedAuthority)
	{
		if(suggestedAuthority>6000)
			suggestedAuthority=6000;
		if(suggestedAuthority<0)
			suggestedAuthority=0;
		return suggestedAuthority;
	}
	
	double checkConsistency(double power1, double power2, double power3)
	{
		double truePower;
		if(power1==power2 && power2==power3)
		{
			truePower=power1;
			return truePower;
		}
		else
		{
			if(power1==power2 || power1==power3)
			{
				truePower=power1;
				return truePower;
			}
			else 
			{
				truePower=power2;
				return truePower;
			}
		}
	}

}
