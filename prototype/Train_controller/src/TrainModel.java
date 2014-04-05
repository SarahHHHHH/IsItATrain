
import java.lang.Math;

public class TrainModel
{
	private int trainID;
	public double speed; //the current speed
	public double goalSpeed;
	public double speedLimit; //physical limit
	public double power;  //kw
	public double friction_acceleration=10;
	public double acceleration;
	public double accelerationMax=30; 
	public double setTemperature;
	public double temperature;
	public double totalMass=65000; //t
	public double powerMax=650000;
	public double grade = 0;
	public double authority=5000;
	public int doorCondition;
	public int lightCondition;
	public int brake;
	public int emergencyBrake;
	public double output_authority;
	
	public float trackFriction;
	
	public int currentBlockNumber;
	
	public TrainModel()
	{
		this.authority=5000;
		this.speed = 30;
		this.temperature = 52.0;
		this.doorCondition = 0;
		this.lightCondition = 0;
		this.trainID = 0;
	}

	public float getTrackFriction()
	{
		return trackFriction;
	}

	public void setTrackFriction(float trackFriction)
	{
		this.trackFriction = trackFriction;
	}


	public void setAccelerationMax(float accelerationMax)
	{
		this.accelerationMax = accelerationMax;
	}
	
	public double getSpeedMax()
	{
		return speedLimit;
	}

	public double getAcceleration()
	{
		return acceleration;
	}

	public void setAcceleration(float acceleration)
	{
		this.acceleration = acceleration;
	}


	public int getTrainID()
	{
		return trainID;
	}

	public void setTrainID(int trainID)
	{
		this.trainID = trainID;
	}

	public double getSpeed()
	{
		return speed;
	}

	public double getTemperature()
	{
		return temperature;
	}

	public void setTemperature(float temperature)
	{
		this.temperature = temperature;
	}

	public int isDoorCondition()
	{
		return doorCondition;
	}


	public int getLightCondition()
	{
		return lightCondition;
	}

	/*
	 * @parameters sampleTimeInterval in millseconds
	 */
	public void updatePhyicsState()
	{
		float force = (float) (power / (speed*1.4667));
		//float gradient_deceleration = (float) (gg/Math.sqrt(gg*gg + 1)* 9.8);
		if(temperature<setTemperature)
			temperature+=0.01;
		else if(temperature>setTemperature)
			temperature-=0.01;
		acceleration  = force/totalMass ;
		
		if(speed==0)
		{
			acceleration=0.5;
		}
		
		if(this.speed>this.goalSpeed)
		{
			acceleration=-0.1;
		}
		
		if (brake==1)
		{
			acceleration = -0.5;
		}
		if(emergencyBrake==1 || authority<5)
		{
			acceleration = -2.7;
		}
		
		
		if(speed>=0)
		{
			if(speed + acceleration /1.4667<0)
			{
				speed=0;
				//System.out.println(speed+" "+acceleration);
			}
			else
			{
				speed += acceleration/1.4667;
				//System.out.println(speed+" "+acceleration);
			}
		}
		if(authority>0)
		{
			authority=authority-speed/360;
		}
	}

}
