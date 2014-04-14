/*
filename: Dimensions
author: Sarah Higbee
group: SHWOZ
date: 26/03/14
*/

package TrainSimulator;

public class Dimensions
{
    //length in miles
    public double engineLength;
    public double carLength;
    public double totalLength;
    public double height;
    public double width;
    public int cars;

    /*Class constructor, default.*/
    public Dimensions()
    {
        this.engineLength = 0.02002679353;
        this.carLength = 0.02002679353;
        cars = getCars();
        this.totalLength = engineLength + cars*carLength;
        this.height = 0.002125089477;
        this.width = 0.001646633659;
    }

    public int getCars()
    {
        return cars;
    }
    
    public void setCars(int cras)
    {
        this.cars = cras;
        calcTotalLength();
    }

    public void calcTotalLength()
    {
        totalLength = engineLength + carLength*cars;
    }
    
    public double getTotalLength()
    {
        return totalLength;
    }
    
    public double getHeight()
    {
        return height;
    }
    
    public double getWidth()
    {
        return width;
    }

}