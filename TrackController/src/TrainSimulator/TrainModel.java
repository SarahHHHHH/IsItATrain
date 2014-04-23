/*
 filename: TrainModelCentral
 author: Sarah Higbee
 group: SHWOZ
 date: 26/03/14
 */

package TrainSimulator;

import java.lang.Math;

public class TrainModel
{
    //1 = open
    public int doorStatus;
    //fahrenheit 
    public double temperature;
    public double inputTemperature;
    //1 = on
    public int lightStatus;
    //mph 
    public double speed;
    public double desiredSpeed;
    //blocks
    public double authority;
    //watts
    public double power;
    //mph
    public double speedLimit;
    //mphps
    public double acceleration;
    //1 = braking
    public int brake;
    public int emergencyBrake;
    //identifies train
    public int id;
    //contains next stop name when in range of beacon, null otherwise
    //set this at interval.
    public String beacon;
    //grade measured in degrees from the horizontal
    public double grade;
    //blocks
    public double remainingAuthority;
    public double oldAuthority;
    public int finishedAuthority;
    //serial numbers
    public int prevBlockID;
    public int blockID;
    //miles
    public double blockLength;
    //1 = red, 2 = green
    public int lineNo;
    //1 = underground
    public boolean underground;
    
    public Internal internal = new Internal();
    public Failure failure = new Failure();
    public Dimensions dimensions = new Dimensions();
    TrackModel myLine;
    
    //Ask Yanis whether he wants to keep this system.
    //Ask how he expects me to get line number.
    public TrainModel(TrackModel line, int id)
    {
        this.doorStatus = 0;
        this.temperature = 74;
        this.lightStatus = 0;
        this.speed = 0;
        this.power = 0;
        this.acceleration = 0;
        this.brake = 0;
        this.emergencyBrake = 0;
        this.id = id;
        this.beacon = null;
        this.lineNo = 1;
        this.prevBlockID = 0;
        if (lineNo == 1)
        {
        	myLine=line;
            this.blockID = myLine.getStartingBlock();
            this.desiredSpeed = myLine.getSpeed(blockID, id);
            this.grade = myLine.getGrade(blockID);
            this.speedLimit = myLine.getSpeedLimit(blockID);
            this.blockLength = myLine.getBlockLength(blockID);
            this.authority = myLine.getAuthority(blockID, id);
            this.beacon=myLine.getBeacon(blockID);
            
            //ask Yanis what his underground var and getter is.
            //this.underground = red.get
        }
        /*
        else if (lineNo == 2)
        {
            blockID = green.getStartingBlock();
            desiredSpeed = green.getSpeed(blockID, id);
            grade = green.getGrade(blockID);
            speedLimit = green.getSpeedLimit(blockID);
            blockLength = green.getBlockLength(blockID);
            authority = green.getAuthority(blockID, id);
        }
        */
        this.remainingAuthority = authority;
        this.oldAuthority = authority;
        this.finishedAuthority = 0;
    }
    
    public void setDoorStatus(int dor)
    {
        this.doorStatus = dor;
    }
    
    public void setTemperature(double temper)
    {
        this.inputTemperature = temper;
    }
    
    public void setLightStatus(int light)
    {
        this.lightStatus = light;
    }
    
    public void setPower(double pow)
    {
        this.power = pow;
    }
    
    public void setSpeedLimit(double sped)
    {
        this.speedLimit = sped;
    }
    
    public void setBrake(int brak)
    {
        this.brake = brak;
    }
    
    public void setEmergencyBrake(int brak)
    {
        this.emergencyBrake = brak;
    }
    
    public void setBeacon(String mavis)
    {
        this.beacon = mavis;
    }
    
    public int getDoorStatus()
    {
        return doorStatus;
    }
    
    public double getTemperature()
    {
        return temperature;
    }
    
    public int getLightStatus()
    {
        return lightStatus;
    }
    
    public double getSpeed()
    {
        return speed;
    }
    
    public int[] getAuthority()
    {
        int[] auth = {(int)authority,1};
        return auth;
    }
    
    public double getSpeedLimit()
    {
        return speedLimit;
    }
    
    public double getAcceleration()
    {
        return acceleration;
    }
    
    public double getPower()
    {
        return power;
    }
    
    public int getBrake()
    {
        return brake;
    }
    
