/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.ikke.sportstracker.dummy;

import de.saring.polarviewer.gui.PVDocument;
import de.saring.polarviewer.data.PVExercise;


/**
 *
 * @author bolav
 */
public class DummyDocument extends PVDocument {
    public void setExercise (PVExercise e) {
        this.exercise = e;
        exerciseFilename = "Unknown";

    }
}
