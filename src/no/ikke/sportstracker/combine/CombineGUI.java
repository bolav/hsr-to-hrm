package no.ikke.sportstracker.combine;

import org.jdesktop.application.SingleFrameApplication;
import de.saring.sportstracker.gui.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.swing.UIManager;
import org.jdesktop.application.View;

import no.ikke.sportstracker.combine.gui.*;



public class CombineGUI extends SingleFrameApplication {
    
    private static final Logger LOGGER = Logger.getLogger (CombineGUI.class.getName ()); 
    
    private String[] cmdLineParameters; 
    
    
    /**
     * Initializes the SportsTracker application.
     */
    @Override
    protected void initialize (String[] args) {
        super.initialize (args);
        cmdLineParameters = args;
    }
    
    
    
    /**
     * Starts up the SportsTracker application.
     */
    @Override 
    protected void startup () {
           no.ikke.sportstracker.dummy.STContextDummy.setContext(getContext());

           CombineView cv = new CombineView();
           show(cv);
        
    }
    
    public static void main (String args[]) {        
        launch (CombineGUI.class, args);
    }
}