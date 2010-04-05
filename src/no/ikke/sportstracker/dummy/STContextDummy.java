/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ikke.sportstracker.dummy;

import de.saring.sportstracker.gui.STContext;
import de.saring.util.unitcalc.FormatUtils;
import de.saring.util.ResourceReader;

import java.awt.Component;

import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.ApplicationContext;



/**
 *
 * @author bolav
 */
public class STContextDummy implements STContext {

    private ResourceReader resReader;
    private static STContextDummy instance;
    private ApplicationContext ctx;
    private FormatUtils formatutils = new FormatUtils(FormatUtils.UnitSystem.Metric, FormatUtils.SpeedView.DistancePerHour);

    public STContextDummy () {
    }

    public static STContextDummy getDummy () {
        if (instance == null) {
            instance = new STContextDummy();
        }
        return instance;
    }

    public static void setContext (ApplicationContext ctx)  {
        STContextDummy st = getDummy();
        st.setSAFContext(ctx);
    }

    public void setFormatUtils (FormatUtils formatUtils) {
        
    }

    public FormatUtils getFormatUtils () {
        return formatutils;
    }

    public ResourceReader getResReader () {
        return new ResourceReader(ctx.getResourceMap());
    }

    public int showConfirmDialog (Component parent, String titleKey, String messageKey) {
        return 1;
    }

    public void showMessageDialog (Component parent, int msgType, String titleKey, String messageKey, Object... arguments) {
    }

    public void showDialog (javax.swing.JDialog dlg) {
    }

    public javax.swing.JFrame getMainFrame () {
        return null;
    }

    public ApplicationContext getSAFContext () {
        return ctx;
    }

    public void setSAFContext (ApplicationContext ctx) {
        this.ctx = ctx;
    }


}
