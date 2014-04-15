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
    int switch_id;
    double trackTemp = 75;
    double blockGrade;
    double blockElevation;
    double speedLimit;
    double blockSize;
    public double speed = 0;
    public int authority = 0;
    int blockID;
    int errorFlag = 0;

    public Block(double[] a)
    {
        blockID = (int) a[0];
        blockSize = a[1]*.000621371;
        blockGrade = a[2];
        speedLimit = a[3]*.621371;
        blockElevation = a[4]*.000621371;
        if(a[5] == 1)
        {
            station = true;
        }
        if(a[6] == 1)
        {
            underground = true;
        }
        if(a[7] == 1)
        {
            crossing = true;
        }

        switch_id = (int) a[8];

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
        return switch_id != 0;
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
            return("Station");
        }
        else
        {
            return ("None");
        }
    }
    public int isFunctional()
    {

        return this.errorFlag;
    }
}