    public int getEmergencyBrake()
    {
        return emergencyBrake;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getBeacon()
    {
        return beacon;
    }
    
    public void setBlockID(int blkid)
    {
        blockID = blkid;
    }
    
    public void setBlockLength(double blkl)
    {
        blockLength = blkl;
    }
    
    public int getPassengers()
    {
        return internal.passengers;
    }
    
    public double getDesiredSpeed()
    {
        return desiredSpeed;
    }
    
    public int getRemainingAuthority()
    {
        return (int)remainingAuthority;
    }
    
    public void calcAcceleration()
    {
        double accelControl;
        double accelGrav;
        double accelFrict;
        
        double totalMass = internal.getTotalMass();
        
        //convert mph to ms for speed
        accelControl = this.power / (totalMass * (speed*0.44704+0.000001));
        //grade is 100*rise/run
        accelGrav = -Math.sin(this.grade/100)*9.8;
        //coefficient of friction = 0.1
        accelFrict = 0.01*Math.cos(this.grade/100)*9.8;
        
        //convert m/s*s to mph/s
        acceleration=accelControl+accelGrav-accelFrict;
        acceleration = acceleration * 2.23693629;
        
        if (brake > 0 && !failure.brakeFail)
        {
            acceleration -= internal.decelBrake;
        }
        
        double maxDecelEmerg = internal.getMaxDecelEmerg();
        double maxDeceleration = internal.getMaxDeceleration();
        double maxAcceleration = internal.getMaxAcceleration();

        
        if (emergencyBrake > 0 || failure.engineFail)
        {
            acceleration -= internal.maxDecelEmerg;
            if (acceleration < -maxDecelEmerg)
            {
                acceleration = -maxDecelEmerg;
            }
        }
        else if (acceleration < -maxDeceleration)
        { 
            acceleration = -maxDeceleration;
        }
        
        if (acceleration > maxAcceleration)
        {
            acceleration = maxAcceleration;
        }
        
        if (acceleration == Double.NaN)
        {
            acceleration = 0;
        }
    }
    
    //elapsed time expected to be 0.1 seconds, in seconds.
    public void calcSpeed(double elapsedTime)
    {
        speed += acceleration * elapsedTime;
        
        if (speed < 0 || speed == Double.NaN)
        {
            speed = 0;
        }
        
        if (lineNo == 1)
        {
            desiredSpeed = myLine.getSpeed(blockID, id);
            authority = myLine.getAuthority(blockID, id);
            if (authority != oldAuthority)
            {
                remainingAuthority = authority;
                oldAuthority = authority;
            }
            
        }
        /*
        else if (lineNo == 2)
        {
            desiredSpeed = green.getSpeed(blockID, id);
            authority = green.getAuthority(blockID, id);
            if (authority != oldAuthority || finishedAuthority == 1)
            {
                remainingAuthority = authority;
                oldAuthority = authority;
            }
        }
        */
    }
        
    public void calcDistance(double elapsedTime)
    {
        internal.distanceTraveled += (speed*elapsedTime)/3600;
        if (internal.distanceTraveled >= blockLength)
        {
            moveToNewBlock();
            internal.distanceTraveled=0;
        }
    }
    
    public void updateTrain(double elapsedTime)
    {
        if (speed <= 0.0000001 && doorStatus == 1 && beacon != null)
        {
            internal.boardPassengers();
        }
        calcAcceleration();
        calcSpeed(elapsedTime);
        calcDistance(elapsedTime);
        calcTemperature();
    }
        
    public void calcTemperature()
    {
        if (inputTemperature > temperature)
        {
            temperature += 0.1;
        }
        if (inputTemperature < temperature)
        {
            temperature -= 0.1;
        }
    }
    
    public void moveToNewBlock()
    {        
        
        if (lineNo == 1)
        {
            blockID = myLine.getNextBlock(blockID, prevBlockID,id);
            grade = myLine.getGrade(blockID);
            speedLimit = myLine.getSpeedLimit(blockID);
            blockLength = myLine.getBlockLength(blockID);
            beacon = myLine.getBeacon(blockID);
            authority = myLine.getAuthority(blockID, id);
            //tentative- ask Yanis
            underground = myLine.getUnderground(blockID);
            
        }
        /*
        else if (lineNo == 2)
        {
            blockID = green.getNextBlock(blockID, prevBlockID);
            grade = green.getGrade(blockID);
            speedLimit = green.getSpeedLimit(blockID);
            blockLength = green.getBlockLength(blockID);
            beacon = green.getBeacon(blockID, id);
            authority = green.getAuthority(blockID, id);
            underground = green.getUnderground(blockID);
        }
        */
        else
        {
            System.out.println("Waluigi says: 'Never miss an opportunity to hide a secret message in an unreachable if case.'");
        }
        prevBlockID = blockID;
        
        internal.distanceTraveled -=blockLength;
        remainingAuthority -= 1;
        if (remainingAuthority == 0)
        {
            finishedAuthority = 1;
        }
        else
        {
            finishedAuthority = 0;
        }
    }
    public double getDt()
    {
        return myLine.getDt(); 
    }
    public double getTotalMass()
    {
    	return internal.getTotalMass();
    }
    
    public double getMaxPower()
    {
    	return internal.getMaxPower();
    }
    
    public double getMaxAcceleration()
    {
    	return internal.getMaxAcceleration();
    }
    
}