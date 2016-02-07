package view;

import control.Toolbar;

/**
 * @author ao1013
 * @version 11/7/15 TCSS 305
 */
public class PowerPaintGUI {
    
    /**
     * The start method to begin the GUI.
     */
    private Toolbar myToolBar;
    /**
     * A function to start all aspects of the GUI of power paint.
     */
    public void start() {
        myToolBar = new Toolbar();
        myToolBar.start();
    }

}
