package TrainSimulator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dward330
 */
public class TC_MAINGUI {
    public TrackControllerGUI trackControllerGUI;
    
    public TC_MAINGUI(PhantomCTCGUI ctc, TrackModel redLine, TrackModel greenLine){
        /* Create and display the form */
        //java.awt.EventQueue.invokeLater(new Runnable() {
            //public void run() {
               trackControllerGUI = new TrackControllerGUI(ctc,redLine, greenLine);
               this.trackControllerGUI.setVisible(true);
            //}
        //});
    }
}
