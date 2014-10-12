package TrainSimulator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class just instantiates the Track Controller GUI
 * 
 * @author dward330
 */
public class TC_MAINGUI {
    public TrackControllerGUI trackControllerGUI;
    
    /**
     * This constructor creates the Track Controller GUI
     * 
     * @param ctc The CTC controlling this system
     * @param redLine The Red Track Line
     * @param greenLine The Green Track Line
     */
    public TC_MAINGUI(PhantomCTCGUI ctc, TrackModel redLine, TrackModel greenLine){
        /* Create and display the form */
        trackControllerGUI = new TrackControllerGUI(ctc,redLine, greenLine);
        this.trackControllerGUI.setVisible(true);
    }
}
