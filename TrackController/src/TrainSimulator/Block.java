/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author yanisoukaci
 */
package TrainSimulator;
public class Block
{
    boolean train = false;
    boolean station = false;
    boolean crossing = false;
    boolean underground = false;
    boolean switchPresent = false;
    boolean trackCircuitFailure = false;
    boolean brokenRailDetection = false;
    String stationName;
    int switch_id;
    double trackTemp = 75;
    double blockGrade;
    double blockElevation;
    double speedLimit;
    double blockSize;
    public double speed = 0;
    public int authority = 0;
    int blockID;

    public Block(String s)
    {
        String[] data = s.split(" ");
        blockID = Integer.parseInt(data[0]);
        blockSize = Double.parseDouble(data[1])*.000621371;
        blockGrade = Double.parseDouble(data[2]);
        speedLimit = Double.parseDouble(data[3])*.621371;
        blockElevation = Double.parseDouble(data[4])*.000621371;
        if(data[5].equals("none"))
        {
            station = false;
        }
        else
        {
            station = true;
            stationName = data[5];
        }
        if(data[6].equals("yes"))
        {
            underground = true;
        }
        if(data[7].equals("yes"))
        {
            crossing = true;
        }
        if(data[8].equals("switch"))
        {
            switchPresent = true;
            switch_id = Integer.parseInt(data[9]);
        }
        else if(data[8].equals("yes"))
        {
            switch_id = Integer.parseInt(data[9]);
        }

    }
    public boolean trainOnBlock()
    {
        return train;
    }
    public double heatTrack(double a)
    {
        if(a > trackTemp && a < 80)
        {
            trackTemp = a;
        }

        return trackTemp;
    }

    public boolean stationOnBlock()
    {

        return this.station;
    }

    public boolean crossingOnBlock()
    {

        return this.crossing;
    }
    
    public boolean switchOnBlock()
    {
        return switchPresent;
    }

    public double size()
    {
        return this.blockSize;
    }

    public double grade()
    {
        return this.blockGrade;
    }
    public double elevation()
    {
        return this.blockElevation;
    }

    public double speedLimit()
    {
        return this.speedLimit;
    }
    
    public int getBlockID()
    {
        return this.blockID;
    }
    public void setTrain(boolean presence)
    {
        this.train = presence;
    }
    public boolean isUnderground()
    {
        return this.underground;
    }
    public String setBeacon()
    {
        if(station)
        {
            return(stationName);
        }
        else
        {
            return ("None");
        }
    }
}
