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
public class Switch 
{
    int[] block_id = new int[3];
    int id;
    int next_block;
    
    public Switch(int num, int loc, int closed)
    {
        block_id[0] = loc;
        block_id[1] = closed;
        next_block = closed;
        id = num;
    }
    
    public void setOpenBlock(int open)
    {
        block_id[2] = open;
    }
    
    public void activateSwitch(int pos)
    {
        //1 for open, 0 for closed
        if(pos == 1)
        {
            next_block = block_id[2];
        }
        else if(pos == 0)
        {
            next_block = block_id[1];
        }
    }
    
    public String toString()
    {
        return ("Switch " + Integer.toString(id));
    }
}
