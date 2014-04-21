/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package TrainSimulator;
import java.util.*;

/**
 *
 * @author yanisoukaci
 */
public class Clock extends TimerTask 
{
    PhantomCTCGUI ctc;
    TC_MAINGUI tc;
    ArrayList<TrainController> trains;
    public Clock(PhantomCTCGUI g, TC_MAINGUI c, ArrayList<TrainController> t)
    {
        ctc = g;
        tc = c;
        trains = t;
    }
    
    @Override
    public void run()
    {
        for(TrainController t : trains)
        {
            t.executeGUI();
        }
        tc.trackControllerGUI.refreshBlockInfoDisplay();
    }
}
