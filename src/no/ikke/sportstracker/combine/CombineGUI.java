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




public class CombineGUI extends SingleFrameApplication {
    
    private static final Logger LOGGER = Logger.getLogger (CombineGUI.class.getName ()); 
    
    private STDocument document;
    
    private String[] cmdLineParameters; 
    
    /** The SportsTracker application context. */
    private STContext context;
    private STView view;
    
    
    /**
     * Initializes the SportsTracker application.
     */
    @Override
    protected void initialize (String[] args) {
        super.initialize (args);
        cmdLineParameters = args;
    }
    
    
    /**
     * Initializes the Look&Feel of the application. Must be done before creating the view 
     * components.
     * @param lookAndFeelClassName class name of the look and feel (or null for system default)
     */
    private void initLookAndFeel (String lookAndFeelClassName) {
        if (lookAndFeelClassName == null) {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName ();
        }
        
        try {
            UIManager.setLookAndFeel (lookAndFeelClassName);
        }
        catch (Exception e) {
            LOGGER.log (Level.WARNING, "Failed to set look&feel to " + lookAndFeelClassName + "!", e);
        }
    }
    
    /**
     * Starts up the SportsTracker application.
     */
    @Override 
    protected void startup () {
        
        // create and configure the Guice injector 
        Injector injector = Guice.createInjector (new AbstractModule () {
            public void configure () {
                // create and bind SportsTracker GUI context, which can be used everywhere
                context = new STContextImpl (getContext ());
                bind (STContext.class).toInstance (context);
            }
        });
        
        document = injector.getInstance (STDocument.class);
        document.evaluateCommandLineParameters (cmdLineParameters);
        document.loadOptions ();
        initLookAndFeel (document.getOptions ().getLookAndFeelClassName ());        
        
        view = injector.getInstance (STView.class);
        view.initView ();
        show ((View) view);
        
    }
    
    public static void main (String args[]) {        
        launch (CombineGUI.class, args);
    }
}