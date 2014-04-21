/*
filename: Failure
author: Sarah Higbee
group: SHWOZ
date: 26/03/14
*/

package TrainSimulator;

//import java.util.Date;
//import java.util.Random;

public class Failure
{
    //true = failure activated
    public boolean trainFailure;
    public boolean engineFail;
    public boolean signalFail;
    public boolean brakeFail;
    //public boolean wheelFail;
    //minutes
    public int timeTilRepair;

//    //percent chance per second
//    public double chanceEngine;
//    public double chanceSignal;
//    public double chanceBrake;
//    
//    public Date dat = new Date();
//    public long time = dat.getTime();
//    public Random rand = new Random(time);
//    public double roll;
    
    public Failure()
    {
        //this.trainFailure = false;
        this.engineFail = false;
        this.signalFail = false;
        this.brakeFail = false;
        //this.wheelFail = false;
        this.timeTilRepair = 5;
        //too high for a real train, possibly too low to demonstrate in class
//        this.chanceEngine = 0.00000082671958;
//        this.chanceEngine = 0.00000082671958;
//        this.chanceEngine = 0.00000082671958;

    }

    public void fail()
    {
//        roll = 100*rand.nextDouble();
//        if (roll <= chanceEngine)
//        {
//            engineFail = true;
//        }
//        roll = 100*rand.nextDouble();
//        if (roll <= chanceSignal)
//        {
//            signalFail = true;
//        }
//        roll = 100*rand.nextDouble();
//        if (roll <= chanceBrake)
//        {
//            brakeFail = true;
//        }
        
    }

    public void callRepair()
    {
        timeTilRepair -= 1;
        if (timeTilRepair == 0)
        {
            repaired();
            timeTilRepair = 5;
        }
    }

    public void repaired()
    {
        engineFail = false;
        signalFail = false;
        brakeFail = false;
    }
    
    public void setEngineFail(boolean fal)
    {
        engineFail = fal;
    }
    
    //Which signal does this mean?
    //The authority and speed from the track?
    //The beacon?
    //Blso, ask TA to describe process of giving authority to train.
    public void setSignalFail(boolean fal)
    {
        signalFail = fal;
    }
    
    public void setBrakeFail(boolean fal)
    {
        brakeFail = fal;
    }
    
    
    public boolean getEngineFail()
    {
        return engineFail;
    }
    
    public boolean getSignalFail()
    {
        return signalFail;
    }
    
    public boolean getBrakeFail()
    {
        return brakeFail;
    }
    
    /*public boolean wheelFail()
    {
        return wheelFail;
    }*/
}