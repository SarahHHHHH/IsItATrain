/*File name: SafeControl.java
 * Author: Hongyao Shi
 * Group: Team Shwoz
 * Created Date: 4/11/2014
 */

package TrainSimulator;
import java.lang.Math.*;


 /*This class the safe crictical validataion class which validates the input
  * parameters fits the safe critic or not
  */

/**
 * @author Hongyao Shi
 */

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
	
	/**
	 *The following function finds the min of the 2 inputs
	 *
	 * @param  min1 an double as input for comparison
	 * @param  min2 an double as input for comparison
	 * 
	 * @return the min between the 2 inputs
	 */
	
	double findMin(double min1, double min2)
	{
		if(min1<min2 && min1!=0)
			return min1;
		else 
			return min2;
	}
	
	/**
	 *The following function finds the max of the 2 inputs
	 *
	 * @param  max1 an double as input for comparison
	 * @param  max2 an double as input for comparison
	 * 
	 * @return the max between the 2 inputs
	 */
	
	double findMax(double max1, double max2)
	{
		if(max2<max1)
			return max1;
		else 
			return max2;
	}
	
	/**
	 *The following function validates the speed limit set is safe
	 *
	 * @param  suggetSpeedLimit an double that as the suggested speed limit
	 * 
	 * @return the safe speed limit that fits the safe critiria
	 */
	
	double checkTrainSpeedLimit(double suggetSpeedLimit)
	{
		if(suggetSpeedLimit>findMin(trainPhysicalSpeedLimit,trackSpeedLimit))
			suggetSpeedLimit=findMin(trainPhysicalSpeedLimit,trackSpeedLimit);
		if(suggetSpeedLimit<5.0)
			suggetSpeedLimit=5;
		return suggetSpeedLimit;
	}
	
	/**
	 *The following function validates the desired speed set is safe
	 *
	 * @param  suggetSpeed an double that as the suggested desired speed
	 * @param  suggetSpeedLimit an double that as the speed limit
	 * 
	 * @return the safe desired speed that fits the safe critiria
	 */
	
	double checkTrainSpeed(double suggestedSpeed, double suggestedSpeedLimit)
	{
		if(suggestedSpeed>suggestedSpeedLimit)
			suggestedSpeed=suggestedSpeedLimit;
		if(suggestedSpeed<0)
			suggestedSpeed=0;
		return suggestedSpeed;
	}
	
	/**
	 *The following function validates the desired speed set is safe
	 *
	 * @param  currentSpeed an double shows the current speed of the train
	 * @param  doorStat an int shows the suggested door stats 
	 * 
	 * @return the safe door stats 
	 */
	
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
	
	/**
	 *The following function validates the desired speed set is safe
	 *
	 * @param  doorStat an int shows the suggested light stats 
	 * 
	 * @return the safe light stats 
	 */
	
	int checkLightStatusChange(int lightStat)
	{
		return lightStat;
	}
	
	/**
	 *The following function validates the desired temperature set is safe
	 *
	 * @param  suggestedTemp an double shows the desired temperature of the train 
	 * 
	 * @return the safe desired temperature
	 */
	
	double checkTrainTemp(double suggestedTemp)
	{
		if(suggestedTemp>tempMax)
			suggestedTemp=tempMax;
		else if(suggestedTemp<tempMin)
			suggestedTemp=tempMin;
		return suggestedTemp;
	}
	
	/**
	 *The following function validates the desired authority set is safe
	 *
	 * @param  suggestedAuthority an double shows the desired authority of the train 
	 * 
	 * @return the safe desired authority
	 */
	
	int checkAuthority(int suggestedAuthority)
	{
		if(suggestedAuthority>6000)
			suggestedAuthority=6000;
		if(suggestedAuthority<0)
			suggestedAuthority=0;
		return suggestedAuthority;
	}
	
	/**
	 *The following function check if the 3 power calculatation results are consistent
	 *
	 * @param  power1 an double to check consistency
	 * @param  power2 an double to check consistency
	 * @param  power3 an double to check consistency
	 * 
	 * @return the safe output power
	 */
	
	double checkConsistency(double power1, double power2, double power3)
	{
		double truePower;
		if(power1==power2 && power2==power3)
		{
			truePower=power1;
			return truePower;
		}
		else if(power1==power2)
		{
			truePower=power1;
			return truePower;
		}
		else if(power2==power3)
		{
			truePower=power2;
			return truePower;
		}
		else
		{
			truePower=0.0;
			return truePower;
		}
	}
	

}
