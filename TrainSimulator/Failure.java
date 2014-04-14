/*
filename: Failure
author: Sarah Higbee
group: SHWOZ
date: 26/03/14
*/

package TrainSimulator;

//We won't need these for the demo.
//but in the future, there will be a function for randomly causing failures

public class Failure
{
    //true = failure activated
    public boolean trainFailure;
    public boolean engineFail;
    public boolean signalFail;
    public boolean brakeFail;
    public boolean wheelFail;
    //minutes
    public int timeTilRepair;

    /*Class constructor, default.*/
    public Failure()
    {
        //this.trainFailure = false;
        this.engineFail = false;
        this.signalFail = false;
        this.brakeFail = false;
        //this.wheelFail = false;
        this.timeTilRepair = 60;
    }

    public void fail()
    {
        
    }

    public int callRepair()
    {
        return 0;
    }

    public void repaired()
    {
        
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