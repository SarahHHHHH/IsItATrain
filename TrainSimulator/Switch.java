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
    int next_block;
    
    public Switch(int loc, int closed, int open)
    {
        block_id[0] = loc;
        block_id[1] = closed;
        block_id[2] = open;
        next_block = closed;
    }
    
    
    public void activateSwitch(int pos)
    {
        next_block = pos;
    }
}
