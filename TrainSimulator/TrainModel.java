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
    //units, others- Yanis: length - miles, speed- mph, power- watts
    //change comments on units
    
    //1 = open
    public int doorStatus;
    //fahrenheit 
    public double temperature;
    //1 = on
    public int lightStatus;
    //kph -> hong: 
    public double speed;
    public double desiredSpeed;
    //blocks
    public double authority;
    //joules
    public double power;
    //kph
    public double speedLimit;
    //kphph
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
    //serial numbers
    public int prevBlockID;
    public int blockID;
    //kilometers
    public double blockLength;
    //1 = red, 2 = green
    public int lineNo;
    
    public Internal internal = new Internal();
    public Failure failure = new Failure();
    public Dimensions dimensions = new Dimensions();
    TrackModel red;
    TrackModel green;
    
    /*Class constructor, default.*/
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
        red = line;
        this.prevBlockID = 0;
        if (lineNo == 1)
        {
            this.blockID = red.getStartingBlock();
            this.desiredSpeed = red.getSpeed(blockID, id);
            this.grade = red.getGrade(blockID);
            this.speedLimit = red.getSpeedLimit(blockID);
            this.blockLength = red.getBlockLength(blockID);
            this.authority = red.getAuthority(blockID, id);
        }
        else if (lineNo == 2)
        {
            blockID = green.getStartingBlock();
            desiredSpeed = green.getSpeed(blockID, id);
            grade = green.getGrade(blockID);
            speedLimit = green.getSpeedLimit(blockID);
            blockLength = green.getBlockLength(blockID);
            authority = green.getAuthority(blockID, id);
        }
        this.remainingAuthority = authority;
        this.oldAuthority = authority;
    }
    
    public void setDoorStatus(int dor)
    {
        this.doorStatus = dor;
    }
    
    public void setTemperature(double temper)
    {
        this.temperature = temper;
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
        
        if (speed == 0 && brake==0 && emergencyBrake==0)
        {
            accelControl = 0.1;
        }
        else
        {
            //convert mph to ms for speed
            accelControl = this.power / (totalMass * (speed*0.44704));
        }
        //grade is 100*rise/run
        accelGrav =     -Math.sin(this.grade/100)*9.8/totalMass;
        //coefficient of friction = 0.5
        accelFrict = 0.5*Math.cos(this.grade/100)*9.8/totalMass;
        
        acceleration=accelControl+accelGrav+accelFrict;
        
        //convert m/s*s to mph/s
        acceleration = acceleration * 2.23693629;
        
        if (brake > 0)
        {
            acceleration -= internal.decelBrake;
        }
        
        double maxDecelEmerg = internal.getMaxDecelEmerg();
        double maxDeceleration = internal.getMaxDeceleration();
        double maxAcceleration = internal.getMaxAcceleration();
        
        if (emergencyBrake > 0)
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
        
    }
    
    //elapsed time expected to be 0.1 seconds, in seconds.
    public void calcSpeed(double elapsedTime)
    {
        speed += acceleration * elapsedTime;
        
        if (lineNo == 1)
        {
            desiredSpeed = red.getSpeed(blockID, id);
            authority = red.getAuthority(blockID, id);
            if (authority != oldAuthority)
            {
                remainingAuthority = authority;
                oldAuthority = authority;
            }

        }
        else if (lineNo == 2)
        {
            desiredSpeed = green.getSpeed(blockID, id);
            authority = green.getAuthority(blockID, id);
        }
    }
    
    public void calcDistance(double elapsedTime)
    {
        internal.distanceTraveled += (speed/36)*elapsedTime;
        if (internal.distanceTraveled >= blockLength)
        {
            moveToNewBlock();
            //do I need to keep track of remaining authority? yes.
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
    }
    
    //i must set presence on block when moving to new block

    public void moveToNewBlock()
    {        
        prevBlockID = blockID;
        
        if (lineNo == 1)
        {
            blockID = red.getNextBlock(blockID, prevBlockID);
            grade = red.getGrade(blockID);
            speedLimit = red.getSpeedLimit(blockID);
            blockLength = red.getBlockLength(blockID);
            beacon = red.getBeacon(blockID, id);
            
        }
        else if (lineNo == 2)
        {
            blockID = green.getNextBlock(blockID, prevBlockID);
            grade = green.getGrade(blockID);
            speedLimit = green.getSpeedLimit(blockID);
            blockLength = green.getBlockLength(blockID);
            beacon = green.getBeacon(blockID, id);
        }
        else
        {
            System.out.println("Waluigi says: 'Never miss an opportunity to hide a secret message in an unreachable if case.'");
        }
        
        internal.distanceTraveled -=blockLength;
        remainingAuthority -= 1;
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